package me.alllex.tbot.api.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy


const val DEFAULT_TELEGRAM_API_HOST = "api.telegram.org"

class TelegramBotApiClient private constructor(
    internal val httpClient: HttpClient,
    internal val apiToken: String,
    internal val apiProtocol: URLProtocol = URLProtocol.HTTPS,
    internal val apiHost: String = DEFAULT_TELEGRAM_API_HOST,
    internal val apiPort: Int = DEFAULT_PORT,
    private val onRequest: (TelegramBotApiClient.(requestMethod: String, requestBody: Any?) -> Unit)? = null,
    private val onResponse: (TelegramBotApiClient.(requestMethod: String, requestBody: Any?, responseBody: TelegramResponse<*>) -> Unit)? = null,
) {

    internal inline fun <T> executeRequest(requestMethod: String, requestBody: Any?, request: () -> TelegramResponse<T>): TelegramResponse<T> {
        onRequest?.invoke(this, requestMethod, requestBody)
        val responseBody = request()
        onResponse?.invoke(this, requestMethod, requestBody, responseBody)
        return responseBody
    }

    fun closeHttpClient() {
        httpClient.close()
    }

    companion object {

        @OptIn(ExperimentalSerializationApi::class)
        private fun HttpClientConfig<*>.applyDefaultConfiguration() {
            install(ContentNegotiation) {
                json(Json {
                    // chat_id to chatId
                    namingStrategy = JsonNamingStrategy.SnakeCase
                    // To avoid the deserialization breaking when Telegram introduces new fields
                    ignoreUnknownKeys = true
                    // Smaller payloads
                    explicitNulls = false
                })
            }
        }

        operator fun invoke(
            apiToken: String,
            protocol: URLProtocol = URLProtocol.HTTPS,
            engine: HttpClientEngine? = null,
            host: String = DEFAULT_TELEGRAM_API_HOST,
            port: Int = DEFAULT_PORT,
            onRequest: (TelegramBotApiClient.(requestMethod: String, requestBody: Any?) -> Unit)? = null,
            onResponse: (TelegramBotApiClient.(requestMethod: String, requestBody: Any?, responseBody: TelegramResponse<*>) -> Unit)? = null,
            configuration: (HttpClientConfig<*>.() -> Unit)? = null,
        ): TelegramBotApiClient {
            val httpClient = if (engine == null) {
                HttpClient {
                    applyDefaultConfiguration()
                    configuration?.invoke(this)
                }
            } else {
                HttpClient(engine) {
                    applyDefaultConfiguration()
                    configuration?.invoke(this)
                }
            }
            return TelegramBotApiClient(httpClient, apiToken, protocol, host, port, onRequest, onResponse)
        }

        operator fun <T : HttpClientEngineConfig> invoke(
            apiToken: String,
            engine: HttpClientEngineFactory<T>,
            protocol: URLProtocol = URLProtocol.HTTPS,
            host: String = DEFAULT_TELEGRAM_API_HOST,
            port: Int = DEFAULT_PORT,
            onRequest: (TelegramBotApiClient.(requestMethod: String, requestBody: Any?) -> Unit)? = null,
            onResponse: (TelegramBotApiClient.(requestMethod: String, requestBody: Any?, responseBody: TelegramResponse<*>) -> Unit)? = null,
            configuration: (HttpClientConfig<T>.() -> Unit)? = null,
        ): TelegramBotApiClient {
            val httpClient = HttpClient(engine) {
                applyDefaultConfiguration()
                configuration?.let { it() }
            }
            return TelegramBotApiClient(httpClient, apiToken, protocol, host, port, onRequest, onResponse)
        }

    }

}
