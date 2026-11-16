package org.litote.motis.client.client

import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import org.litote.motis.client.client.ClientConfiguration.Companion.defaultClientConfiguration
import org.litote.motis.client.model.Error
import org.litote.motis.client.model.Itinerary
import org.litote.motis.client.model.Mode
import org.litote.motis.client.model.PedestrianProfile
import org.litote.motis.client.model.Place
import org.litote.motis.client.model.RefreshItineraryPostBody
import org.litote.motis.client.model.StopTime

public interface TimetableClient {
  /**
   * Get a trip as itinerary
   */
  public suspend fun trip(
    tripId: String,
    withScheduledSkippedStops: Boolean? = false,
    detailedLegs: Boolean? = true,
    joinInterlinedLegs: Boolean? = true,
    language: List<String>? = null,
  ): TripResponse

  /**
   * Reconstruct an itinerary from an itinerary ID.
   */
  public suspend fun refreshItinerary(
    itineraryId: String,
    requireDisplayNameMatch: Boolean? = true,
    joinInterlinedLegs: Boolean? = true,
    detailedTransfers: Boolean? = null,
    detailedLegs: Boolean? = true,
    withFares: Boolean? = false,
    withScheduledSkippedStops: Boolean? = false,
    numLegAlternatives: Long? = 0,
    transitModes: List<Mode>? = null,
    pedestrianProfile: PedestrianProfile? = null,
    useRoutedTransfers: Boolean? = false,
    requireBikeTransport: Boolean? = false,
    requireCarTransport: Boolean? = false,
    language: List<String>? = null,
  ): RefreshItineraryResponse

  /**
   * Reconstruct an itinerary from a protobuf-JSON itinerary identifier.
   */
  public suspend fun refreshItineraryPost(request: RefreshItineraryPostBody): RefreshItineraryPostResponse

  /**
   * Get the next N departures or arrivals of a stop sorted by time
   */
  public suspend fun stoptimes(
    stopId: String? = null,
    center: String? = null,
    time: Instant? = null,
    arriveBy: Boolean? = false,
    direction: Direction? = null,
    window: Long? = null,
    mode: List<Mode>? = null,
    n: Long? = null,
    radius: Long? = null,
    exactRadius: Boolean? = false,
    fetchStops: Boolean? = null,
    pageCursor: String? = null,
    withScheduledSkippedStops: Boolean? = false,
    language: List<String>? = null,
    withAlerts: Boolean? = true,
  ): StoptimesResponse

  @Serializable
  public enum class Direction {
    @SerialName("EARLIER")
    EARLIER,
    @SerialName("LATER")
    LATER,
    ;

    public fun serialName(): String = Direction.serializer().descriptor.getElementName(this.ordinal)
  }

  @Serializable
  public sealed class TripResponse : OperationResponse<Itinerary> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class TripResponseSuccess(
    override val body: Itinerary,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TripResponse(),
      OperationResponseSuccess<Itinerary>

  @Serializable
  public data class TripResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TripResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class TripResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TripResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public sealed class RefreshItineraryResponse : OperationResponse<Itinerary> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class RefreshItineraryResponseSuccess(
    override val body: Itinerary,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RefreshItineraryResponse(),
      OperationResponseSuccess<Itinerary>

  @Serializable
  public data class RefreshItineraryResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RefreshItineraryResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class RefreshItineraryResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RefreshItineraryResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public sealed class RefreshItineraryPostResponse : OperationResponse<Itinerary> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class RefreshItineraryPostResponseSuccess(
    override val body: Itinerary,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RefreshItineraryPostResponse(),
      OperationResponseSuccess<Itinerary>

  @Serializable
  public data class RefreshItineraryPostResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RefreshItineraryPostResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class RefreshItineraryPostResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RefreshItineraryPostResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public data class StoptimesResponseBody(
    public val nextPageCursor: String,
    public val place: Place,
    public val previousPageCursor: String,
    public val stopTimes: List<StopTime>,
  )

  @Serializable
  public sealed class StoptimesResponse : OperationResponse<StoptimesResponseBody> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class StoptimesResponseSuccess(
    override val body: StoptimesResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : StoptimesResponse(),
      OperationResponseSuccess<StoptimesResponseBody>

  @Serializable
  public data class StoptimesResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : StoptimesResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class StoptimesResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : StoptimesResponse(),
      OperationResponseUnknownFailure
}

public fun TimetableClient(configuration: ClientConfiguration = defaultClientConfiguration): TimetableClient = DefaultTimetableClient(configuration)

public class DefaultTimetableClient(
  private val configuration: ClientConfiguration = defaultClientConfiguration,
) : TimetableClient {
  override suspend fun trip(
    tripId: String,
    withScheduledSkippedStops: Boolean?,
    detailedLegs: Boolean?,
    joinInterlinedLegs: Boolean?,
    language: List<String>?,
  ): TimetableClient.TripResponse {
    try {
      val response = configuration.client.`get`("api/v6/trip") {
        url {
          parameters.append("tripId", tripId)
          if (withScheduledSkippedStops != null) {
            parameters.append("withScheduledSkippedStops", withScheduledSkippedStops.toString())
          }
          if (detailedLegs != null) {
            parameters.append("detailedLegs", detailedLegs.toString())
          }
          if (joinInterlinedLegs != null) {
            parameters.append("joinInterlinedLegs", joinInterlinedLegs.toString())
          }
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
        }
      }
      return when (response.status.value) {
        200 -> TimetableClient.TripResponseSuccess(response.body<Itinerary>(), response.headers)
        400, 404, 422, 500 -> TimetableClient.TripResponseFailure(response.body<Error>(), response.headers)
        else -> TimetableClient.TripResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return TimetableClient.TripResponseUnknownFailure(500)
    }
  }

  override suspend fun refreshItinerary(
    itineraryId: String,
    requireDisplayNameMatch: Boolean?,
    joinInterlinedLegs: Boolean?,
    detailedTransfers: Boolean?,
    detailedLegs: Boolean?,
    withFares: Boolean?,
    withScheduledSkippedStops: Boolean?,
    numLegAlternatives: Long?,
    transitModes: List<Mode>?,
    pedestrianProfile: PedestrianProfile?,
    useRoutedTransfers: Boolean?,
    requireBikeTransport: Boolean?,
    requireCarTransport: Boolean?,
    language: List<String>?,
  ): TimetableClient.RefreshItineraryResponse {
    try {
      val response = configuration.client.`get`("api/v6/refresh-itinerary") {
        url {
          parameters.append("itineraryId", itineraryId)
          if (requireDisplayNameMatch != null) {
            parameters.append("requireDisplayNameMatch", requireDisplayNameMatch.toString())
          }
          if (joinInterlinedLegs != null) {
            parameters.append("joinInterlinedLegs", joinInterlinedLegs.toString())
          }
          if (detailedTransfers != null) {
            parameters.append("detailedTransfers", detailedTransfers.toString())
          }
          if (detailedLegs != null) {
            parameters.append("detailedLegs", detailedLegs.toString())
          }
          if (withFares != null) {
            parameters.append("withFares", withFares.toString())
          }
          if (withScheduledSkippedStops != null) {
            parameters.append("withScheduledSkippedStops", withScheduledSkippedStops.toString())
          }
          if (numLegAlternatives != null) {
            parameters.append("numLegAlternatives", numLegAlternatives.toString())
          }
          if (transitModes != null) {
            parameters.append("transitModes", transitModes.joinToString(",") { it.serialName() })
          }
          if (pedestrianProfile != null) {
            parameters.append("pedestrianProfile", pedestrianProfile.serialName())
          }
          if (useRoutedTransfers != null) {
            parameters.append("useRoutedTransfers", useRoutedTransfers.toString())
          }
          if (requireBikeTransport != null) {
            parameters.append("requireBikeTransport", requireBikeTransport.toString())
          }
          if (requireCarTransport != null) {
            parameters.append("requireCarTransport", requireCarTransport.toString())
          }
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
        }
      }
      return when (response.status.value) {
        200 -> TimetableClient.RefreshItineraryResponseSuccess(response.body<Itinerary>(), response.headers)
        400, 500 -> TimetableClient.RefreshItineraryResponseFailure(response.body<Error>(), response.headers)
        else -> TimetableClient.RefreshItineraryResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return TimetableClient.RefreshItineraryResponseUnknownFailure(500)
    }
  }

  override suspend fun refreshItineraryPost(request: RefreshItineraryPostBody): TimetableClient.RefreshItineraryPostResponse {
    try {
      val response = configuration.client.post("api/v6/refresh-itinerary") {
        setBody(request)
        contentType(ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> TimetableClient.RefreshItineraryPostResponseSuccess(response.body<Itinerary>(), response.headers)
        400, 500 -> TimetableClient.RefreshItineraryPostResponseFailure(response.body<Error>(), response.headers)
        else -> TimetableClient.RefreshItineraryPostResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return TimetableClient.RefreshItineraryPostResponseUnknownFailure(500)
    }
  }

  override suspend fun stoptimes(
    stopId: String?,
    center: String?,
    time: Instant?,
    arriveBy: Boolean?,
    direction: TimetableClient.Direction?,
    window: Long?,
    mode: List<Mode>?,
    n: Long?,
    radius: Long?,
    exactRadius: Boolean?,
    fetchStops: Boolean?,
    pageCursor: String?,
    withScheduledSkippedStops: Boolean?,
    language: List<String>?,
    withAlerts: Boolean?,
  ): TimetableClient.StoptimesResponse {
    try {
      val response = configuration.client.`get`("api/v6/stoptimes") {
        url {
          if (stopId != null) {
            parameters.append("stopId", stopId)
          }
          if (center != null) {
            parameters.append("center", center)
          }
          if (time != null) {
            parameters.append("time", time.let { kotlin.time.Instant.fromEpochSeconds(it.epochSeconds, it.nanosecondsOfSecond / 1_000_000 * 1_000_000) }.toString())
          }
          if (arriveBy != null) {
            parameters.append("arriveBy", arriveBy.toString())
          }
          if (direction != null) {
            parameters.append("direction", direction.serialName())
          }
          if (window != null) {
            parameters.append("window", window.toString())
          }
          if (mode != null) {
            parameters.append("mode", mode.joinToString(",") { it.serialName() })
          }
          if (n != null) {
            parameters.append("n", n.toString())
          }
          if (radius != null) {
            parameters.append("radius", radius.toString())
          }
          if (exactRadius != null) {
            parameters.append("exactRadius", exactRadius.toString())
          }
          if (fetchStops != null) {
            parameters.append("fetchStops", fetchStops.toString())
          }
          if (pageCursor != null) {
            parameters.append("pageCursor", pageCursor)
          }
          if (withScheduledSkippedStops != null) {
            parameters.append("withScheduledSkippedStops", withScheduledSkippedStops.toString())
          }
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
          if (withAlerts != null) {
            parameters.append("withAlerts", withAlerts.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> TimetableClient.StoptimesResponseSuccess(response.body<TimetableClient.StoptimesResponseBody>(), response.headers)
        400, 404, 422, 500 -> TimetableClient.StoptimesResponseFailure(response.body<Error>(), response.headers)
        else -> TimetableClient.StoptimesResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return TimetableClient.StoptimesResponseUnknownFailure(500)
    }
  }
}
