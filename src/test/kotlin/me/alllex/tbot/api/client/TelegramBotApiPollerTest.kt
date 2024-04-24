package me.alllex.tbot.api.client

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.content.TextContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import me.alllex.tbot.api.dsl.startPolling
import me.alllex.tbot.api.model.Chat
import me.alllex.tbot.api.model.ChatId
import me.alllex.tbot.api.model.GetUpdatesRequest
import me.alllex.tbot.api.model.Message
import me.alllex.tbot.api.model.MessageId
import me.alllex.tbot.api.model.MessageUpdate
import me.alllex.tbot.api.model.UnixTimestamp
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.fail

class TelegramBotApiPollerTest {

    private inline fun <reified T> MockRequestHandleScope.respondOk(body: T): HttpResponseData = respond(
        content = TelegramBotApiClient.JSON.encodeToString(TelegramResponse(ok = true, result = body)),
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
                    val body = TelegramBotApiClient.JSON.decodeFromString<GetUpdatesRequest>(requestBodyText)
                    when (body.offset?.toInt()) {
                        0 -> {
                            log.info("[TEST] API: Responding with 1 message")
                            respondOk(
                                listOf(
                                    MessageUpdate(
                                        updateId = 8000,
                                        message = Message(
                                            messageId = MessageId(1000),
                                            date = UnixTimestamp(2023),
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

        val client = TelegramBotApiClient(
            apiToken = ":testToken",
            apiHost = "bot.test",
            httpClient = TelegramBotApiClient.httpClient(engine)
        )

        val poller = launch {
            client.startPolling {
                onMessage { message ->
                    log.info("[TEST] Received message: $message")
                    assertThat(message).all {
                        prop(Message::text).isEqualTo("Message 1")
                    }
                }
            }
        }

        log.info("[TEST] Waiting for deferred for stop")
        deferredStop.await()

        log.info("[TEST] Stopping poller")
        poller.cancel()

        log.info("[TEST] Completing deferred for end")
        deferredEnd.complete(true)

        log.info("[TEST] Done")
    }

    companion object {
        private val log = LoggerFactory.getLogger(TelegramBotApiPollerTest::class.java)
    }

}
