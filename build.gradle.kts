import groovy.json.JsonSlurper
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Properties

plugins {
    alias(openapi.plugins.kotlin.multiplatform)
    alias(openapi.plugins.serialization)
    alias(libs.plugins.generator)
    alias(openapi.plugins.sonarqube)
    alias(openapi.plugins.vanniktech.maven.publish)
    alias(libs.plugins.dokka)
    id("signing")
    id("jacoco")
}

group = providers.gradleProperty("GROUP").orNull ?: error("Missing gradle.properties 'GROUP'")
version = providers.gradleProperty("VERSION_NAME").orNull ?: error("Missing gradle.properties 'VERSION_NAME'")

tasks.withType<Jar>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

signing {
    val inMemoryKey = providers.gradleProperty("signingInMemoryKey").orNull
    if (inMemoryKey != null) {
        val keyId = providers.gradleProperty("signingInMemoryKeyId").orNull
        val password = providers.gradleProperty("signingInMemoryKeyPassword").orNull ?: ""
        useInMemoryPgpKeys(keyId, inMemoryKey, password)
    }
    isRequired = inMemoryKey != null
}

// identifies this library in the User-Agent header, after the application identity
val libraryUserAgent = "motis-ktor-client/$version (+https://github.com/Litote/motis-ktor-client)"

val generateLibraryInfo = tasks.register("generateLibraryInfo") {
    group = "build"
    description = "Generates the LIBRARY_USER_AGENT constant from the project version."
    val userAgent = libraryUserAgent
    val outputDir = layout.buildDirectory.dir("generated/libraryInfo/src/commonMain/kotlin")
    inputs.property("userAgent", userAgent)
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("org/litote/motis/client/LibraryInfo.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            |package org.litote.motis.client
            |
            |internal const val LIBRARY_USER_AGENT: String = "$userAgent"
            |""".trimMargin(),
        )
    }
}

kotlin {
    jvmToolchain(25)
    explicitApi()

    // public API dump in api/: checkKotlinAbi runs with `check`, updateKotlinAbi after an intended API change
    @OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
    abiValidation()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xjdk-release=17")
        }
        // integration tests against the public Transitous API: not run by `check`, see jvmIntegrationTest
        val main = compilations.getByName("main")
        compilations.create("integrationTest") {
            associateWith(main)
            defaultSourceSet.dependencies {
                implementation(kotlin("test-junit5"))
                implementation(libs.ktor.client.engine.defaults)
                implementation(libs.coroutines.test)
            }
        }
    }
    js {
        browser()
        nodejs()
    }
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }
    linuxX64()
    macosArm64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            kotlin.srcDir(generateLibraryInfo)
            dependencies {
                // exposed by the public API (ClientConfiguration, HttpClient, LogLevel, Json)
                api(openapi.ktor.core)
                api(openapi.ktor.log)
                api(openapi.serialization)
                implementation(openapi.ktor.contentnegotiation)
                implementation(openapi.ktor.json)
                implementation(openapi.coroutines)
                // no engine: the application provides one (or io.ktor:ktor-client-engine-defaults)
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.ktor.client.mock)
            implementation(libs.coroutines.test)
        }
    }
}

tasks.register<Test>("jvmIntegrationTest") {
    group = "verification"
    description = "Runs the integration tests against the public Transitous API (network access required)."
    val compilation = kotlin.jvm().compilations.getByName("integrationTest")
    testClassesDirs = compilation.output.classesDirs
    classpath = compilation.output.allOutputs + compilation.runtimeDependencyFiles
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

apiClientGenerator {
    generators {
        create("motis") {
            openApiFile = file("src/commonMain/openapi/motis.yaml")
            outputDirectory = file("build/generated/motis")
            basePackage = "org.litote.motis.client"
            // fallback when ClientConfiguration is created directly: prefer motisClientConfiguration(...)
            userAgent = libraryUserAgent
            engine = "platform"
            modulesIds =
                setOf(
                    "UnknownEnumValueModule",
                    "KotlinTimeInstantModule",
                )
        }
    }
}

// JVM coverage of the common code (the tests in src/commonTest run on the JVM through jvmTest)
val jacocoAggregatedReport = tasks.register<JacocoReport>("jacocoAggregatedReport") {
    group = "verification"
    description = "Generates the JaCoCo coverage report of the jvmTest tests, used by SonarCloud."
    val jvmTest = tasks.named<Test>("jvmTest")
    dependsOn(jvmTest)
    executionData.setFrom(layout.buildDirectory.file("jacoco/jvmTest.exec"))
    sourceDirectories.setFrom(
        kotlin.sourceSets.named("commonMain").map { it.kotlin.srcDirs },
        kotlin.sourceSets.named("jvmMain").map { it.kotlin.srcDirs },
    )
    classDirectories.setFrom(
        layout.buildDirectory.dir("classes/kotlin/jvm/main").map { dir ->
            fileTree(dir) { exclude("**/*\$\$serializer.class") }
        },
    )
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

sonar {
    properties {
        property("sonar.projectKey", "Litote_motis-ktor-client")
        property("sonar.organization", "litote")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/jacoco/jacocoAggregatedReport/jacocoAggregatedReport.xml").get().asFile.absolutePath,
        )
    }
}

/**
 * Regenerates gradle/verification-metadata.xml after dependency upgrades.
 *
 * Uses --refresh-dependencies to force re-downloading all artifacts (including .module files
 * from the Gradle Plugin Portal) so the generated metadata matches a clean CI environment.
 * Without this flag, locally-cached resolutions can differ from CI and cause verification failures.
 *
 * Run this after every dependency change (gradle/libs.versions.toml, generator version, Gradle wrapper):
 *   ./gradlew updateVerificationMetadata
 */
tasks.register("updateVerificationMetadata") {
    group = "verification"
    description = "Regenerates gradle/verification-metadata.xml after dependency upgrades. Run after every dependency change."
    notCompatibleWithConfigurationCache("Spawns a Gradle subprocess that writes verification-metadata.xml")
    doLast {
        val result = ProcessBuilder(
            "${rootDir}/gradlew",
            "--write-verification-metadata", "sha256",
            "--refresh-dependencies",
            "dependencies", "check", "jacocoAggregatedReport", "compileIntegrationTestKotlinJvm",
            "dokkaGeneratePublicationHtml", "publishToMavenLocal",
        )
            .directory(rootDir)
            .inheritIO()
            .start()
            .waitFor()
        check(result == 0) { "updateVerificationMetadata failed with exit code $result" }
    }
}

/**
 * Fetches quality gate status and open issues from SonarCloud.
 * Fails the build if the gate is not OK or any issue/hotspot remains.
 *
 * Waits for SonarCloud to finish processing the analysis (async CE task) before checking.
 *
 * Prerequisites: run `./gradlew check jacocoAggregatedReport sonar` first to push the analysis.
 * Token: set `systemProp.sonar.token=<token>` in ~/.gradle/gradle.properties
 *
 * Full quality loop:
 *   ./gradlew check jacocoAggregatedReport sonar sonarCheck
 */
tasks.register("sonarCheck") {
    group = "verification"
    description = "Verifies SonarCloud quality gate: fails if gate != OK, issues > 0, or hotspots > 0."
    mustRunAfter("sonar")
    notCompatibleWithConfigurationCache("Reads sonar/report-task.txt generated at execution time")
    doLast {
        val token = project.providers.systemProperty("sonar.token")
            .orElse(project.providers.environmentVariable("SONAR_TOKEN"))
            .orNull
            ?: error("sonar.token not set. Add systemProp.sonar.token=<token> to ~/.gradle/gradle.properties")

        val projectKey = "Litote_motis-ktor-client"
        val client = HttpClient.newHttpClient()
        val slurper = JsonSlurper()

        fun fetch(url: String): Any {
            val req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer $token")
                .GET()
                .build()
            val resp = client.send(req, HttpResponse.BodyHandlers.ofString())
            check(resp.statusCode() == 200) {
                "SonarCloud API error ${resp.statusCode()}: ${resp.body()}"
            }
            return slurper.parseText(resp.body())
        }

        val reportTaskFile = layout.buildDirectory.file("sonar/report-task.txt").get().asFile
        if (reportTaskFile.exists()) {
            val props = Properties().also { it.load(reportTaskFile.inputStream()) }
            val ceTaskId = props.getProperty("ceTaskId")
            if (ceTaskId != null) {
                logger.lifecycle("⏳ Waiting for SonarCloud analysis (task $ceTaskId) to complete…")
                val maxWaitMs = 120_000L
                val pollIntervalMs = 5_000L
                val start = System.currentTimeMillis()
                while (true) {
                    @Suppress("UNCHECKED_CAST")
                    val taskData = fetch("https://sonarcloud.io/api/ce/task?id=$ceTaskId") as Map<String, Any>
                    @Suppress("UNCHECKED_CAST")
                    val taskStatus = (taskData["task"] as Map<String, Any>)["status"] as String
                    when (taskStatus) {
                        "SUCCESS" -> {
                            logger.lifecycle("✅ Analysis processing complete.")
                            break
                        }
                        "FAILED", "CANCELLED" -> error("SonarCloud analysis task $ceTaskId ended with status: $taskStatus")
                        else -> {
                            check(System.currentTimeMillis() - start < maxWaitMs) {
                                "Timed out waiting for SonarCloud analysis task $ceTaskId (status: $taskStatus)"
                            }
                            Thread.sleep(pollIntervalMs)
                        }
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        val qgData = fetch("https://sonarcloud.io/api/qualitygates/project_status?projectKey=$projectKey") as Map<String, Any>
        @Suppress("UNCHECKED_CAST")
        val gateStatus = (qgData["projectStatus"] as Map<String, Any>)["status"] as String

        @Suppress("UNCHECKED_CAST")
        val issuesData = fetch("https://sonarcloud.io/api/issues/search?projectKeys=$projectKey&resolved=false&ps=1") as Map<String, Any>
        val issueCount = (issuesData["total"] as Number).toInt()

        @Suppress("UNCHECKED_CAST")
        val hotspotsData = fetch("https://sonarcloud.io/api/hotspots/search?projectKey=$projectKey&status=TO_REVIEW&ps=1") as Map<String, Any>
        @Suppress("UNCHECKED_CAST")
        val hotspotCount = ((hotspotsData["paging"] as Map<String, Any>)["total"] as Number).toInt()

        logger.lifecycle("")
        logger.lifecycle("╔════════════════════════════════════╗")
        logger.lifecycle("║     SonarCloud Quality Gate        ║")
        logger.lifecycle("╠════════════════════════════════════╣")
        logger.lifecycle("║  Gate    : ${gateStatus.padEnd(24)}║")
        logger.lifecycle("║  Issues  : ${issueCount.toString().padEnd(24)}║")
        logger.lifecycle("║  Hotspots: ${hotspotCount.toString().padEnd(24)}║")
        logger.lifecycle("╚════════════════════════════════════╝")
        logger.lifecycle("")

        // NONE = gate not yet calibrated (no baseline period); ERROR = gate conditions failed.
        // Accepting NONE is safe because issues and hotspots are checked independently above.
        val failures = buildList {
            if (gateStatus != "OK" && gateStatus != "NONE") add("quality gate status is '$gateStatus' (expected OK or NONE)")
            if (issueCount > 0) add("$issueCount unresolved issue(s)")
            if (hotspotCount > 0) add("$hotspotCount unreviewed security hotspot(s)")
        }

        check(failures.isEmpty()) {
            "❌ SonarCloud check FAILED:\n${failures.joinToString("\n") { "  • $it" }}"
        }

        logger.lifecycle("✅ SonarCloud quality gate passed — 0 issues, 0 hotspots.")
    }
}
