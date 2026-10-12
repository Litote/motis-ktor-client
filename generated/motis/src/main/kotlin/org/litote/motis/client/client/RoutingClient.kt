package org.litote.motis.client.client

import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.contentType
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import org.litote.motis.client.client.ClientConfiguration.Companion.defaultClientConfiguration
import org.litote.motis.client.model.Duration
import org.litote.motis.client.model.ElevationCosts
import org.litote.motis.client.model.Error
import org.litote.motis.client.model.Itinerary
import org.litote.motis.client.model.Mode
import org.litote.motis.client.model.OneToManyIntermodalParams
import org.litote.motis.client.model.OneToManyIntermodalResponse
import org.litote.motis.client.model.OneToManyParams
import org.litote.motis.client.model.PedestrianProfile
import org.litote.motis.client.model.Place
import org.litote.motis.client.model.Reachable
import org.litote.motis.client.model.RentalFormFactor
import org.litote.motis.client.model.RentalPropulsionType

public interface RoutingClient {
  /**
   * Computes optimal connections from one place to another.
   */
  public suspend fun plan(
    fromPlace: String,
    toPlace: String,
    radius: Double? = null,
    via: List<String>? = null,
    viaMinimumStay: List<Long>? = null,
    time: Instant? = null,
    maxTransfers: Long? = null,
    maxTravelTime: Long? = null,
    minTransferTime: Long? = 0,
    additionalTransferTime: Long? = 0,
    transferTimeFactor: Double? = 1.0,
    maxMatchingDistance: Double? = 25.0,
    pedestrianProfile: PedestrianProfile? = null,
    pedestrianSpeed: Double? = null,
    cyclingSpeed: Double? = null,
    elevationCosts: ElevationCosts? = null,
    useRoutedTransfers: Boolean? = false,
    detailedTransfers: Boolean? = null,
    detailedLegs: Boolean? = true,
    joinInterlinedLegs: Boolean? = true,
    transitModes: List<Mode>? = null,
    directModes: List<Mode>? = null,
    preTransitModes: List<Mode>? = null,
    postTransitModes: List<Mode>? = null,
    directRentalFormFactors: List<RentalFormFactor>? = null,
    preTransitRentalFormFactors: List<RentalFormFactor>? = null,
    postTransitRentalFormFactors: List<RentalFormFactor>? = null,
    directRentalPropulsionTypes: List<RentalPropulsionType>? = null,
    preTransitRentalPropulsionTypes: List<RentalPropulsionType>? = null,
    postTransitRentalPropulsionTypes: List<RentalPropulsionType>? = null,
    directRentalProviders: List<String>? = null,
    directRentalProviderGroups: List<String>? = null,
    preTransitRentalProviders: List<String>? = null,
    preTransitRentalProviderGroups: List<String>? = null,
    postTransitRentalProviders: List<String>? = null,
    postTransitRentalProviderGroups: List<String>? = null,
    ignoreDirectRentalReturnConstraints: Boolean? = false,
    ignorePreTransitRentalReturnConstraints: Boolean? = false,
    ignorePostTransitRentalReturnConstraints: Boolean? = false,
    numItineraries: Long? = 5,
    maxItineraries: Long? = null,
    pageCursor: String? = null,
    timetableView: Boolean? = true,
    arriveBy: Boolean? = false,
    searchWindow: Long? = 900,
    requireBikeTransport: Boolean? = false,
    requireCarTransport: Boolean? = false,
    maxPreTransitTime: Long? = 900,
    maxPostTransitTime: Long? = 900,
    maxDirectTime: Long? = null,
    fastestDirectFactor: Double? = 1.0,
    timeout: Long? = null,
    passengers: Long? = null,
    luggage: Long? = null,
    slowDirect: Boolean? = false,
    fastestSlowDirectFactor: Double? = 3.0,
    withFares: Boolean? = false,
    numLegAlternatives: Long? = 0,
    withScheduledSkippedStops: Boolean? = false,
    language: List<String>? = null,
    algorithm: Algorithm? = Algorithm.PONG,
  ): PlanResponse

  /**
   * Street routing from one to many places or many to one.
   * The order in the response array corresponds to the order of coordinates of the \`many\` parameter in the query.
   *
   */
  public suspend fun oneToMany(
    one: String,
    many: List<String>,
    mode: Mode,
    max: Double,
    maxMatchingDistance: Double,
    elevationCosts: ElevationCosts? = null,
    arriveBy: Boolean,
    withDistance: Boolean? = false,
  ): OneToManyResponse

  /**
   * Street routing from one to many places or many to one.
   * The order in the response array corresponds to the order of coordinates of the \`many\` parameter in the request body.
   *
   */
  public suspend fun oneToManyPost(request: OneToManyParams): OneToManyPostResponse

  /**
   * One to many routing
   * Computes the minimal duration from one place to many or vice versa.
   * The order in the response array corresponds to the order of coordinates of the \`many\` parameter in the query.
   *
   */
  public suspend fun oneToManyIntermodal(
    one: String,
    many: List<String>,
    time: Instant? = null,
    maxTravelTime: Long? = null,
    maxMatchingDistance: Double? = 25.0,
    arriveBy: Boolean? = false,
    maxTransfers: Long? = null,
    minTransferTime: Long? = 0,
    additionalTransferTime: Long? = 0,
    transferTimeFactor: Double? = 1.0,
    useRoutedTransfers: Boolean? = false,
    pedestrianProfile: PedestrianProfile? = null,
    pedestrianSpeed: Double? = null,
    cyclingSpeed: Double? = null,
    elevationCosts: ElevationCosts? = null,
    transitModes: List<Mode>? = null,
    preTransitModes: List<Mode>? = null,
    postTransitModes: List<Mode>? = null,
    directMode: Mode? = null,
    maxPreTransitTime: Long? = 900,
    maxPostTransitTime: Long? = 900,
    maxDirectTime: Long? = null,
    withDistance: Boolean? = false,
    requireBikeTransport: Boolean? = false,
    requireCarTransport: Boolean? = false,
  ): OneToManyIntermodalResponse

  /**
   * One to many routing
   * Computes the minimal duration from one place to many or vice versa.
   * The order in the response array corresponds to the order of coordinates of the \`many\` parameter in the request body.
   *
   */
  public suspend fun oneToManyIntermodalPost(request: OneToManyIntermodalParams): OneToManyIntermodalPostResponse

  /**
   * Computes all reachable locations from a given stop within a set duration.
   * Each result entry will contain the fastest travel duration and the number of connections used.
   *
   */
  public suspend fun oneToAll(
    one: String,
    time: Instant? = null,
    maxTravelTime: Long,
    arriveBy: Boolean? = false,
    maxTransfers: Long? = null,
    minTransferTime: Long? = 0,
    additionalTransferTime: Long? = 0,
    transferTimeFactor: Double? = 1.0,
    maxMatchingDistance: Double? = 25.0,
    useRoutedTransfers: Boolean? = false,
    pedestrianProfile: PedestrianProfile? = null,
    pedestrianSpeed: Double? = null,
    cyclingSpeed: Double? = null,
    elevationCosts: ElevationCosts? = null,
    transitModes: List<Mode>? = null,
    preTransitModes: List<Mode>? = null,
    postTransitModes: List<Mode>? = null,
    requireBikeTransport: Boolean? = false,
    requireCarTransport: Boolean? = false,
    maxPreTransitTime: Long? = 900,
    maxPostTransitTime: Long? = 900,
  ): OneToAllResponse

  @Serializable
  public enum class Algorithm {
    @SerialName("RAPTOR")
    RAPTOR,
    @SerialName("PONG")
    PONG,
    @SerialName("TB")
    TB,
    ;

    public fun serialName(): String = Algorithm.serializer().descriptor.getElementName(this.ordinal)
  }

  @Serializable
  public data class PlanResponseBody(
    public val debugOutput: Map<String, Long>,
    public val direct: List<Itinerary>,
    public val from: Place,
    public val itineraries: List<Itinerary>,
    public val nextPageCursor: String,
    public val previousPageCursor: String,
    public val requestParameters: Map<String, String>,
    public val to: Place,
  )

  @Serializable
  public sealed class PlanResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class PlanResponseSuccess(
    public val body: PlanResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : PlanResponse()

  @Serializable
  public data class PlanResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : PlanResponse()

  @Serializable
  public data class PlanResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : PlanResponse()

  @Serializable
  public sealed class OneToManyResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class OneToManyResponseSuccess(
    public val body: List<Duration>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyResponse()

  @Serializable
  public data class OneToManyResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyResponse()

  @Serializable
  public data class OneToManyResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyResponse()

  @Serializable
  public sealed class OneToManyPostResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class OneToManyPostResponseSuccess(
    public val body: List<Duration>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyPostResponse()

  @Serializable
  public data class OneToManyPostResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyPostResponse()

  @Serializable
  public data class OneToManyPostResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyPostResponse()

  @Serializable
  public sealed class OneToManyIntermodalResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class OneToManyIntermodalResponseSuccess(
    public val body: org.litote.motis.client.model.OneToManyIntermodalResponse,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyIntermodalResponse()

  @Serializable
  public data class OneToManyIntermodalResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyIntermodalResponse()

  @Serializable
  public data class OneToManyIntermodalResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyIntermodalResponse()

  @Serializable
  public sealed class OneToManyIntermodalPostResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class OneToManyIntermodalPostResponseSuccess(
    public val body: org.litote.motis.client.model.OneToManyIntermodalResponse,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyIntermodalPostResponse()

  @Serializable
  public data class OneToManyIntermodalPostResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyIntermodalPostResponse()

  @Serializable
  public data class OneToManyIntermodalPostResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToManyIntermodalPostResponse()

  @Serializable
  public sealed class OneToAllResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class OneToAllResponseSuccess(
    public val body: Reachable,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToAllResponse()

  @Serializable
  public data class OneToAllResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToAllResponse()

  @Serializable
  public data class OneToAllResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : OneToAllResponse()
}

public fun RoutingClient(configuration: ClientConfiguration = defaultClientConfiguration): RoutingClient = DefaultRoutingClient(configuration)

public class DefaultRoutingClient(
  private val configuration: ClientConfiguration = defaultClientConfiguration,
) : RoutingClient {
  override suspend fun plan(
    fromPlace: String,
    toPlace: String,
    radius: Double?,
    via: List<String>?,
    viaMinimumStay: List<Long>?,
    time: Instant?,
    maxTransfers: Long?,
    maxTravelTime: Long?,
    minTransferTime: Long?,
    additionalTransferTime: Long?,
    transferTimeFactor: Double?,
    maxMatchingDistance: Double?,
    pedestrianProfile: PedestrianProfile?,
    pedestrianSpeed: Double?,
    cyclingSpeed: Double?,
    elevationCosts: ElevationCosts?,
    useRoutedTransfers: Boolean?,
    detailedTransfers: Boolean?,
    detailedLegs: Boolean?,
    joinInterlinedLegs: Boolean?,
    transitModes: List<Mode>?,
    directModes: List<Mode>?,
    preTransitModes: List<Mode>?,
    postTransitModes: List<Mode>?,
    directRentalFormFactors: List<RentalFormFactor>?,
    preTransitRentalFormFactors: List<RentalFormFactor>?,
    postTransitRentalFormFactors: List<RentalFormFactor>?,
    directRentalPropulsionTypes: List<RentalPropulsionType>?,
    preTransitRentalPropulsionTypes: List<RentalPropulsionType>?,
    postTransitRentalPropulsionTypes: List<RentalPropulsionType>?,
    directRentalProviders: List<String>?,
    directRentalProviderGroups: List<String>?,
    preTransitRentalProviders: List<String>?,
    preTransitRentalProviderGroups: List<String>?,
    postTransitRentalProviders: List<String>?,
    postTransitRentalProviderGroups: List<String>?,
    ignoreDirectRentalReturnConstraints: Boolean?,
    ignorePreTransitRentalReturnConstraints: Boolean?,
    ignorePostTransitRentalReturnConstraints: Boolean?,
    numItineraries: Long?,
    maxItineraries: Long?,
    pageCursor: String?,
    timetableView: Boolean?,
    arriveBy: Boolean?,
    searchWindow: Long?,
    requireBikeTransport: Boolean?,
    requireCarTransport: Boolean?,
    maxPreTransitTime: Long?,
    maxPostTransitTime: Long?,
    maxDirectTime: Long?,
    fastestDirectFactor: Double?,
    timeout: Long?,
    passengers: Long?,
    luggage: Long?,
    slowDirect: Boolean?,
    fastestSlowDirectFactor: Double?,
    withFares: Boolean?,
    numLegAlternatives: Long?,
    withScheduledSkippedStops: Boolean?,
    language: List<String>?,
    algorithm: RoutingClient.Algorithm?,
  ): RoutingClient.PlanResponse {
    try {
      val response = configuration.client.`get`("api/v6/plan") {
        url {
          parameters.append("fromPlace", fromPlace)
          parameters.append("toPlace", toPlace)
          if (radius != null) {
            parameters.append("radius", radius.toString())
          }
          if (via != null) {
            parameters.append("via", via.joinToString(","))
          }
          if (viaMinimumStay != null) {
            parameters.append("viaMinimumStay", viaMinimumStay.joinToString(","))
          }
          if (time != null) {
            parameters.append("time", time.let { kotlin.time.Instant.fromEpochSeconds(it.epochSeconds, it.nanosecondsOfSecond / 1_000_000 * 1_000_000) }.toString())
          }
          if (maxTransfers != null) {
            parameters.append("maxTransfers", maxTransfers.toString())
          }
          if (maxTravelTime != null) {
            parameters.append("maxTravelTime", maxTravelTime.toString())
          }
          if (minTransferTime != null) {
            parameters.append("minTransferTime", minTransferTime.toString())
          }
          if (additionalTransferTime != null) {
            parameters.append("additionalTransferTime", additionalTransferTime.toString())
          }
          if (transferTimeFactor != null) {
            parameters.append("transferTimeFactor", transferTimeFactor.toString())
          }
          if (maxMatchingDistance != null) {
            parameters.append("maxMatchingDistance", maxMatchingDistance.toString())
          }
          if (pedestrianProfile != null) {
            parameters.append("pedestrianProfile", pedestrianProfile.serialName())
          }
          if (pedestrianSpeed != null) {
            parameters.append("pedestrianSpeed", pedestrianSpeed.toString())
          }
          if (cyclingSpeed != null) {
            parameters.append("cyclingSpeed", cyclingSpeed.toString())
          }
          if (elevationCosts != null) {
            parameters.append("elevationCosts", elevationCosts.serialName())
          }
          if (useRoutedTransfers != null) {
            parameters.append("useRoutedTransfers", useRoutedTransfers.toString())
          }
          if (detailedTransfers != null) {
            parameters.append("detailedTransfers", detailedTransfers.toString())
          }
          if (detailedLegs != null) {
            parameters.append("detailedLegs", detailedLegs.toString())
          }
          if (joinInterlinedLegs != null) {
            parameters.append("joinInterlinedLegs", joinInterlinedLegs.toString())
          }
          if (transitModes != null) {
            parameters.append("transitModes", transitModes.joinToString(",") { it.serialName() })
          }
          if (directModes != null) {
            parameters.append("directModes", directModes.joinToString(",") { it.serialName() })
          }
          if (preTransitModes != null) {
            parameters.append("preTransitModes", preTransitModes.joinToString(",") { it.serialName() })
          }
          if (postTransitModes != null) {
            parameters.append("postTransitModes", postTransitModes.joinToString(",") { it.serialName() })
          }
          if (directRentalFormFactors != null) {
            parameters.append("directRentalFormFactors", directRentalFormFactors.joinToString(",") { it.serialName() })
          }
          if (preTransitRentalFormFactors != null) {
            parameters.append("preTransitRentalFormFactors", preTransitRentalFormFactors.joinToString(",") { it.serialName() })
          }
          if (postTransitRentalFormFactors != null) {
            parameters.append("postTransitRentalFormFactors", postTransitRentalFormFactors.joinToString(",") { it.serialName() })
          }
          if (directRentalPropulsionTypes != null) {
            parameters.append("directRentalPropulsionTypes", directRentalPropulsionTypes.joinToString(",") { it.serialName() })
          }
          if (preTransitRentalPropulsionTypes != null) {
            parameters.append("preTransitRentalPropulsionTypes", preTransitRentalPropulsionTypes.joinToString(",") { it.serialName() })
          }
          if (postTransitRentalPropulsionTypes != null) {
            parameters.append("postTransitRentalPropulsionTypes", postTransitRentalPropulsionTypes.joinToString(",") { it.serialName() })
          }
          if (directRentalProviders != null) {
            parameters.append("directRentalProviders", directRentalProviders.joinToString(","))
          }
          if (directRentalProviderGroups != null) {
            parameters.append("directRentalProviderGroups", directRentalProviderGroups.joinToString(","))
          }
          if (preTransitRentalProviders != null) {
            parameters.append("preTransitRentalProviders", preTransitRentalProviders.joinToString(","))
          }
          if (preTransitRentalProviderGroups != null) {
            parameters.append("preTransitRentalProviderGroups", preTransitRentalProviderGroups.joinToString(","))
          }
          if (postTransitRentalProviders != null) {
            parameters.append("postTransitRentalProviders", postTransitRentalProviders.joinToString(","))
          }
          if (postTransitRentalProviderGroups != null) {
            parameters.append("postTransitRentalProviderGroups", postTransitRentalProviderGroups.joinToString(","))
          }
          if (ignoreDirectRentalReturnConstraints != null) {
            parameters.append("ignoreDirectRentalReturnConstraints", ignoreDirectRentalReturnConstraints.toString())
          }
          if (ignorePreTransitRentalReturnConstraints != null) {
            parameters.append("ignorePreTransitRentalReturnConstraints", ignorePreTransitRentalReturnConstraints.toString())
          }
          if (ignorePostTransitRentalReturnConstraints != null) {
            parameters.append("ignorePostTransitRentalReturnConstraints", ignorePostTransitRentalReturnConstraints.toString())
          }
          if (numItineraries != null) {
            parameters.append("numItineraries", numItineraries.toString())
          }
          if (maxItineraries != null) {
            parameters.append("maxItineraries", maxItineraries.toString())
          }
          if (pageCursor != null) {
            parameters.append("pageCursor", pageCursor)
          }
          if (timetableView != null) {
            parameters.append("timetableView", timetableView.toString())
          }
          if (arriveBy != null) {
            parameters.append("arriveBy", arriveBy.toString())
          }
          if (searchWindow != null) {
            parameters.append("searchWindow", searchWindow.toString())
          }
          if (requireBikeTransport != null) {
            parameters.append("requireBikeTransport", requireBikeTransport.toString())
          }
          if (requireCarTransport != null) {
            parameters.append("requireCarTransport", requireCarTransport.toString())
          }
          if (maxPreTransitTime != null) {
            parameters.append("maxPreTransitTime", maxPreTransitTime.toString())
          }
          if (maxPostTransitTime != null) {
            parameters.append("maxPostTransitTime", maxPostTransitTime.toString())
          }
          if (maxDirectTime != null) {
            parameters.append("maxDirectTime", maxDirectTime.toString())
          }
          if (fastestDirectFactor != null) {
            parameters.append("fastestDirectFactor", fastestDirectFactor.toString())
          }
          if (timeout != null) {
            parameters.append("timeout", timeout.toString())
          }
          if (passengers != null) {
            parameters.append("passengers", passengers.toString())
          }
          if (luggage != null) {
            parameters.append("luggage", luggage.toString())
          }
          if (slowDirect != null) {
            parameters.append("slowDirect", slowDirect.toString())
          }
          if (fastestSlowDirectFactor != null) {
            parameters.append("fastestSlowDirectFactor", fastestSlowDirectFactor.toString())
          }
          if (withFares != null) {
            parameters.append("withFares", withFares.toString())
          }
          if (numLegAlternatives != null) {
            parameters.append("numLegAlternatives", numLegAlternatives.toString())
          }
          if (withScheduledSkippedStops != null) {
            parameters.append("withScheduledSkippedStops", withScheduledSkippedStops.toString())
          }
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
          if (algorithm != null) {
            parameters.append("algorithm", algorithm.serialName())
          }
        }
      }
      return when (response.status.value) {
        200 -> RoutingClient.PlanResponseSuccess(response.body<RoutingClient.PlanResponseBody>(), response.headers)
        400, 404, 422, 500 -> RoutingClient.PlanResponseFailure(response.body<Error>(), response.headers)
        else -> RoutingClient.PlanResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return RoutingClient.PlanResponseUnknownFailure(500)
    }
  }

  override suspend fun oneToMany(
    one: String,
    many: List<String>,
    mode: Mode,
    max: Double,
    maxMatchingDistance: Double,
    elevationCosts: ElevationCosts?,
    arriveBy: Boolean,
    withDistance: Boolean?,
  ): RoutingClient.OneToManyResponse {
    try {
      val response = configuration.client.`get`("api/v1/one-to-many") {
        url {
          parameters.append("one", one)
          parameters.append("many", many.joinToString(","))
          parameters.append("mode", mode.serialName())
          parameters.append("max", max.toString())
          parameters.append("maxMatchingDistance", maxMatchingDistance.toString())
          if (elevationCosts != null) {
            parameters.append("elevationCosts", elevationCosts.serialName())
          }
          parameters.append("arriveBy", arriveBy.toString())
          if (withDistance != null) {
            parameters.append("withDistance", withDistance.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> RoutingClient.OneToManyResponseSuccess(response.body<List<Duration>>(), response.headers)
        400, 422, 500 -> RoutingClient.OneToManyResponseFailure(response.body<Error>(), response.headers)
        else -> RoutingClient.OneToManyResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return RoutingClient.OneToManyResponseUnknownFailure(500)
    }
  }

  override suspend fun oneToManyPost(request: OneToManyParams): RoutingClient.OneToManyPostResponse {
    try {
      val response = configuration.client.post("api/v1/one-to-many") {
        setBody(request)
        contentType(ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> RoutingClient.OneToManyPostResponseSuccess(response.body<List<Duration>>(), response.headers)
        400, 422, 500 -> RoutingClient.OneToManyPostResponseFailure(response.body<Error>(), response.headers)
        else -> RoutingClient.OneToManyPostResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return RoutingClient.OneToManyPostResponseUnknownFailure(500)
    }
  }

  override suspend fun oneToManyIntermodal(
    one: String,
    many: List<String>,
    time: Instant?,
    maxTravelTime: Long?,
    maxMatchingDistance: Double?,
    arriveBy: Boolean?,
    maxTransfers: Long?,
    minTransferTime: Long?,
    additionalTransferTime: Long?,
    transferTimeFactor: Double?,
    useRoutedTransfers: Boolean?,
    pedestrianProfile: PedestrianProfile?,
    pedestrianSpeed: Double?,
    cyclingSpeed: Double?,
    elevationCosts: ElevationCosts?,
    transitModes: List<Mode>?,
    preTransitModes: List<Mode>?,
    postTransitModes: List<Mode>?,
    directMode: Mode?,
    maxPreTransitTime: Long?,
    maxPostTransitTime: Long?,
    maxDirectTime: Long?,
    withDistance: Boolean?,
    requireBikeTransport: Boolean?,
    requireCarTransport: Boolean?,
  ): RoutingClient.OneToManyIntermodalResponse {
    try {
      val response = configuration.client.`get`("api/experimental/one-to-many-intermodal") {
        url {
          parameters.append("one", one)
          parameters.append("many", many.joinToString(","))
          if (time != null) {
            parameters.append("time", time.let { kotlin.time.Instant.fromEpochSeconds(it.epochSeconds, it.nanosecondsOfSecond / 1_000_000 * 1_000_000) }.toString())
          }
          if (maxTravelTime != null) {
            parameters.append("maxTravelTime", maxTravelTime.toString())
          }
          if (maxMatchingDistance != null) {
            parameters.append("maxMatchingDistance", maxMatchingDistance.toString())
          }
          if (arriveBy != null) {
            parameters.append("arriveBy", arriveBy.toString())
          }
          if (maxTransfers != null) {
            parameters.append("maxTransfers", maxTransfers.toString())
          }
          if (minTransferTime != null) {
            parameters.append("minTransferTime", minTransferTime.toString())
          }
          if (additionalTransferTime != null) {
            parameters.append("additionalTransferTime", additionalTransferTime.toString())
          }
          if (transferTimeFactor != null) {
            parameters.append("transferTimeFactor", transferTimeFactor.toString())
          }
          if (useRoutedTransfers != null) {
            parameters.append("useRoutedTransfers", useRoutedTransfers.toString())
          }
          if (pedestrianProfile != null) {
            parameters.append("pedestrianProfile", pedestrianProfile.serialName())
          }
          if (pedestrianSpeed != null) {
            parameters.append("pedestrianSpeed", pedestrianSpeed.toString())
          }
          if (cyclingSpeed != null) {
            parameters.append("cyclingSpeed", cyclingSpeed.toString())
          }
          if (elevationCosts != null) {
            parameters.append("elevationCosts", elevationCosts.serialName())
          }
          if (transitModes != null) {
            parameters.append("transitModes", transitModes.joinToString(",") { it.serialName() })
          }
          if (preTransitModes != null) {
            parameters.append("preTransitModes", preTransitModes.joinToString(",") { it.serialName() })
          }
          if (postTransitModes != null) {
            parameters.append("postTransitModes", postTransitModes.joinToString(",") { it.serialName() })
          }
          if (directMode != null) {
            parameters.append("directMode", directMode.serialName())
          }
          if (maxPreTransitTime != null) {
            parameters.append("maxPreTransitTime", maxPreTransitTime.toString())
          }
          if (maxPostTransitTime != null) {
            parameters.append("maxPostTransitTime", maxPostTransitTime.toString())
          }
          if (maxDirectTime != null) {
            parameters.append("maxDirectTime", maxDirectTime.toString())
          }
          if (withDistance != null) {
            parameters.append("withDistance", withDistance.toString())
          }
          if (requireBikeTransport != null) {
            parameters.append("requireBikeTransport", requireBikeTransport.toString())
          }
          if (requireCarTransport != null) {
            parameters.append("requireCarTransport", requireCarTransport.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> RoutingClient.OneToManyIntermodalResponseSuccess(response.body<OneToManyIntermodalResponse>(), response.headers)
        400, 422, 500 -> RoutingClient.OneToManyIntermodalResponseFailure(response.body<Error>(), response.headers)
        else -> RoutingClient.OneToManyIntermodalResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return RoutingClient.OneToManyIntermodalResponseUnknownFailure(500)
    }
  }

  override suspend fun oneToManyIntermodalPost(request: OneToManyIntermodalParams): RoutingClient.OneToManyIntermodalPostResponse {
    try {
      val response = configuration.client.post("api/experimental/one-to-many-intermodal") {
        setBody(request)
        contentType(ContentType.Application.Json)
      }
      return when (response.status.value) {
        200 -> RoutingClient.OneToManyIntermodalPostResponseSuccess(response.body<OneToManyIntermodalResponse>(), response.headers)
        400, 422, 500 -> RoutingClient.OneToManyIntermodalPostResponseFailure(response.body<Error>(), response.headers)
        else -> RoutingClient.OneToManyIntermodalPostResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return RoutingClient.OneToManyIntermodalPostResponseUnknownFailure(500)
    }
  }

  override suspend fun oneToAll(
    one: String,
    time: Instant?,
    maxTravelTime: Long,
    arriveBy: Boolean?,
    maxTransfers: Long?,
    minTransferTime: Long?,
    additionalTransferTime: Long?,
    transferTimeFactor: Double?,
    maxMatchingDistance: Double?,
    useRoutedTransfers: Boolean?,
    pedestrianProfile: PedestrianProfile?,
    pedestrianSpeed: Double?,
    cyclingSpeed: Double?,
    elevationCosts: ElevationCosts?,
    transitModes: List<Mode>?,
    preTransitModes: List<Mode>?,
    postTransitModes: List<Mode>?,
    requireBikeTransport: Boolean?,
    requireCarTransport: Boolean?,
    maxPreTransitTime: Long?,
    maxPostTransitTime: Long?,
  ): RoutingClient.OneToAllResponse {
    try {
      val response = configuration.client.`get`("api/v6/one-to-all") {
        url {
          parameters.append("one", one)
          if (time != null) {
            parameters.append("time", time.let { kotlin.time.Instant.fromEpochSeconds(it.epochSeconds, it.nanosecondsOfSecond / 1_000_000 * 1_000_000) }.toString())
          }
          parameters.append("maxTravelTime", maxTravelTime.toString())
          if (arriveBy != null) {
            parameters.append("arriveBy", arriveBy.toString())
          }
          if (maxTransfers != null) {
            parameters.append("maxTransfers", maxTransfers.toString())
          }
          if (minTransferTime != null) {
            parameters.append("minTransferTime", minTransferTime.toString())
          }
          if (additionalTransferTime != null) {
            parameters.append("additionalTransferTime", additionalTransferTime.toString())
          }
          if (transferTimeFactor != null) {
            parameters.append("transferTimeFactor", transferTimeFactor.toString())
          }
          if (maxMatchingDistance != null) {
            parameters.append("maxMatchingDistance", maxMatchingDistance.toString())
          }
          if (useRoutedTransfers != null) {
            parameters.append("useRoutedTransfers", useRoutedTransfers.toString())
          }
          if (pedestrianProfile != null) {
            parameters.append("pedestrianProfile", pedestrianProfile.serialName())
          }
          if (pedestrianSpeed != null) {
            parameters.append("pedestrianSpeed", pedestrianSpeed.toString())
          }
          if (cyclingSpeed != null) {
            parameters.append("cyclingSpeed", cyclingSpeed.toString())
          }
          if (elevationCosts != null) {
            parameters.append("elevationCosts", elevationCosts.serialName())
          }
          if (transitModes != null) {
            parameters.append("transitModes", transitModes.joinToString(",") { it.serialName() })
          }
          if (preTransitModes != null) {
            parameters.append("preTransitModes", preTransitModes.joinToString(",") { it.serialName() })
          }
          if (postTransitModes != null) {
            parameters.append("postTransitModes", postTransitModes.joinToString(",") { it.serialName() })
          }
          if (requireBikeTransport != null) {
            parameters.append("requireBikeTransport", requireBikeTransport.toString())
          }
          if (requireCarTransport != null) {
            parameters.append("requireCarTransport", requireCarTransport.toString())
          }
          if (maxPreTransitTime != null) {
            parameters.append("maxPreTransitTime", maxPreTransitTime.toString())
          }
          if (maxPostTransitTime != null) {
            parameters.append("maxPostTransitTime", maxPostTransitTime.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> RoutingClient.OneToAllResponseSuccess(response.body<Reachable>(), response.headers)
        400, 404, 422, 500 -> RoutingClient.OneToAllResponseFailure(response.body<Error>(), response.headers)
        else -> RoutingClient.OneToAllResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return RoutingClient.OneToAllResponseUnknownFailure(500)
    }
  }
}
