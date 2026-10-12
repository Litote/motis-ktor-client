# motis-ktor-client

[![KDoc](https://img.shields.io/badge/KDoc-API_Reference-blue)](https://litote.github.io/motis-ktor-client/)
[![Maven Central](https://img.shields.io/maven-central/v/org.litote/motis-ktor-client)](https://central.sonatype.com/artifact/org.litote/motis-ktor-client)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Litote_motis-ktor-client&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)

Kotlin Multiplatform [Ktor](https://ktor.io) client for the [MOTIS](https://github.com/motis-project/motis) routing API,
as deployed by [Transitous](https://transitous.org/api/).

The client is generated from the MOTIS v2.10.2 OpenAPI specification
(`src/commonMain/openapi/motis.yaml`) with
[openapi-ktor-client-generator](https://github.com/Litote/openapi-ktor-client-generator).

## Targets

| Platform | Targets                                        |
|----------|------------------------------------------------|
| JVM      | `jvm`                                          |
| Web      | `js` (browser, Node.js), `wasmJs` (browser, Node.js) |
| Native   | `linuxX64`, `macosArm64`, `iosArm64`, `iosSimulatorArm64` |

## Installation

```kotlin
// build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("org.litote:motis-ktor-client:<version>")
            // the library does not impose an HTTP engine: add one
            implementation("io.ktor:ktor-client-engine-defaults:3.6.0")
        }
    }
}
```

`ktor-client-engine-defaults` selects an engine for each platform (OkHttp on the JVM, Curl on Linux,
Darwin on Apple platforms, Js on the web). You can instead add a specific engine dependency
and pass it with the `engine` parameter of `motisClientConfiguration`.

## Usage

### Configuration

```kotlin
val configuration =
    motisClientConfiguration(
        applicationName = "MyApp",
        applicationVersion = "1.2.0",
        contact = "https://myapp.example",
    )
```

The [Transitous usage policy](https://transitous.org/api/) requires every request to send a `User-Agent`
header containing the application name, the application version and a way of contact (e-mail address
or website URL). `motisClientConfiguration` builds it, followed by the library identifier:

```
User-Agent: MyApp/1.2.0 (+https://myapp.example) motis-ktor-client/<version> (+https://github.com/Litote/motis-ktor-client)
```

In a browser, the `User-Agent` header cannot be overridden: Transitous accepts the `Referer` header instead,
provided your website displays contact information.

Other parameters: `baseUrl` (default `https://api.transitous.org/`), `logLevel` (Ktor logging level,
default `NONE`) and `engine`.

### Clients

One client per API tag, all sharing the same configuration:

| Client            | Operations                                                                 |
|-------------------|----------------------------------------------------------------------------|
| `RoutingClient`   | `plan`, `oneToMany`, `oneToManyPost`, `oneToManyIntermodal`, `oneToManyIntermodalPost`, `oneToAll` |
| `GeocodeClient`   | `geocode`, `reverseGeocode`                                                |
| `TimetableClient` | `trip`, `refreshItinerary`, `refreshItineraryPost`, `stoptimes`            |
| `MapClient`       | `trips`, `initial`, `stops`, `levels`, `routes`, `routeDetails`, `rentals` |
| `DebugClient`     | `transfers`                                                                |
| `Client`          | `health`                                                                   |

Each operation returns a sealed class: `…ResponseSuccess` (typed `body`), `…ResponseFailure`
(documented error `body`) or `…ResponseUnknownFailure` (other status code or exception).
Response headers are available on every case.

### Example: plan a journey

```kotlin
val geocode = GeocodeClient(configuration)
val routing = RoutingClient(configuration)

suspend fun stopId(text: String): String? =
    when (val response = geocode.geocode(text = text, type = listOf(LocationType.STOP), numResults = 1)) {
        is GeocodeClient.GeocodeResponseSuccess -> response.body.firstOrNull()?.id
        else -> null
    }

val from = stopId("Paris Gare de Lyon") ?: error("not found")
val to = stopId("Lyon Part-Dieu") ?: error("not found")

when (
    val plan =
        routing.plan(
            fromPlace = from,
            toPlace = to,
            time = Clock.System.now(),
            transitModes = listOf(Mode.HIGHSPEED_RAIL, Mode.LONG_DISTANCE),
            maxTransfers = 2,
        )
) {
    is RoutingClient.PlanResponseSuccess -> {
        plan.body.itineraries.forEach { itinerary ->
            println("${itinerary.startTime} -> ${itinerary.endTime}, ${itinerary.transfers} transfer(s)")
        }
        // next page
        // routing.plan(fromPlace = from, toPlace = to, pageCursor = plan.body.nextPageCursor)
    }

    is RoutingClient.PlanResponseFailure -> println("error: ${plan.body.error}")
    is RoutingClient.PlanResponseUnknownFailure -> println("HTTP ${plan.statusCode}")
}
```

Dates are `kotlin.time.Instant`. Enums include an `UNKNOWN_` value, so values added to the API later
do not break deserialization.

## Development

```bash
./gradlew build                  # generates the client, compiles all targets, runs tests
./gradlew generateMotis          # regenerates the client only (build/generated/motis)
./gradlew publishToMavenLocal    # publishes all targets to ~/.m2
```

### Quality

The CI analyses the project with [SonarCloud](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
(`SONAR_TOKEN` secret). To run the analysis locally, set `systemProp.sonar.token=<token>` in `~/.gradle/gradle.properties`:

```bash
./gradlew check jacocoAggregatedReport sonar sonarCheck
```

`sonarCheck` fails if the quality gate is not OK, or if any issue or security hotspot remains.

The public API is recorded in `api/` (Kotlin ABI validation): `check` fails if it changes.
After an intended API change, regenerate the dump with `./gradlew updateKotlinAbi` and commit it;
a breaking change requires a `feat!:` commit.

Dependencies are verified with `gradle/verification-metadata.xml`. After a dependency change
(`gradle/libs.versions.toml`, Gradle wrapper), regenerate it with `./gradlew updateVerificationMetadata`.

The generated sources are not versioned: they are regenerated from the OpenAPI specification at each build.
To update the API, replace `src/commonMain/openapi/motis.yaml` with a newer MOTIS specification.

The generator version (`generator` in `gradle/libs.versions.toml`) also selects its published version catalog
(`openapi`), which provides the Kotlin, Ktor, kotlinx-serialization and coroutines versions.

### Tests

The tests in `src/commonTest` run on every target against a mocked Transitous API (Ktor `MockEngine`):
no network access is required. Apple targets tests require a macOS host.

```bash
./gradlew check                  # all targets available on the host
```

The integration tests in `src/jvmIntegrationTest` call the public Transitous API: they require network access,
depend on the Transitous timetable data and are not run by `check`.

```bash
./gradlew jvmIntegrationTest
```

### Release

Releases are managed by [release-please](https://github.com/googleapis/release-please) from
[Conventional Commits](https://www.conventionalcommits.org/) (see [CONTRIBUTING.md](CONTRIBUTING.md)):
each push on `main` updates a release PR, and merging it creates the tag and the GitHub release, publishes all targets
to Maven Central and the KDoc to [GitHub Pages](https://litote.github.io/motis-ktor-client/).

A `main-SNAPSHOT` version is published to Maven Central every Monday (`publish-snapshot.yml`, also runnable manually).

## License

This project is licensed under the [Apache License 2.0](LICENSE).

The OpenAPI specification `src/commonMain/openapi/motis.yaml` comes from the
[MOTIS project](https://github.com/motis-project/motis) and is distributed under the
[MIT License](src/commonMain/openapi/LICENSE-MOTIS).
