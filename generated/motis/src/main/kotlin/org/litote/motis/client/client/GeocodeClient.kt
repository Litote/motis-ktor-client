package org.litote.motis.client.client

import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.Headers
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.litote.motis.client.client.ClientConfiguration.Companion.defaultClientConfiguration
import org.litote.motis.client.model.Error
import org.litote.motis.client.model.LocationType
import org.litote.motis.client.model.Match
import org.litote.motis.client.model.Mode

public interface GeocodeClient {
  /**
   * Translate coordinates to the closest address(es)/places/stops.
   */
  public suspend fun reverseGeocode(
    place: String,
    type: List<LocationType>? = null,
    numResults: Long? = null,
  ): ReverseGeocodeResponse

  /**
   * Autocompletion & geocoding that resolves user input addresses including coordinates
   */
  public suspend fun geocode(
    text: String,
    language: List<String>? = null,
    type: List<LocationType>? = null,
    mode: List<Mode>? = null,
    place: String? = null,
    placeBias: Double? = 1.0,
    numResults: Long? = null,
  ): GeocodeResponse

  @Serializable
  public sealed class ReverseGeocodeResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class ReverseGeocodeResponseSuccess(
    public val body: List<Match>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : ReverseGeocodeResponse()

  @Serializable
  public data class ReverseGeocodeResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : ReverseGeocodeResponse()

  @Serializable
  public data class ReverseGeocodeResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : ReverseGeocodeResponse()

  @Serializable
  public sealed class GeocodeResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class GeocodeResponseSuccess(
    public val body: List<Match>,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : GeocodeResponse()

  @Serializable
  public data class GeocodeResponseFailure(
    public val body: Error,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : GeocodeResponse()

  @Serializable
  public data class GeocodeResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : GeocodeResponse()
}

public fun GeocodeClient(configuration: ClientConfiguration = defaultClientConfiguration): GeocodeClient = DefaultGeocodeClient(configuration)

public class DefaultGeocodeClient(
  private val configuration: ClientConfiguration = defaultClientConfiguration,
) : GeocodeClient {
  override suspend fun reverseGeocode(
    place: String,
    type: List<LocationType>?,
    numResults: Long?,
  ): GeocodeClient.ReverseGeocodeResponse {
    try {
      val response = configuration.client.`get`("api/v1/reverse-geocode") {
        url {
          parameters.append("place", place)
          if (type != null) {
            parameters.append("type", type.joinToString(",") { it.serialName() })
          }
          if (numResults != null) {
            parameters.append("numResults", numResults.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> GeocodeClient.ReverseGeocodeResponseSuccess(response.body<List<Match>>(), response.headers)
        400 -> GeocodeClient.ReverseGeocodeResponseFailure(response.body<Error>(), response.headers)
        else -> GeocodeClient.ReverseGeocodeResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return GeocodeClient.ReverseGeocodeResponseUnknownFailure(500)
    }
  }

  override suspend fun geocode(
    text: String,
    language: List<String>?,
    type: List<LocationType>?,
    mode: List<Mode>?,
    place: String?,
    placeBias: Double?,
    numResults: Long?,
  ): GeocodeClient.GeocodeResponse {
    try {
      val response = configuration.client.`get`("api/v1/geocode") {
        url {
          parameters.append("text", text)
          if (language != null) {
            parameters.append("language", language.joinToString(","))
          }
          if (type != null) {
            parameters.append("type", type.joinToString(",") { it.serialName() })
          }
          if (mode != null) {
            parameters.append("mode", mode.joinToString(",") { it.serialName() })
          }
          if (place != null) {
            parameters.append("place", place)
          }
          if (placeBias != null) {
            parameters.append("placeBias", placeBias.toString())
          }
          if (numResults != null) {
            parameters.append("numResults", numResults.toString())
          }
        }
      }
      return when (response.status.value) {
        200 -> GeocodeClient.GeocodeResponseSuccess(response.body<List<Match>>(), response.headers)
        400 -> GeocodeClient.GeocodeResponseFailure(response.body<Error>(), response.headers)
        else -> GeocodeClient.GeocodeResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return GeocodeClient.GeocodeResponseUnknownFailure(500)
    }
  }
}
