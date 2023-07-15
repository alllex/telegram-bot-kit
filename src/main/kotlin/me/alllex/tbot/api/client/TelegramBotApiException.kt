package me.alllex.tbot.api.client

class TelegramBotApiException(
    val response: TelegramResponse<*>,
) : RuntimeException(response.description) {

    override fun toString(): String {
        return "TelegramBotApiException(response=$response)"
    }
}
