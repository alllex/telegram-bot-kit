package me.alllex.tbot.api.client

import kotlinx.serialization.Serializable
import me.alllex.tbot.api.model.ResponseParameters

@Serializable
data class TelegramResponse<out T>(
    val result: T? = null,
    val ok: Boolean,
    val description: String? = null,
    val errorCode: Int? = null,
    val parameters: ResponseParameters? = null
)

fun <T> TelegramResponse<T>.unwrap(): T {
    check (ok && result != null) {
        "Telegram API error: $errorCode $description $parameters"
    }
    return result
}
