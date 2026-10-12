import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.test.runTest
import org.litote.motis.client.client.GeocodeClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Integration test against the public Transitous API (requires network access).
 */
class GeocodeClientIntegrationTest {
    @Test
    fun geocodeReturnsMatchesWithCompliantUserAgent() =
        runTest {
            val sentUserAgents = mutableListOf<String?>()
            integrationTestConfiguration.client.plugin(HttpSend).intercept { request ->
                sentUserAgents += request.headers[HttpHeaders.UserAgent]
                execute(request)
            }
            val response = GeocodeClient(integrationTestConfiguration).geocode(text = "Paris Gare de Lyon")
            assertIs<GeocodeClient.GeocodeResponseSuccess>(response)
            assertTrue(response.body.isNotEmpty())
            assertEquals(listOf<String?>(EXPECTED_USER_AGENT), sentUserAgents)
        }
}
