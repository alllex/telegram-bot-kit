package me.alllex.tbot.demo

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.dsl.startPolling
import me.alllex.tbot.api.model.answer
import me.alllex.tbot.api.model.button
import me.alllex.tbot.api.model.copyMessage
import me.alllex.tbot.api.model.inlineKeyboard
import me.alllex.tbot.api.model.selfCheck


suspend fun main(args: Array<String>) = coroutineScope {
    check(args.size >= 2) { "Expected 2 arguments" }
    val (botApiToken, botUsername) = args

    val client = TelegramBotApiClient(botApiToken)

    println("Checking bot info...")
    runBlocking { client.selfCheck(botUsername) }
    println("Bot info is OK")
    val mutex = Mutex(true)

    println("Starting bot...")
    val poller = launch {
        client.startPolling {
            onMessage { message ->
                println("Received message: $message")
                val text = message.text
                if (text.equals("stop", ignoreCase = true)) {
                    println("Received stop command, stopping...")
                    mutex.unlock()
                } else {
                    println("Echoing the message back to the chat...")
                    message.copyMessage(
                        message.chat.id,
                        replyToMessageId = message.messageId,
                        replyMarkup = inlineKeyboard {
                            button("Button", "wow")
                        })
                }
            }
            onCallbackQuery { callbackQuery ->
                println("Received callback query: $callbackQuery")
                callbackQuery.answer("Wow!")
            }
        }
    }

    mutex.lock()
    poller.cancel()

    println("Done")
}
