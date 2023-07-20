package me.alllex.tbot.api.model

import me.alllex.tbot.api.client.TelegramBotApiClient
import kotlin.time.Duration


val Int.asSeconds get() = Seconds(this.toLong())

val Long.asSeconds get() = Seconds(this)

val Duration.asSeconds get() = Seconds(this.inWholeSeconds)

val User.usernameOrId: String get() = "@" + (username ?: id.value.toString())

val MessageEntity.isBotCommand get(): Boolean = type == "bot_command"

fun Message.findLeadCommand(): MessageEntity? {
    return findEntity { it.isBotCommand }
        ?.takeIf { it.offset == 0L }
}

inline fun Message.findEntity(predicate: (MessageEntity) -> Boolean): MessageEntity? {
    return entities?.find(predicate)
}

inline fun Message.findEntityText(predicate: (MessageEntity) -> Boolean): String? {
    val e = findEntity(predicate) ?: return null
    return this[e]
}

/**
 * Extracts part of the [text] which is covered by the [entity]
 */
operator fun Message.get(entity: MessageEntity): String {
    val text = ensureText("unable to extract entity from missing text")
    val start = entity.offset
    val endExcl = start + entity.length
    return text.substring(start.toInt(), endExcl.toInt())
}

fun Message.getTextAfter(entity: MessageEntity): String {
    val text = ensureText("unable to determine entity boundaries in empty text")
    val startIndex = entity.offset + entity.length
    return text.substring(startIndex.toInt())
}

private fun Message.ensureText(reason: String): String {
    val text = text
    requireNotNull(text) { reason }
    return text
}

suspend fun TelegramBotApiClient.selfCheck(expectedUsername: String) {
    val me = getMe()
    check(me.isBot) {
        "Self-check for being a bot has failed"
    }
    check(me.username == expectedUsername) {
        "Username must be @$expectedUsername, but it is @${me.username}"
    }
}
