package me.alllex.tbot.api.client

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import me.alllex.tbot.api.model.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.fail

@OptIn(ExperimentalSerializationApi::class)
class TelegramBotApiPollerTest {

    private val json = Json {
        namingStrategy = JsonNamingStrategy.SnakeCase
        explicitNulls = false
    }

    private inline fun <reified T> MockRequestHandleScope.respondOk(body: T): HttpResponseData = respond(
        content = json.encodeToString(TelegramResponse(ok = true, result = body)),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )

    @Test
    fun pollerRequestsUpdatesAndCanBeStopped() = runTest {

        val deferredStop = CompletableDeferred<Boolean>()
        val deferredEnd = CompletableDeferred<Boolean>()

        val engine = MockEngine { request ->
            val url = request.url.toString()
            val requestBodyText = (request.body as TextContent).text
            when {
                url.endsWith("/getUpdates") -> {
                    val body = json.decodeFromString<GetUpdatesRequest>(requestBodyText)
                    when (body.offset?.toInt()) {
                        0 -> {
                            log.info("[TEST] API: Responding with 1 message")
                            respondOk(
                                listOf(
                                    MessageUpdate(
                                        updateId = 8000,
                                        message = Message(
                                            messageId = MessageId(1000),
                                            date = 2023,
                                            chat = Chat(id = ChatId(1), type = "private"),
                                            text = "Message 1"
                                        )
                                    )
                                )
                            )
                        }

                        8001 -> {
                            log.info("[TEST] API: Completing deferred for stop")
                            deferredStop.complete(true)
                            log.info("[TEST] API: Waiting for deferred for end")
                            deferredEnd.await()
                            log.info("[TEST] API: Responding with empty list")
                            respondOk(listOf<MessageUpdate>())
                        }

                        else -> fail("Unexpected offset: ${body.offset}")
                    }
                }

                else -> fail("Unexpected request: $request")
            }

        }

        val client = TelegramBotApiClient(":testToken", host = "bot.test", engine = engine)
        val poller = TelegramBotApiPoller(client)

        poller.start(TelegramBotUpdateListener(
            onMessage = { message ->
                log.info("[TEST] Received message: $message")
                assertThat(message).all {
                    prop(Message::text).isEqualTo("Message 1")
                }
            }
        ))

        log.info("[TEST] Waiting for deferred for stop")
        deferredStop.await()

        log.info("[TEST] Stopping poller")
        poller.stop()

        log.info("[TEST] Completing deferred for end")
        deferredEnd.complete(true)

        log.info("[TEST] Done")
    }

    companion object {
        private val log = LoggerFactory.getLogger(TelegramBotApiPollerTest::class.java)
    }

}
