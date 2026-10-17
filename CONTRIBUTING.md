# Contributing

## Prerequisites

- JDK 17+ (the build uses a JDK 25 toolchain, provisioned automatically)
- Gradle 9+ (use the wrapper: `./gradlew`)

## Build

```bash
./gradlew build                  # generates the client, compiles all targets, runs tests
./gradlew generateMotis          # regenerates the client only (generated/motis)
./gradlew publishToMavenLocal    # publishes all targets to ~/.m2
```

### Generated client

The generated client is versioned in `generated/motis`, so that its evolutions can be reviewed. It is regenerated
from the OpenAPI specification at each build, and the CI fails if the committed version is not up to date.
To update the API, replace `src/commonMain/openapi/motis.yaml` with a newer MOTIS specification, run the build
and commit the changes of `generated/`. Do not edit `generated/` by hand: the build overwrites it.

The generator version (`generator` in `gradle/libs.versions.toml`) also selects its published version catalog
(`openapi`), which provides the Kotlin, Ktor, kotlinx-serialization and coroutines versions.

## Tests

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

## Coding conventions

Follow the [official Kotlin conventions](https://kotlinlang.org/docs/coding-conventions.html).

## Commit signing

All commits merged into `main` **must be signed**.

See [Signing commits](https://docs.github.com/en/authentication/managing-commit-signature-verification/signing-commits)

## Conventional Commits

All PR titles **must** follow the [Conventional Commits](https://www.conventionalcommits.org/) specification.
This is enforced automatically via `amannn/action-semantic-pull-request` in `ci.yml`.

### Branch naming

PR branches should mirror the PR title using the same Conventional Commits type as prefix:

```
<type>/<short-kebab-description>

# Examples:
feat/stoptimes-pagination
fix/plan-time-parameter
chore/update-motis-spec
```

| Type | Version bump | When to use |
|------|-------------|-------------|
| `feat:` | Minor (`0.3.0` → `0.4.0`) | New user-facing feature |
| `fix:` | Patch (`0.3.0` → `0.3.1`) | Bug fix |
| `perf:` | Patch | Performance improvement |
| `feat!:` / `BREAKING CHANGE:` | Major (`0.3.0` → `1.0.0`) | Breaking API change |
| `chore:`, `docs:`, `test:`, `refactor:`, `ci:` | No release | Internal changes |

> **Note:** While the major version is `0`, `feat:` commits bump the minor version (not major). This is controlled by `bump-minor-pre-major: true` in `release-please-config.json`.

## Before opening a PR

The CI analyses the project with [SonarCloud](https://sonarcloud.io/summary/new_code?id=Litote_motis-ktor-client)
(`SONAR_TOKEN` secret). Run the same analysis locally:

```bash
./gradlew check jacocoAggregatedReport sonar sonarCheck
```

`sonarCheck` must pass (gate OK, 0 issues, 0 hotspots). It requires `systemProp.sonar.token` in
`~/.gradle/gradle.properties`; without a token, let the CI run the analysis.

The public API is recorded in `api/` (Kotlin ABI validation): `check` fails if it changes.
After a public API change (including a new OpenAPI specification or generator version), update the API dump
in `api/` and check the diff: a removed or changed declaration is a breaking change (`feat!:`).

```bash
./gradlew updateKotlinAbi
```

After a dependency change (`gradle/libs.versions.toml`, generator version, Gradle wrapper), regenerate the
dependency verification metadata:

```bash
./gradlew updateVerificationMetadata
```

The generated client in `generated/motis` is committed: after a change of the OpenAPI specification or of the
generator version, commit the regenerated files with the change (the CI checks they are up to date).
For a Dependabot generator update, `dependabot-gradle-verification.yml` commits them with the verification metadata.

## Release

Releases are managed by [release-please](https://github.com/googleapis/release-please) from
[Conventional Commits](#conventional-commits), and automated by `release-please.yml`:

1. each push on `main` updates the release PR (version in `.release-please-manifest.json`, `CHANGELOG.md`);
2. merging the release PR creates the tag and a **draft** GitHub release;
3. the `publish` (Linux) and `publish-macos` (Apple targets) jobs publish to Maven Central;
4. the draft release is published, and the KDoc is deployed to [GitHub Pages](https://litote.github.io/motis-ktor-client/).

The version published is the tag version: `VERSION_NAME` in `gradle.properties` stays `main-SNAPSHOT`.
A failed release can be published again with the `workflow_dispatch` trigger of `release-please.yml` (input: the tag).

A `main-SNAPSHOT` version is published to Maven Central every Monday (`publish-snapshot.yml`, also runnable manually).
