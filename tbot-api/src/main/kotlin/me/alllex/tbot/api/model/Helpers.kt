package me.alllex.tbot.api.model

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
