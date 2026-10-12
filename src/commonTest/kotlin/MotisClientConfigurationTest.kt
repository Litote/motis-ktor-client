import io.ktor.http.HttpHeaders
import kotlinx.coroutines.test.runTest
import org.litote.motis.client.LIBRARY_USER_AGENT
import org.litote.motis.client.client.GeocodeClient
import org.litote.motis.client.motisClientConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MotisClientConfigurationTest {
    @Test
    fun requestsTargetTransitousWithCompliantUserAgent() =
        runTest {
            val transitous = MockTransitous { respondJson(TransitousFixtures.geocodeParis) }

            GeocodeClient(transitous.configuration).geocode(text = "Paris Gare de Lyon")

            val request = transitous.lastRequest
            assertEquals("api.transitous.org", request.url.host)
            assertEquals(EXPECTED_USER_AGENT, request.headers[HttpHeaders.UserAgent])
        }

    @Test
    fun libraryUserAgentIdentifiesTheLibraryAndItsRepository() {
        assertTrue(
            Regex("""motis-ktor-client/\S+ \(\+https://github\.com/Litote/motis-ktor-client\)""").matches(LIBRARY_USER_AGENT),
            LIBRARY_USER_AGENT,
        )
    }

    @Test
    fun applicationIdentityIsRequired() {
        assertFailsWith<IllegalArgumentException> {
            motisClientConfiguration(applicationName = " ", applicationVersion = "1.0", contact = "me@example.org")
        }
        assertFailsWith<IllegalArgumentException> {
            motisClientConfiguration(applicationName = "MyApp", applicationVersion = "", contact = "me@example.org")
        }
        assertFailsWith<IllegalArgumentException> {
            motisClientConfiguration(applicationName = "MyApp", applicationVersion = "1.0", contact = "")
        }
    }
}
