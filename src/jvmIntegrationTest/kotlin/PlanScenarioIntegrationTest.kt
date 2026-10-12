import kotlinx.coroutines.test.runTest
import org.litote.motis.client.client.GeocodeClient
import org.litote.motis.client.client.RoutingClient
import org.litote.motis.client.client.TimetableClient
import org.litote.motis.client.model.LocationType
import org.litote.motis.client.model.Mode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

/**
 * End-to-end scenario against the public Transitous API (requires network access):
 * geocode -> plan (many parameters) -> next page -> trip details.
 */
class PlanScenarioIntegrationTest {
    private val configuration = integrationTestConfiguration
    private val geocodeClient = GeocodeClient(configuration)
    private val routingClient = RoutingClient(configuration)
    private val timetableClient = TimetableClient(configuration)

    private suspend fun stopId(text: String): String {
        val response = geocodeClient.geocode(text = text, type = listOf(LocationType.STOP), numResults = 1)
        assertIs<GeocodeClient.GeocodeResponseSuccess>(response)
        return response.body.first().id
    }

    @Test
    fun planParisToLyonThenNextPageAndTripDetails() =
        runTest {
            val from = stopId("Paris Gare de Lyon")
            val to = stopId("Lyon Part-Dieu")
            val departure = Clock.System.now() + 1.days

            val plan =
                routingClient.plan(
                    fromPlace = from,
                    toPlace = to,
                    time = departure,
                    transitModes = listOf(Mode.HIGHSPEED_RAIL, Mode.LONG_DISTANCE, Mode.REGIONAL_RAIL),
                    preTransitModes = listOf(Mode.WALK),
                    postTransitModes = listOf(Mode.WALK),
                    directModes = listOf(Mode.WALK),
                    maxTransfers = 2,
                    numItineraries = 3,
                    searchWindow = 2 * 3600,
                    pedestrianSpeed = 1.2,
                    withScheduledSkippedStops = true,
                    language = listOf("fr"),
                )
            assertIs<RoutingClient.PlanResponseSuccess>(plan)
            val itineraries = plan.body.itineraries
            assertTrue(itineraries.isNotEmpty(), "no itinerary found")

            itineraries.forEach { itinerary ->
                println("${itinerary.startTime} -> ${itinerary.endTime} (${itinerary.duration / 60} min, ${itinerary.transfers} transfers)")
                itinerary.legs.forEach { leg ->
                    println("  ${leg.mode} ${leg.displayName ?: ""} ${leg.from.name} ${leg.startTime} -> ${leg.to.name} ${leg.endTime}")
                }
                assertTrue(itinerary.startTime >= departure - 1.hours, "departure before requested time")
                assertTrue(itinerary.transfers <= 2)
                assertTrue(itinerary.legs.any { it.mode in setOf(Mode.HIGHSPEED_RAIL, Mode.LONG_DISTANCE, Mode.REGIONAL_RAIL) })
                itinerary.legs.zipWithNext().forEach { (a, b) -> assertTrue(a.endTime <= b.startTime) }
            }

            // pagination with the returned cursor
            val cursor = plan.body.nextPageCursor
            val nextPage = routingClient.plan(fromPlace = from, toPlace = to, pageCursor = cursor, numItineraries = 3)
            assertIs<RoutingClient.PlanResponseSuccess>(nextPage)
            val nextItineraries = nextPage.body.itineraries
            assertTrue(nextItineraries.isNotEmpty(), "next page is empty")
            assertTrue(nextItineraries.first().startTime >= itineraries.first().startTime)

            // trip details of the first transit leg
            val transitLeg = itineraries.first().legs.first { it.tripId != null }
            val trip = timetableClient.trip(tripId = transitLeg.tripId!!)
            assertIs<TimetableClient.TripResponseSuccess>(trip)
            val tripLeg = trip.body.legs.single()
            assertEquals(transitLeg.tripId, tripLeg.tripId)
            println("trip ${tripLeg.displayName}: ${tripLeg.intermediateStops?.size} intermediate stops")
        }
}
