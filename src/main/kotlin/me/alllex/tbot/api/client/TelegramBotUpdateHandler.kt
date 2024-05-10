package me.alllex.tbot.api.client

fun interface TelegramBotUpdateHandler<T> {
    context(TelegramBotApiContext)
    suspend fun handle(update: T)
}
