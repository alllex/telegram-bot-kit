package me.alllex.tbot.api.model

import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.client.TelegramResponse

suspend fun TelegramBotApiClient.sendMarkdown(
    chatId: ChatId,
    text: String,
    messageThreadId: MessageThreadId? = null,
    parseMode: String? = "markdown",
    entities: List<MessageEntity>? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyToMessageId: MessageId? = null,
    allowSendingWithoutReply: Boolean? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> =
    sendMessage(SendMessageRequest(chatId, text, messageThreadId, parseMode, entities, disableWebPagePreview, disableNotification, protectContent, replyToMessageId, allowSendingWithoutReply, replyMarkup))


context(TelegramBotApiContext)
suspend fun Chat.sendMessage(
    text: String,
    messageThreadId: MessageThreadId? = null,
    parseMode: String? = null,
    entities: List<MessageEntity>? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyToMessageId: MessageId? = null,
    allowSendingWithoutReply: Boolean? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> =
    botApiClient.sendMessage(SendMessageRequest(id, text, messageThreadId, parseMode, entities, disableWebPagePreview, disableNotification, protectContent, replyToMessageId, allowSendingWithoutReply, replyMarkup))

context(TelegramBotApiContext)
suspend fun Chat.sendMarkdown(
    text: String,
    messageThreadId: MessageThreadId? = null,
    entities: List<MessageEntity>? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyToMessageId: MessageId? = null,
    allowSendingWithoutReply: Boolean? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> =
    botApiClient.sendMessage(SendMessageRequest(id, text, messageThreadId, "markdown", entities, disableWebPagePreview, disableNotification, protectContent, replyToMessageId, allowSendingWithoutReply, replyMarkup))

context(TelegramBotApiContext)
suspend fun Message.reply(
    text: String,
    messageThreadId: MessageThreadId? = null,
    parseMode: String? = null,
    entities: List<MessageEntity>? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowSendingWithoutReply: Boolean? = null,
    replyMarkup: ReplyMarkup? = null,
): TelegramResponse<Message> =
    botApiClient.sendMessage(SendMessageRequest(chat.id, text, messageThreadId, parseMode, entities, disableWebPagePreview, disableNotification, protectContent, messageId, allowSendingWithoutReply, replyMarkup))

context(TelegramBotApiContext)
suspend fun Message.editMarkdown(
    text: String,
    parseMode: String? = "markdown",
    entities: List<MessageEntity>? = null,
    disableWebPagePreview: Boolean? = null,
    replyMarkup: InlineKeyboardMarkup? = null,
): TelegramResponse<Message> =
    botApiClient.editMessageText(EditMessageTextRequest(text, chat.id, messageId, null, parseMode, entities, disableWebPagePreview, replyMarkup))

context(TelegramBotApiContext)
suspend fun Message.delete(
): TelegramResponse<Boolean> =
    botApiClient.deleteMessage(DeleteMessageRequest(chat.id, messageId))

context(TelegramBotApiContext)
suspend fun CallbackQuery.answer(
    text: String? = null,
    showAlert: Boolean? = null,
    url: String? = null,
    cacheTime: Seconds? = null,
): TelegramResponse<Boolean> =
    botApiClient.answerCallbackQuery(AnswerCallbackQueryRequest(id, text, showAlert, url, cacheTime))

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
