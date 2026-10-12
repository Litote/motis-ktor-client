rootProject.name = "motis-ktor-client"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val generatorVersion = run {
    val toml = file("gradle/libs.versions.toml").readText()
    Regex("""generator\s*=\s*"([^"]+)"""").find(toml)?.groupValues[1] ?: error("generator version not found")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("openapi") {
            from("org.litote.openapi.ktor.client.generator:version-catalog:$generatorVersion")
        }
    }
}
