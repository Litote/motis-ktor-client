/**
 * Responses of the Transitous API, trimmed from real responses.
 */
object TransitousFixtures {
    const val PARIS_GARE_DE_LYON = "fr-idfm_IDFM:73626"
    const val LYON_PART_DIEU = "fr-sncf_FR:87723197"
    const val TRIP_ID = "20261010_10:00_fr-sncf_FR:SN6611"

    val geocodeParis =
        """
        [
          {
            "type": "STOP",
            "tokens": [[0.0, 5.0], [6.0, 4.0]],
            "name": "Gare de Lyon",
            "id": "$PARIS_GARE_DE_LYON",
            "lat": 48.84457,
            "lon": 2.37514,
            "level": 1.0,
            "country": "FR",
            "tz": "Europe/Paris",
            "areas": [
              { "name": "France", "adminLevel": 2.0, "matched": false, "unique": false, "default": false },
              { "name": "Paris", "adminLevel": 6.0, "matched": true, "unique": true, "default": true }
            ],
            "score": 12.5,
            "modes": ["HIGHSPEED_RAIL", "SUBWAY", "BUS"]
          }
        ]
        """.trimIndent()

    val badRequest =
        """
        { "error": "invalid request" }
        """.trimIndent()

    private fun place(
        name: String,
        stopId: String,
        time: String,
    ): String =
        """
        {
          "name": "$name",
          "stopId": "$stopId",
          "lat": 45.76,
          "lon": 4.86,
          "level": 0.0,
          "tz": "Europe/Paris",
          "arrival": "$time",
          "departure": "$time",
          "scheduledArrival": "$time",
          "scheduledDeparture": "$time",
          "vertexType": "TRANSIT"
        }
        """.trimIndent()

    private fun leg(
        mode: String,
        start: String,
        end: String,
        tripId: String?,
    ): String =
        """
        {
          "mode": "$mode",
          "from": ${place("Paris Gare de Lyon", PARIS_GARE_DE_LYON, start)},
          "to": ${place("Lyon Part-Dieu", LYON_PART_DIEU, end)},
          "duration": 7200,
          "startTime": "$start",
          "endTime": "$end",
          "scheduledStartTime": "$start",
          "scheduledEndTime": "$end",
          "realTime": false,
          "scheduled": true,
          ${if (tripId != null) "\"tripId\": \"$tripId\", \"displayName\": \"TGV INOUI 6611\", \"headsign\": \"Lyon Part-Dieu\"," else ""}
          "intermediateStops": [],
          "legGeometry": { "points": "_p~iF~ps|U_ulLnnqC", "precision": 6, "length": 2 }
        }
        """.trimIndent()

    private fun itinerary(
        start: String,
        end: String,
        legs: List<String>,
    ): String =
        """
        {
          "duration": 7200,
          "startTime": "$start",
          "endTime": "$end",
          "transfers": 0,
          "id": "itinerary-$start",
          "legs": [${legs.joinToString(",")}]
        }
        """.trimIndent()

    private fun plan(
        itineraries: List<String>,
        nextPageCursor: String,
    ): String =
        """
        {
          "requestParameters": {},
          "debugOutput": { "execute_time": 42 },
          "from": ${place("Paris Gare de Lyon", PARIS_GARE_DE_LYON, "2026-10-10T08:00:00Z")},
          "to": ${place("Lyon Part-Dieu", LYON_PART_DIEU, "2026-10-10T10:00:00Z")},
          "direct": [],
          "itineraries": [${itineraries.joinToString(",")}],
          "previousPageCursor": "PREVIOUS",
          "nextPageCursor": "$nextPageCursor"
        }
        """.trimIndent()

    val planFirstPage =
        plan(
            listOf(
                itinerary(
                    "2026-10-10T08:00:00Z",
                    "2026-10-10T10:00:00Z",
                    listOf(leg("HIGHSPEED_RAIL", "2026-10-10T08:00:00Z", "2026-10-10T10:00:00Z", TRIP_ID)),
                ),
            ),
            nextPageCursor = "NEXT",
        )

    val planNextPage =
        plan(
            listOf(
                itinerary(
                    "2026-10-10T09:00:00Z",
                    "2026-10-10T11:00:00Z",
                    // mode unknown to this client version: deserialized as Mode.UNKNOWN_
                    listOf(leg("HOVERCRAFT", "2026-10-10T09:00:00Z", "2026-10-10T11:00:00Z", null)),
                ),
            ),
            nextPageCursor = "NEXT2",
        )

    val trip =
        itinerary(
            "2026-10-10T08:00:00Z",
            "2026-10-10T10:00:00Z",
            listOf(leg("HIGHSPEED_RAIL", "2026-10-10T08:00:00Z", "2026-10-10T10:00:00Z", TRIP_ID)),
        )
}
