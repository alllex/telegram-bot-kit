package me.alllex.tbot.echobot

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.alllex.tbot.api.client.*
import me.alllex.tbot.api.model.*
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.runForever

fun main(args: Array<String>) {
    check(args.size == 3) { "Expected 3 arguments" }
    val (botApiToken, botUsername, chatId) = args

    val client = TelegramBotApiClient(botApiToken)

    runBlocking { selfCheck(client, botUsername) }

    val bot = TelegramBot(client)

    bot.start {
        println("Received update: ${it.toString().take(64)}")
        println()
        if (it !is MessageUpdate) return@start

        val text = it.message.text
        if (text.equals("stop", ignoreCase = true)) {
            bot.stop()
        } else {
            it.message.reply("Thanks!")
        }
    }

    runForever(loggerForClass<TelegramBot>())
}

suspend fun selfCheck(client: TelegramBotApiClient, expectedUsername: String) {
    val me = client.getMe().unwrap()
    if (!me.isBot) error("Self-check for being a bot has failed")
    if (me.username != expectedUsername) {
        error("Username must be @$expectedUsername, but it is @${me.username}")
    }
}
