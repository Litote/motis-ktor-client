package org.litote.motis.client

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.logging.LogLevel
import org.litote.motis.client.client.ClientConfiguration

/**
 * Creates the [ClientConfiguration] to use with the MOTIS clients.
 *
 * The Transitous usage policy requires a `User-Agent` header with the application name,
 * the application version and a way of contact (e-mail address or website URL).
 * The header sent is `applicationName/applicationVersion (+contact) motis-ktor-client/<version> (...)`.
 *
 * In a browser, the `User-Agent` header cannot be overridden: the `Referer` header is used instead,
 * so the website must display contact information.
 *
 * @param logLevel the Ktor logging level of the requests (no logging by default)
 * @param engine the Ktor engine to use; when `null`, the engine found on the classpath is used
 * (add a Ktor engine dependency, or `io.ktor:ktor-client-engine-defaults`, to the application)
 */
public fun motisClientConfiguration(
    applicationName: String,
    applicationVersion: String,
    contact: String,
    baseUrl: String = "https://api.transitous.org/",
    logLevel: LogLevel = LogLevel.NONE,
    engine: HttpClientEngineFactory<*>? = null,
): ClientConfiguration {
    require(applicationName.isNotBlank()) { "applicationName is required" }
    require(applicationVersion.isNotBlank()) { "applicationVersion is required" }
    require(contact.isNotBlank()) { "contact is required" }
    return ClientConfiguration(
        baseUrl = baseUrl,
        logLevel = logLevel,
        userAgent = "$applicationName/$applicationVersion (+$contact) $LIBRARY_USER_AGENT",
        engine = engine,
    )
}
