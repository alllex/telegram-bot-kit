package me.alllex.tbot.echobot

import kotlinx.coroutines.runBlocking
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.model.*
import me.alllex.tbot.bot.TelegramBotApiPoller
import me.alllex.tbot.bot.TelegramBotUpdateListener
import java.util.concurrent.CountDownLatch


fun main(args: Array<String>) {
    check(args.size == 3) { "Expected 3 arguments" }
    val (botApiToken, botUsername, chatId) = args

    val client = TelegramBotApiClient(botApiToken)

    runBlocking { selfCheck(client, botUsername) }

    val updatePolling = TelegramBotApiPoller(client)
    val countDownLatch = CountDownLatch(1)

    updatePolling.start(object : TelegramBotUpdateListener {
        context(TelegramBotApiContext)
        override suspend fun onMessage(message: Message) {
            println("Received message: $message")
            println()
            val text = message.text
            if (text.equals("stop", ignoreCase = true)) {
                println("Received stop command, stopping...")
                countDownLatch.countDown()
            } else {
                println("Echoing the message back to the chat...")
//                delay(5000)
                message.copyMessage(message.chat.id, replyToMessageId = message.messageId, replyMarkup = inlineKeyboard {
                    button("Wow", "wowwow")
                })
            }
        }
    })

    countDownLatch.await()
    updatePolling.stop()

    println("Done")
}

suspend fun selfCheck(client: TelegramBotApiClient, expectedUsername: String) {
    val me = client.getMe()
    if (!me.isBot) error("Self-check for being a bot has failed")
    if (me.username != expectedUsername) {
        error("Username must be @$expectedUsername, but it is @${me.username}")
    }
}
