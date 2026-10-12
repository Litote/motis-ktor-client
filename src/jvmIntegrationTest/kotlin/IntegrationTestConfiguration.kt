import io.ktor.client.plugins.logging.LogLevel
import org.litote.motis.client.LIBRARY_USER_AGENT
import org.litote.motis.client.motisClientConfiguration

val EXPECTED_USER_AGENT: String =
    "motis-ktor-client-integration-tests/1.0 (+https://github.com/Litote/motis-ktor-client) $LIBRARY_USER_AGENT"

val integrationTestConfiguration =
    motisClientConfiguration(
        applicationName = "motis-ktor-client-integration-tests",
        applicationVersion = "1.0",
        contact = "https://github.com/Litote/motis-ktor-client",
        logLevel = LogLevel.INFO,
    )
