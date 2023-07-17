package me.alllex.tbot.echobot

import kotlinx.coroutines.runBlocking
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.client.unwrap
import me.alllex.tbot.api.model.Message
import me.alllex.tbot.api.model.getMe
import me.alllex.tbot.api.model.reply
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
            println("Received message: ${message.text}")
            val text = message.text
            if (text.equals("stop", ignoreCase = true)) {
                println("Received stop command, stopping...")
                countDownLatch.countDown()
            } else {
                message.reply("Thanks!")
            }
        }
    })

    countDownLatch.await()
    updatePolling.stop()

    println("Done")
}

suspend fun selfCheck(client: TelegramBotApiClient, expectedUsername: String) {
    val me = client.getMe().unwrap()
    if (!me.isBot) error("Self-check for being a bot has failed")
    if (me.username != expectedUsername) {
        error("Username must be @$expectedUsername, but it is @${me.username}")
    }
}
