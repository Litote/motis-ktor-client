package org.litote.motis.client.client

import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.Headers
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.litote.motis.client.client.ClientConfiguration.Companion.defaultClientConfiguration
import org.litote.motis.client.model.Place
import org.litote.motis.client.model.Transfer

public interface DebugClient {
  /**
   * Prints all transfers of a timetable location (track, bus stop, etc.)
   */
  public suspend fun transfers(id: String): TransfersResponse

  @Serializable
  public data class TransfersResponseBody(
    public val equivalences: List<Place>,
    public val hasCarTransfers: Boolean,
    public val hasFootTransfers: Boolean,
    public val hasWheelchairTransfers: Boolean,
    public val place: Place,
    public val root: Place,
    public val transfers: List<Transfer>,
  )

  @Serializable
  public sealed class TransfersResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class TransfersResponseSuccess(
    public val body: TransfersResponseBody,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TransfersResponse()

  @Serializable
  public data class TransfersResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : TransfersResponse()
}

public fun DebugClient(configuration: ClientConfiguration = defaultClientConfiguration): DebugClient = DefaultDebugClient(configuration)

public class DefaultDebugClient(
  private val configuration: ClientConfiguration = defaultClientConfiguration,
) : DebugClient {
  override suspend fun transfers(id: String): DebugClient.TransfersResponse {
    try {
      val response = configuration.client.`get`("api/debug/transfers") {
        url {
          parameters.append("id", id)
        }
      }
      return when (response.status.value) {
        200 -> DebugClient.TransfersResponseSuccess(response.body<DebugClient.TransfersResponseBody>(), response.headers)
        else -> DebugClient.TransfersResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return DebugClient.TransfersResponseUnknownFailure(500)
    }
  }
}
