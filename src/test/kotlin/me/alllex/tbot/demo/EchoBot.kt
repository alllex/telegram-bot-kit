package me.alllex.tbot.demo

import kotlinx.coroutines.runBlocking
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiPoller
import me.alllex.tbot.api.client.TelegramBotUpdateListener
import me.alllex.tbot.api.model.*
import java.util.concurrent.CountDownLatch


fun main(args: Array<String>) {
    check(args.size >= 2) { "Expected 2 arguments" }
    val (botApiToken, botUsername) = args

    val client = TelegramBotApiClient(botApiToken)

    println("Checking bot info...")
    runBlocking { client.selfCheck(botUsername) }
    println("Bot info is OK")

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
                message.copyMessage(message.chat.id, replyParameters = ReplyParameters(message.messageId), replyMarkup = inlineKeyboard {
                    button("Button", "wow")
                })
            }
        },
        onCallbackQuery = { callbackQuery ->
            println("Received callback query: $callbackQuery")
            callbackQuery.answer("Wow!")
        }
    )

    println("Starting bot...")
    poller.start(listener)
    println("Bot started")

    countDownLatch.await()
    poller.stopBlocking()

    println("Done")
}
