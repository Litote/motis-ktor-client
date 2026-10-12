import kotlinx.coroutines.test.runTest
import org.litote.motis.client.client.RoutingClient
import org.litote.motis.client.client.TimetableClient
import org.litote.motis.client.model.Mode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Instant

/**
 * Scenario plan -> next page -> trip details, against a mocked Transitous API.
 */
class PlanScenarioTest {
    private val transitous =
        MockTransitous { request ->
            when (request.url.encodedPath) {
                "/api/v6/plan" ->
                    respondJson(
                        if (request.url.parameters["pageCursor"] == null) {
                            TransitousFixtures.planFirstPage
                        } else {
                            TransitousFixtures.planNextPage
                        },
                    )

                "/api/v6/trip" -> respondJson(TransitousFixtures.trip)
                else -> error("unexpected request ${request.url}")
            }
        }
    private val routingClient = RoutingClient(transitous.configuration)
    private val timetableClient = TimetableClient(transitous.configuration)

    @Test
    fun planThenNextPageThenTripDetails() =
        runTest {
            val departure = Instant.parse("2026-10-10T08:00:00Z")

            val plan =
                routingClient.plan(
                    fromPlace = TransitousFixtures.PARIS_GARE_DE_LYON,
                    toPlace = TransitousFixtures.LYON_PART_DIEU,
                    time = departure,
                    transitModes = listOf(Mode.HIGHSPEED_RAIL, Mode.LONG_DISTANCE),
                    maxTransfers = 2,
                    numItineraries = 1,
                )

            val planParameters = transitous.lastRequest.url.parameters
            assertEquals(TransitousFixtures.PARIS_GARE_DE_LYON, planParameters["fromPlace"])
            assertEquals(TransitousFixtures.LYON_PART_DIEU, planParameters["toPlace"])
            assertEquals("2026-10-10T08:00:00Z", planParameters["time"])
            assertEquals("HIGHSPEED_RAIL,LONG_DISTANCE", planParameters["transitModes"])
            assertEquals("2", planParameters["maxTransfers"])
            assertEquals("1", planParameters["numItineraries"])

            assertIs<RoutingClient.PlanResponseSuccess>(plan)
            val itinerary = plan.body.itineraries.single()
            assertEquals(departure, itinerary.startTime)
            assertEquals(Instant.parse("2026-10-10T10:00:00Z"), itinerary.endTime)
            assertEquals(0L, itinerary.transfers)
            val leg = itinerary.legs.single()
            assertEquals(Mode.HIGHSPEED_RAIL, leg.mode)
            assertEquals("Paris Gare de Lyon", leg.from.name)
            assertEquals("Lyon Part-Dieu", leg.to.name)
            assertEquals(TransitousFixtures.TRIP_ID, leg.tripId)

            // pagination with the returned cursor
            val nextPage =
                routingClient.plan(
                    fromPlace = TransitousFixtures.PARIS_GARE_DE_LYON,
                    toPlace = TransitousFixtures.LYON_PART_DIEU,
                    pageCursor = plan.body.nextPageCursor,
                )
            assertEquals("NEXT", transitous.lastRequest.url.parameters["pageCursor"])
            assertIs<RoutingClient.PlanResponseSuccess>(nextPage)
            // a mode added to the API later does not break deserialization
            assertEquals(Mode.UNKNOWN_, nextPage.body.itineraries.single().legs.single().mode)

            // trip details of the transit leg
            val trip = timetableClient.trip(tripId = leg.tripId!!)
            assertEquals("/api/v6/trip", transitous.lastRequest.url.encodedPath)
            assertEquals(TransitousFixtures.TRIP_ID, transitous.lastRequest.url.parameters["tripId"])
            assertIs<TimetableClient.TripResponseSuccess>(trip)
            assertEquals("TGV INOUI 6611", trip.body.legs.single().displayName)

            assertEquals(3, transitous.requests.size)
        }
}
