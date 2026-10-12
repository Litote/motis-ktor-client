# Contributing

## Prerequisites

- JDK 17+ (the build uses a JDK 25 toolchain, provisioned automatically)
- Gradle 9+ (use the wrapper: `./gradlew`)

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

```bash
./gradlew check jacocoAggregatedReport sonar sonarCheck
```

`sonarCheck` must pass (gate OK, 0 issues, 0 hotspots). It requires `systemProp.sonar.token` in
`~/.gradle/gradle.properties`; without a token, let the CI run the analysis.

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

## Release

Releases are automated by `release-please.yml`:

1. each push on `main` updates the release PR (version in `.release-please-manifest.json`, `CHANGELOG.md`);
2. merging the release PR creates the tag and a **draft** GitHub release;
3. the `publish` (Linux) and `publish-macos` (Apple targets) jobs publish to Maven Central;
4. the draft release is published, and the KDoc is deployed to GitHub Pages.

The version published is the tag version: `VERSION_NAME` in `gradle.properties` stays `main-SNAPSHOT`.
A failed release can be published again with the `workflow_dispatch` trigger of `release-please.yml` (input: the tag).
