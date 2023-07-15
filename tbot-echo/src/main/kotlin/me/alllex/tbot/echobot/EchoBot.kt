package me.alllex.tbot.echobot

import kotlinx.coroutines.runBlocking
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiPoller
import me.alllex.tbot.api.client.TelegramBotUpdateListener
import me.alllex.tbot.api.client.TelegramResponse
import me.alllex.tbot.api.model.*
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.runForever

class EchoBot {

}

fun main(args: Array<String>) {
    check(args.size == 3) { "Expected 3 arguments" }
    val (botApiToken, botUsername, chatId) = args

    val client = TelegramBotApiClient(
        botApiToken
    )

    val poller = TelegramBotApiPoller(client)

    try {
        poller.start()

        poller.addListener(object : TelegramBotUpdateListener {
            override fun onUpdate(update: Update) {
                println("Received update: $update")
                when (update) {
                    is MessageUpdate -> {
                        val message = update.message
                        runBlocking {
                            client.sendMessage(
                                chatId = message.chat.id,
                                text = message.text ?: "I don't understand you"
                            )
                        }
                    }
                    else -> {}
                }
            }
        })

        runBlocking {
            selfCheck(client, botUsername)

            client.sendMessage(
                chatId = ChatId(chatId.toLong()),
                text = "Hello, world!"
            )
        }

        runForever(loggerForClass<EchoBot>())
    } finally {
        poller.stop()
    }

}

fun <T> TelegramResponse<T>.unwrap(): T = result ?: error("Response is not ok: $this")

suspend fun selfCheck(client: TelegramBotApiClient, expectedUsername: String) {
    val me = client.getMe().unwrap()
    if (!me.isBot) error("Self-check for being a bot has failed")
    if (me.username != expectedUsername) {
        error("Username must be @$expectedUsername, but it is @${me.username}")
    }
}
