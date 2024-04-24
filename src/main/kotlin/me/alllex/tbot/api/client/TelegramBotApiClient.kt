package me.alllex.tbot.api.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.DEFAULT_PORT
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
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

    internal suspend inline fun <reified T> telegramGet(requestMethod: String): TelegramResponse<T> =
        executeRequest(requestMethod, null) {
            httpClient.get {
                urlForTelegramMethod(requestMethod)
            }.body()
        }

    internal suspend inline fun <reified T> telegramPost(requestMethod: String, requestBody: Any): TelegramResponse<T> =
        executeRequest(requestMethod, requestBody) {
            httpClient.post {
                urlForTelegramMethod(requestMethod)
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }.body()
        }

    private fun HttpRequestBuilder.urlForTelegramMethod(requestMethod: String) {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", requestMethod)
        }
    }

    private inline fun <T> executeRequest(
        requestMethod: String,
        requestBody: Any?,
        request: () -> TelegramResponse<T>
    ): TelegramResponse<T> {

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
