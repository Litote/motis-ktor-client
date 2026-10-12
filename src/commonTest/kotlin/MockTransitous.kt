import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.litote.motis.client.client.ClientConfiguration
import org.litote.motis.client.LIBRARY_USER_AGENT
import org.litote.motis.client.motisClientConfiguration

val EXPECTED_USER_AGENT: String =
    "motis-ktor-client-tests/1.0 (+https://github.com/Litote/motis-ktor-client) $LIBRARY_USER_AGENT"

/**
 * Transitous API replaced by a [MockEngine]: records the requests and answers with [handler].
 */
class MockTransitous(
    private val handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
) {
    val requests: MutableList<HttpRequestData> = mutableListOf()

    val configuration: ClientConfiguration =
        motisClientConfiguration(
            applicationName = "motis-ktor-client-tests",
            applicationVersion = "1.0",
            contact = "https://github.com/Litote/motis-ktor-client",
            logLevel = LogLevel.NONE,
            engine =
                object : HttpClientEngineFactory<HttpClientEngineConfig> {
                    override fun create(block: HttpClientEngineConfig.() -> Unit): HttpClientEngine =
                        MockEngine { request ->
                            requests += request
                            handler(request)
                        }
                },
        )

    val lastRequest: HttpRequestData get() = requests.last()
}

fun MockRequestHandleScope.respondJson(
    content: String,
    status: HttpStatusCode = HttpStatusCode.OK,
): HttpResponseData = respond(content, status, headersOf(HttpHeaders.ContentType, "application/json"))
