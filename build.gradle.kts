import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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

// client generated from the OpenAPI specification, versioned (the CI checks it is up to date)
val generatedClientDir = file("generated/motis")

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
            // versioned, to follow the evolutions of the generated client: see generateMotis below
            outputDirectory = generatedClientDir
            basePackage = "org.litote.motis.client"
            // fallback when ClientConfiguration is created directly: prefer motisClientConfiguration(...).
            // Without the version, so that the versioned generated code does not change at each release.
            userAgent = "motis-ktor-client (+https://github.com/Litote/motis-ktor-client)"
            engine = "platform"
            modulesIds =
                setOf(
                    "UnknownEnumValueModule",
                    "KotlinTimeInstantModule",
                )
        }
    }
}

// the generator does not remove the files of the types removed from the specification: start from an empty directory
tasks.matching { it.name == "generateMotis" }.configureEach {
    val outputDir = generatedClientDir
    doFirst { outputDir.deleteRecursively() }
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
        property("sonar.qualitygate.wait", true)
        // generated client: its quality is the generator's concern
        property("sonar.exclusions", "generated/**")
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
