package me.alllex.tbot.api.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.DEFAULT_PORT
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlin.time.Duration.Companion.seconds


const val DEFAULT_TELEGRAM_API_HOST = "api.telegram.org"

class TelegramBotApiClient(
    val apiToken: String,
    val httpClient: HttpClient = httpClient(),
    val apiProtocol: URLProtocol = URLProtocol.HTTPS,
    val apiHost: String = DEFAULT_TELEGRAM_API_HOST,
    val apiPort: Int = DEFAULT_PORT,
) {

    companion object Defaults {

        val JSON
            get() = Json {
                // chat_id to chatId
                namingStrategy = JsonNamingStrategy.SnakeCase
                // To avoid the deserialization breaking when Telegram introduces new fields
                ignoreUnknownKeys = true
                // Smaller payloads
                explicitNulls = false
            }

        fun HttpClientConfig<*>.defaultConfiguration() {
            install(ContentNegotiation) {
                json(JSON)
            }
            install(HttpTimeout) {
                requestTimeout = 10.seconds
            }
            install(HttpRequestRetry) {
                constantDelay()
            }
        }

        fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}) = HttpClient {
            defaultConfiguration()
            config()
        }

        fun <T : HttpClientEngineConfig> httpClient(
            engineFactory: HttpClientEngineFactory<T>,
            config: HttpClientConfig<T>.() -> Unit = {}
        ) = HttpClient(engineFactory) {
            defaultConfiguration()
            config()
        }

        fun httpClient(
            engine: HttpClientEngine,
            config: HttpClientConfig<*>.() -> Unit = {}
        ) = HttpClient(engine) {
            defaultConfiguration()
            config()
        }
    }

}
