package me.alllex.tbot.api.client

fun interface TelegramBotUpdateHandler<T> {
    context(_: TelegramBotApiContext)
    suspend fun handle(update: T)
}
