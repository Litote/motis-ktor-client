package org.litote.motis.client.client

import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.Headers
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.litote.motis.client.client.ClientConfiguration.Companion.defaultClientConfiguration
import org.litote.motis.client.model.Error
import org.litote.motis.client.model.Mode
import org.litote.motis.client.model.Place
import org.litote.motis.client.model.RentalProvider
import org.litote.motis.client.model.RentalProviderGroup
import org.litote.motis.client.model.RentalStation
import org.litote.motis.client.model.RentalVehicle
import org.litote.motis.client.model.RentalZone
import org.litote.motis.client.model.RouteInfo
import org.litote.motis.client.model.RoutePolyline
import org.litote.motis.client.model.ServerConfig
import org.litote.motis.client.model.TripSegment

public interface MapClient {
  /**
   * Given a area frame (box defined by top right and bottom left corner) and a time frame,
   * it returns all trips and their respective shapes that operate in this area + time frame.
   * Trips are filtered by zoom level. On low zoom levels, only long distance trains will be shown
   * while on high zoom levels, also metros, buses and trams will be returned.
   *
   */
  public suspend fun trips(
    zoom: Double,
    min: String,
    max: String,
    startTime: Instant,
    endTime: Instant,
    precision: Double? = 5.0,
    language: List<String>? = null,
  ): TripsResponse

  /**
   * initial location to view the map at after loading based on where public transport should be visible
   */
  public suspend fun initial(): InitialResponse

  /**
   * Get all stops for a map section
   */
  public suspend fun stops(
    min: String,
    max: String,
    grouped: Boolean? = null,
    modes: List<Mode>? = null,
    language: List<String>? = null,
  ): StopsResponse

  /**
   * Get all available levels for a map section
   */
  public suspend fun levels(min: String, max: String): LevelsResponse

  /**
   * Given an area frame (box defined by the top-right and bottom-left corners),
   * it returns all routes and their respective shapes that operate within this area.
   * Routes are filtered by zoom level. On low zoom levels, only long distance trains will be shown
   * while on high zoom levels, also metros, buses and trams will be returned.
   *
   */
  public suspend fun routes(
    zoom: Double,
    min: String,
    max: String,
    language: List<String>? = null,
  ): RoutesResponse

  /**
   * Returns the full data for a single route, including all stops and
   * polyline segments.
   *
   */
  public suspend fun routeDetails(routeIdx: Long, language: List<String>? = null): RouteDetailsResponse

  /**
   * Get a list of rental providers or all rental stations and vehicles for
   * a map section or provider
   *
   */
  public suspend fun rentals(
    min: String? = null,
    max: String? = null,
    point: String? = null,
    radius: Long? = null,
    providerGroups: List<String>? = null,
    providers: List<String>? = null,
    withProviders: Boolean? = true,
    withStations: Boolean? = true,
    withVehicles: Boolean? = true,
    withZones: Boolean? = true,
  ): RentalsResponse

  @Serializable
  public sealed class TripsResponse : OperationResponse<List<TripSegment>> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class TripsResponseSuccess(
    override val body: List<TripSegment>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TripsResponse(),
      OperationResponseSuccess<List<TripSegment>>

  @Serializable
  public data class TripsResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TripsResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class TripsResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TripsResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public data class InitialResponseBody(
    public val lat: Double,
    public val lon: Double,
    public val serverConfig: ServerConfig,
    public val zoom: Double,
  )

  @Serializable
  public sealed class InitialResponse : OperationResponse<InitialResponseBody> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class InitialResponseSuccess(
    override val body: InitialResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : InitialResponse(),
      OperationResponseSuccess<InitialResponseBody>

  @Serializable
  public data class InitialResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : InitialResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class InitialResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : InitialResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public sealed class StopsResponse : OperationResponse<List<Place>> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class StopsResponseSuccess(
    override val body: List<Place>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : StopsResponse(),
      OperationResponseSuccess<List<Place>>

  @Serializable
  public data class StopsResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : StopsResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class StopsResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : StopsResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public sealed class LevelsResponse : OperationResponse<List<Double>> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class LevelsResponseSuccess(
    override val body: List<Double>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : LevelsResponse(),
      OperationResponseSuccess<List<Double>>

  @Serializable
  public data class LevelsResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : LevelsResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class LevelsResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : LevelsResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public data class RoutesResponseBody(
    public val polylines: List<RoutePolyline>,
    public val routes: List<RouteInfo>,
    public val stops: List<Place>,
    public val zoomFiltered: Boolean,
  )

  @Serializable
  public sealed class RoutesResponse : OperationResponse<RoutesResponseBody> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class RoutesResponseSuccess(
    override val body: RoutesResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RoutesResponse(),
      OperationResponseSuccess<RoutesResponseBody>

  @Serializable
  public data class RoutesResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RoutesResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class RoutesResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RoutesResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public data class RouteDetailsResponseBody(
    public val polylines: List<RoutePolyline>,
    public val routes: List<RouteInfo>,
    public val stops: List<Place>,
    public val zoomFiltered: Boolean,
  )

  @Serializable
  public sealed class RouteDetailsResponse : OperationResponse<RouteDetailsResponseBody> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class RouteDetailsResponseSuccess(
    override val body: RouteDetailsResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RouteDetailsResponse(),
      OperationResponseSuccess<RouteDetailsResponseBody>

  @Serializable
  public data class RouteDetailsResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RouteDetailsResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class RouteDetailsResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RouteDetailsResponse(),
      OperationResponseUnknownFailure

  @Serializable
  public data class RentalsResponseBody(
    public val providerGroups: List<RentalProviderGroup>,
    public val providers: List<RentalProvider>,
    public val stations: List<RentalStation>,
    public val vehicles: List<RentalVehicle>,
    public val zones: List<RentalZone>,
  )

  @Serializable
  public sealed class RentalsResponse : OperationResponse<RentalsResponseBody> {
    abstract override val headers: Headers
  }

  @Serializable
  public data class RentalsResponseSuccess(
    override val body: RentalsResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RentalsResponse(),
      OperationResponseSuccess<RentalsResponseBody>

  @Serializable
  public data class RentalsResponseFailure(
    override val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RentalsResponse(),
      OperationResponseFailure<Error>

  @Serializable
  public data class RentalsResponseUnknownFailure(
    override val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : RentalsResponse(),
      OperationResponseUnknownFailure
}

public fun MapClient(configuration: ClientConfiguration = defaultClientConfiguration): MapClient = DefaultMapClient(configuration)

public class DefaultMapClient(
  private val configuration: ClientConfiguration = defaultClientConfiguration,
) : MapClient {
  override suspend fun trips(
    zoom: Double,
    min: String,
    max: String,
    startTime: Instant,
    endTime: Instant,
    precision: Double?,
    language: List<String>?,
  ): MapClient.TripsResponse {
    try {
      val response = configuration.client.`get`("api/v6/map/trips") {
        url {
          parameters.append("zoom", zoom.toString())
          parameters.append("min", min)
          parameters.append("max", max)
          parameters.append("startTime", startTime.let { kotlin.time.Instant.fromEpochSeconds(it.epochSeconds, it.nanosecondsOfSecond / 1_000_000 * 1_000_000) }.toString())
          parameters.append("endTime", endTime.let { kotlin.time.Instant.fromEpochSeconds(it.epochSeconds, it.nanosecondsOfSecond / 1_000_000 * 1_000_000) }.toString())
          if (precision != null) {
            parameters.append("precision", precision.toString())
          }
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
        }
      }
      return when (response.status.value) {
        200 -> MapClient.TripsResponseSuccess(response.body<List<TripSegment>>(), response.headers)
        400, 404, 422, 500 -> MapClient.TripsResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.TripsResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.TripsResponseUnknownFailure(500)
    }
  }

  override suspend fun initial(): MapClient.InitialResponse {
    try {
      val response = configuration.client.`get`("api/v1/map/initial") {
      }
      return when (response.status.value) {
        200 -> MapClient.InitialResponseSuccess(response.body<MapClient.InitialResponseBody>(), response.headers)
        400 -> MapClient.InitialResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.InitialResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.InitialResponseUnknownFailure(500)
    }
  }

  override suspend fun stops(
    min: String,
    max: String,
    grouped: Boolean?,
    modes: List<Mode>?,
    language: List<String>?,
  ): MapClient.StopsResponse {
    try {
      val response = configuration.client.`get`("api/v6/map/stops") {
        url {
          parameters.append("min", min)
          parameters.append("max", max)
          if (grouped != null) {
            parameters.append("grouped", grouped.toString())
          }
          if (modes != null) {
            parameters.appendAll("modes", modes.map { it.serialName() })
          }
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
        }
      }
      return when (response.status.value) {
        200 -> MapClient.StopsResponseSuccess(response.body<List<Place>>(), response.headers)
        400, 404, 422, 500 -> MapClient.StopsResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.StopsResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.StopsResponseUnknownFailure(500)
    }
  }

  override suspend fun levels(min: String, max: String): MapClient.LevelsResponse {
    try {
      val response = configuration.client.`get`("api/v1/map/levels") {
        url {
          parameters.append("min", min)
          parameters.append("max", max)
        }
      }
      return when (response.status.value) {
        200 -> MapClient.LevelsResponseSuccess(response.body<List<Double>>(), response.headers)
        400, 404, 422, 500 -> MapClient.LevelsResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.LevelsResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.LevelsResponseUnknownFailure(500)
    }
  }

  override suspend fun routes(
    zoom: Double,
    min: String,
    max: String,
    language: List<String>?,
  ): MapClient.RoutesResponse {
    try {
      val response = configuration.client.`get`("api/experimental/map/routes") {
        url {
          parameters.append("zoom", zoom.toString())
          parameters.append("min", min)
          parameters.append("max", max)
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
        }
      }
      return when (response.status.value) {
        200 -> MapClient.RoutesResponseSuccess(response.body<MapClient.RoutesResponseBody>(), response.headers)
        400, 404, 422, 500 -> MapClient.RoutesResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.RoutesResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.RoutesResponseUnknownFailure(500)
    }
  }

  override suspend fun routeDetails(routeIdx: Long, language: List<String>?): MapClient.RouteDetailsResponse {
    try {
      val response = configuration.client.`get`("api/experimental/map/route-details") {
        url {
          parameters.append("routeIdx", routeIdx.toString())
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
        }
      }
      return when (response.status.value) {
        200 -> MapClient.RouteDetailsResponseSuccess(response.body<MapClient.RouteDetailsResponseBody>(), response.headers)
        400, 404, 422, 500 -> MapClient.RouteDetailsResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.RouteDetailsResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.RouteDetailsResponseUnknownFailure(500)
    }
  }

  override suspend fun rentals(
    min: String?,
    max: String?,
    point: String?,
    radius: Long?,
    providerGroups: List<String>?,
    providers: List<String>?,
    withProviders: Boolean?,
    withStations: Boolean?,
    withVehicles: Boolean?,
    withZones: Boolean?,
  ): MapClient.RentalsResponse {
    try {
      val response = configuration.client.`get`("api/v1/rentals") {
        url {
          if (min != null) {
            parameters.append("min", min)
          }
          if (max != null) {
            parameters.append("max", max)
          }
          if (point != null) {
            parameters.append("point", point)
          }
          if (radius != null) {
            parameters.append("radius", radius.toString())
          }
          if (providerGroups != null) {
            parameters.append("providerGroups", providerGroups.joinToString(","))
          }
          if (providers != null) {
            parameters.append("providers", providers.joinToString(","))
          }
          if (withProviders != null) {
            parameters.append("withProviders", withProviders.toString())
          }
          if (withStations != null) {
            parameters.append("withStations", withStations.toString())
          }
          if (withVehicles != null) {
            parameters.append("withVehicles", withVehicles.toString())
          }
          if (withZones != null) {
            parameters.append("withZones", withZones.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> MapClient.RentalsResponseSuccess(response.body<MapClient.RentalsResponseBody>(), response.headers)
        400 -> MapClient.RentalsResponseFailure(response.body<Error>(), response.headers)
        else -> MapClient.RentalsResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return MapClient.RentalsResponseUnknownFailure(500)
    }
  }
}
