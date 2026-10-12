package org.litote.motis.client.client

import io.ktor.client.call.body
import io.ktor.client.request.`get`
import io.ktor.http.Headers
import kotlin.Int
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.litote.motis.client.client.ClientConfiguration.Companion.defaultClientConfiguration
import org.litote.motis.client.model.HealthResponse

public interface Client {
  public suspend fun health(): HealthResponse

  @Serializable
  public sealed class HealthResponse {
    public abstract val headers: Headers
  }

  @Serializable
  public data class HealthResponseSuccess(
    public val body: org.litote.motis.client.model.HealthResponse,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : HealthResponse()

  @Serializable
  public data class HealthResponseFailure(
    public val body: org.litote.motis.client.model.HealthResponse,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : HealthResponse()

  @Serializable
  public data class HealthResponseUnknownFailure(
    public val statusCode: Int,
    @Transient
    override val headers: Headers = Headers.Empty,
  ) : HealthResponse()
}

public fun Client(configuration: ClientConfiguration = defaultClientConfiguration): Client = DefaultClient(configuration)

public class DefaultClient(
  private val configuration: ClientConfiguration = defaultClientConfiguration,
) : Client {
  override suspend fun health(): Client.HealthResponse {
    try {
      val response = configuration.client.`get`("api/v1/health") {
      }
      return when (response.status.value) {
        200 -> Client.HealthResponseSuccess(response.body<HealthResponse>(), response.headers)
        400 -> Client.HealthResponseFailure(response.body<HealthResponse>(), response.headers)
        else -> Client.HealthResponseUnknownFailure(response.status.value, response.headers)
      }
    }
    catch(e: CancellationException) {
      throw e
    }
    catch(e: Exception) {
      configuration.exceptionLogger(e)
      return Client.HealthResponseUnknownFailure(500)
    }
  }
}
