package me.alllex.tbot.echobot

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.model.*
import me.alllex.tbot.api.client.TelegramBotApiPoller
import me.alllex.tbot.api.client.TelegramBotUpdateListener
import java.util.concurrent.CountDownLatch


fun main(args: Array<String>) {
    check(args.size >= 2) { "Expected 2 arguments" }
    val (botApiToken, botUsername) = args

    val client = TelegramBotApiClient(botApiToken)

    runBlocking { client.selfCheck(botUsername) }

    val poller = TelegramBotApiPoller(client)
    val countDownLatch = CountDownLatch(1)

    val listener = TelegramBotUpdateListener(
        onMessage = { message ->
            println("Received message: $message")
            val text = message.text
            if (text.equals("stop", ignoreCase = true)) {
                println("Received stop command, stopping...")
                countDownLatch.countDown()
            } else {
                println("Echoing the message back to the chat...")
                delay(5000)
                message.copyMessage(message.chat.id, replyToMessageId = message.messageId, replyMarkup = inlineKeyboard {
                    button("Wow", "wowwow")
                })
            }
        },
        onCallbackQuery = { callbackQuery ->
            println("Received callback query: $callbackQuery")
            callbackQuery.answer("Wow!")
        }
    )

    poller.start(listener)
    countDownLatch.await()
    poller.stopBlocking()

    println("Done")
}
