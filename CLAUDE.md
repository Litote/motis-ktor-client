# motis-ktor-client

## SonarCloud after every code change

After modifying code — Kotlin sources, Gradle build scripts, the OpenAPI specification or GitHub workflows
(SonarCloud also analyses `.github/workflows/*.yml`) — run the full quality loop and fix every reported issue
before considering the task done:

```bash
./gradlew check jacocoAggregatedReport sonar sonarCheck
```

The CI fails on the same `sonarCheck` (quality gate OK, 0 issues, 0 hotspots), so an issue left locally
blocks the push. The token comes from `systemProp.sonar.token` in `~/.gradle/gradle.properties`; if it is
missing, say so instead of skipping the check silently. Use the SonarQube MCP tools to read the issue details.
