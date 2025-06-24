@file:Suppress("CONTEXT_RECEIVERS_DEPRECATED")

package me.alllex.tbot.api.client

fun interface TelegramBotUpdateHandler<T> {
    context(botApi: TelegramBotApiContext)
    suspend fun handle(update: T)
}
