import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.litote.motis.client.client.GeocodeClient
import org.litote.motis.client.model.LocationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GeocodeClientTest {
    @Test
    fun geocodeSendsParametersAndReturnsMatches() =
        runTest {
            val transitous = MockTransitous { respondJson(TransitousFixtures.geocodeParis) }

            val response =
                GeocodeClient(transitous.configuration)
                    .geocode(text = "Paris Gare de Lyon", type = listOf(LocationType.STOP), numResults = 1)

            val request = transitous.lastRequest
            assertEquals("/api/v1/geocode", request.url.encodedPath)
            assertEquals("Paris Gare de Lyon", request.url.parameters["text"])
            assertEquals("STOP", request.url.parameters["type"])
            assertEquals("1", request.url.parameters["numResults"])

            assertIs<GeocodeClient.GeocodeResponseSuccess>(response)
            val match = response.body.single()
            assertEquals(TransitousFixtures.PARIS_GARE_DE_LYON, match.id)
            assertEquals("Gare de Lyon", match.name)
            assertEquals(LocationType.STOP, match.type)
            assertEquals(listOf("France", "Paris"), match.areas.map { it.name })
        }

    @Test
    fun geocodeReturnsDocumentedFailure() =
        runTest {
            val transitous = MockTransitous { respondJson(TransitousFixtures.badRequest, HttpStatusCode.BadRequest) }

            val response = GeocodeClient(transitous.configuration).geocode(text = "")

            assertIs<GeocodeClient.GeocodeResponseFailure>(response)
            assertEquals("invalid request", response.body.error)
        }

    @Test
    fun geocodeReturnsUnknownFailureOnUndocumentedStatus() =
        runTest {
            val transitous = MockTransitous { respondJson("", HttpStatusCode.TooManyRequests) }

            val response = GeocodeClient(transitous.configuration).geocode(text = "Paris")

            assertIs<GeocodeClient.GeocodeResponseUnknownFailure>(response)
            assertEquals(429, response.statusCode)
        }
}
