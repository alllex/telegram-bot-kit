package me.alllex.tbot.apigen


val unionTypesWithExplicitMarker = setOf(
    "PassportElementError"
)

data class MethodVariation(
    val methodName: String,
    val requiredParams: List<String>,
    val skipParams: List<String>,
    val newMethodName: String,
    val returnType: String,
    val descriptionSubstitutions: List<Pair<Regex, String>> = emptyList(),
)

private val editedMessageDescriptionSubstitutions = listOf(
    "On success, if the( edited)? message is not an inline message, the( edited)? Message is returned, otherwise True is returned.".toRegex() to "On success the edited Message is returned."
)

private val editedInlineMessageDescriptionSubstitutions = listOf(
    "On success, if the( edited)? message is not an inline message, the( edited)? Message is returned, otherwise True is returned.".toRegex() to "On success True is returned."
)

val methodVariations = listOf(
    MethodVariation(
        "editMessageText", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageText", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageText", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageText", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageCaption", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageCaption", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageCaption", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageCaption", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageMedia", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageMedia", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageMedia", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageMedia", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageLiveLocation", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageLiveLocation", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageLiveLocation", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageLiveLocation", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "editMessageReplyMarkup", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "editMessageReplyMarkup", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "editMessageReplyMarkup", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "editInlineMessageReplyMarkup", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "stopMessageLiveLocation", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "stopMessageLiveLocation", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "stopMessageLiveLocation", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "stopInlineMessageLiveLocation", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),

    MethodVariation(
        "setGameScore", listOf("chat_id", "message_id"), listOf("inline_message_id"),
        "setGameScore", "Message",
        editedMessageDescriptionSubstitutions,
    ),
    MethodVariation(
        "setGameScore", listOf("inline_message_id"), listOf("chat_id", "message_id"),
        "setInlineGameScore", "Boolean",
        editedInlineMessageDescriptionSubstitutions,
    ),
)

data class ValueType(
    val name: String,
    val backingType: String,
    val docString: String? = null,
    val fieldPredicate: (type: BotApiElementName, field: BotApiElement.Field) -> Boolean
)

val valueTypes = listOf(
    ValueType("ChatId", "Long", "Chat identifier.") { type, field ->
        (type.value == "Chat" && field.serialName == "id") || field.serialName.endsWith("chat_id")
    },
    ValueType("UserId", "Long", "User identifier.") { type, field ->
        (type.value == "User" && field.serialName == "id") || field.serialName.endsWith("user_id")
    },
    ValueType("MessageId", "Long", "Opaque message identifier.") { _, field ->
        !field.serialName.endsWith("inline_message_id") && field.serialName.endsWith("message_id")
    },
    ValueType("InlineMessageId", "String", "Opaque inline message identifier.") { _, field ->
        field.serialName.endsWith("inline_message_id")
    },
    ValueType("MessageThreadId", "Long", "Opaque message thread identifier.") { _, field ->
        field.serialName.endsWith("message_thread_id")
    },
    ValueType("MessageEffectId", "String", "Unique identifier of the message effect to be added to the message; for private chats only") { _, field ->
        field.serialName.endsWith("effect_id")
    },
    ValueType("CallbackQueryId", "String", "Opaque [CallbackQuery] identifier.") { type, field ->
        (type.value == "CallbackQuery" && field.serialName == "id") || field.serialName.endsWith("callback_query_id")
    },
    ValueType("InlineQueryId", "String", "Opaque [InlineQuery] identifier.") { type, field ->
        (type.value == "InlineQuery" && field.serialName == "id") || field.serialName.endsWith("inline_query_id")
    },
    ValueType("InlineQueryResultId", "String", "Opaque [InlineQueryResult] identifier.") { type, field ->
        (type.value.startsWith("InlineQuery") && field.serialName == "id") ||
            (type.value == "ChosenInlineResult" && field.serialName == "result_id")
    },
    ValueType("FileId", "String", "Identifier for a file, which can be used to download or reuse the file.") { _, field ->
        field.serialName.endsWith("file_id")
    },
    ValueType(
        "FileUniqueId",
        "String",
        "Unique identifier for a file, which is supposed to be the same over time and for different bots.\n\nIt can't be used to download or reuse the file."
    ) { _, field ->
        field.serialName.endsWith("file_unique_id")
    },
    ValueType("ShippingQueryId", "String", "Opaque [ShippingQuery] identifier.") { type, field ->
        (type.value == "ShippingQuery" && field.serialName == "id") || field.serialName.endsWith("shipping_query_id")
    },
    ValueType("WebAppQueryId", "String", "Opaque web-app query identifier.") { _, field ->
        field.serialName.endsWith("web_app_query_id")
    },
    ValueType("CustomEmojiId", "String", "Opaque custom emoji identifier.") { _, field ->
        field.serialName.endsWith("custom_emoji_id")
    },
    ValueType("Seconds", "Long", "Duration in seconds.") { _, field ->
        field.serialName in setOf("cache_time", "duration", "life_period", "open_period", "timeout", "retry_after")
    },
    ValueType(
        "UnixTimestamp",
        "Long",
        "Unix time - **number of seconds** that have elapsed since 00:00:00 UTC on 1 January 1970.",
    ) { _, field ->
        field.serialName in setOf(
            "date",
            "last_error_date",
            "last_synchronization_error_date",
            "emoji_status_expiration_date",
            "forward_date",
            "edit_date",
            "close_date",
            "start_date",
            "expire_date",
            "until_date",
            "file_date",
        )
    },
    ValueType("TelegramPaymentChargeId", "String", "Telegram payment identifier") { _, field ->
        field.serialName.endsWith("telegram_payment_charge_id")
    },
    ValueType("BusinessConnectionId", "String", "Business connection identifier") { type, field ->
        (type.value == "BusinessConnection" && field.serialName == "id") || field.serialName.endsWith("business_connection_id")
    },
)

val unionMarkerValueInDescriptionRe = Regex("""(?:must be|always) ["“”]?([\w_]+)["“”]?""")

private val unionMarkerFieldNames = setOf("type", "status", "source")

fun BotApiElement.Field.isUnionDiscriminator(): Boolean {
    return serialName in unionMarkerFieldNames
}

fun BotApiElement.getUnionDiscriminatorFieldName(): String? {
    return fields?.find { it.isUnionDiscriminator() }?.serialName
}

fun BotApiElementName.asMethodNameToRequestTypeName() = "${value.toTitleCase()}Request"

data class FluentContextMethod(
    val receiver: String,
    val name: String,
    val delegateName: String,
    val args: Map<String, String>
) {
    constructor(receiver: String, name: String, args: Map<String, String>) : this(receiver, name, name, args)
}

/**
 * Fluent methods move some of the parameters from the base method signatures
 * into a direct receiver of the function, thus making it more idiomatic.
 * The ability to call a base method is retained by having `TelegramBotApiContext` as a context receiver.
 *
 * Fluent methods generation happens after the [methodVariations] are expanded.
 */
val fluentMethods = listOf(
    FluentContextMethod("Chat", "sendMessage", "sendMessage", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendMarkdown", "sendMessage", mapOf("chatId" to "id", "parseMode" to "ParseMode.MARKDOWN")),
    FluentContextMethod("Chat", "sendMarkdownV2", "sendMessage", mapOf("chatId" to "id", "parseMode" to "ParseMode.MARKDOWN_V2")),
    FluentContextMethod("Chat", "sendHtml", "sendMessage", mapOf("chatId" to "id", "parseMode" to "ParseMode.HTML")),
    FluentContextMethod("ChatId", "sendMessage", "sendMessage", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendMarkdown", "sendMessage", mapOf("chatId" to "this", "parseMode" to "ParseMode.MARKDOWN")),
    FluentContextMethod("ChatId", "sendMarkdownV2", "sendMessage", mapOf("chatId" to "this", "parseMode" to "ParseMode.MARKDOWN_V2")),
    FluentContextMethod("ChatId", "sendHtml", "sendMessage", mapOf("chatId" to "this", "parseMode" to "ParseMode.HTML")),

    FluentContextMethod("Message", "reply", "sendMessage", mapOf("chatId" to "chat.id", "replyParameters" to "ReplyParameters(messageId)")),
    FluentContextMethod(
        "Message", "replyMarkdown",
        "sendMessage", mapOf("chatId" to "chat.id", "replyParameters" to "ReplyParameters(messageId)", "parseMode" to "ParseMode.MARKDOWN")
    ),
    FluentContextMethod(
        "Message", "replyMarkdownV2",
        "sendMessage", mapOf("chatId" to "chat.id", "replyParameters" to "ReplyParameters(messageId)", "parseMode" to "ParseMode.MARKDOWN_V2")
    ),
    FluentContextMethod(
        "Message", "replyHtml",
        "sendMessage", mapOf("chatId" to "chat.id", "replyParameters" to "ReplyParameters(messageId)", "parseMode" to "ParseMode.HTML")
    ),

    FluentContextMethod(
        "Message", "editText",
        "editMessageText", mapOf("chatId" to "chat.id", "messageId" to "messageId")
    ),
    FluentContextMethod(
        "Message", "editTextMarkdown",
        "editMessageText",
        mapOf("chatId" to "chat.id", "messageId" to "messageId", "parseMode" to "ParseMode.MARKDOWN")
    ),
    FluentContextMethod(
        "Message", "editTextMarkdownV2",
        "editMessageText",
        mapOf("chatId" to "chat.id", "messageId" to "messageId", "parseMode" to "ParseMode.MARKDOWN_V2")
    ),
    FluentContextMethod(
        "Message", "editTextHtml",
        "editMessageText",
        mapOf("chatId" to "chat.id", "messageId" to "messageId", "parseMode" to "ParseMode.HTML")
    ),

    FluentContextMethod("Message", "delete", "deleteMessage", mapOf("chatId" to "chat.id", "messageId" to "messageId")),
    FluentContextMethod("Message", "forward", "forwardMessage", mapOf("fromChatId" to "chat.id", "messageId" to "messageId")),
    FluentContextMethod("Message", "copyMessage", "copyMessage", mapOf("fromChatId" to "chat.id", "messageId" to "messageId")),

    FluentContextMethod("Chat", "sendPhoto", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendAudio", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendDocument", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVideo", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendAnimation", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVoice", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVideoNote", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendMediaGroup", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendLocation", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendVenue", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendContact", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendPoll", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendDice", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "sendChatAction", mapOf("chatId" to "id")),

    FluentContextMethod("Chat", "getMemberCount", "getChatMemberCount", mapOf("chatId" to "id")),
    FluentContextMethod("Chat", "getMember", "getChatMember", mapOf("chatId" to "id")),

    FluentContextMethod("ChatId", "sendPhoto", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendAudio", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendDocument", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVideo", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendAnimation", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVoice", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVideoNote", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendMediaGroup", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendLocation", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendVenue", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendContact", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendPoll", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendDice", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "sendChatAction", mapOf("chatId" to "this")),

    FluentContextMethod("ChatId", "getMemberCount", "getChatMemberCount", mapOf("chatId" to "this")),
    FluentContextMethod("ChatId", "getMember", "getChatMember", mapOf("chatId" to "this")),

    FluentContextMethod("User", "getProfilePhotos", mapOf("userId" to "id")),

    FluentContextMethod("UserId", "getProfilePhotos", mapOf("userId" to "this")),

    FluentContextMethod("CallbackQuery", "answer", "answerCallbackQuery", mapOf("callbackQueryId" to "id")),
    FluentContextMethod("CallbackQueryId", "answer", "answerCallbackQuery", mapOf("callbackQueryId" to "this")),

    FluentContextMethod("InlineQuery", "answer", "answerInlineQuery", mapOf("inlineQueryId" to "id")),
    FluentContextMethod("InlineQueryId", "answer", "answerInlineQuery", mapOf("inlineQueryId" to "this")),
)

data class UpdateListenerEntry(
    val typeWithoutUpdate: String,
    val field: String = typeWithoutUpdate.toUntitleCase(),
    val fieldType: String = typeWithoutUpdate,
)

val updateListenerEntries = listOf(
    UpdateListenerEntry("Message"),
    UpdateListenerEntry("EditedMessage", fieldType = "Message"),
    UpdateListenerEntry("ChannelPost", fieldType = "Message"),
    UpdateListenerEntry("EditedChannelPost", fieldType = "Message"),
    UpdateListenerEntry("InlineQuery"),
    UpdateListenerEntry("ChosenInlineResult"),
    UpdateListenerEntry("CallbackQuery"),
    UpdateListenerEntry("ShippingQuery"),
    UpdateListenerEntry("PreCheckoutQuery"),
    UpdateListenerEntry("Poll"),
    UpdateListenerEntry("PollAnswer"),
    UpdateListenerEntry("MyChatMember", fieldType = "ChatMemberUpdated"),
    UpdateListenerEntry("ChatMember", fieldType = "ChatMemberUpdated"),
    UpdateListenerEntry("ChatJoinRequest"),
    UpdateListenerEntry("BusinessConnection"),
    UpdateListenerEntry("BusinessMessage", fieldType = "Message"),
    UpdateListenerEntry("DeletedBusinessMessages", fieldType = "BusinessMessagesDeleted"),
    UpdateListenerEntry("EditedBusinessMessage", fieldType = "Message"),
    UpdateListenerEntry("MessageReactionCount", fieldType = "MessageReactionCountUpdated"),
    UpdateListenerEntry("MessageReaction", fieldType = "MessageReactionUpdated"),
    UpdateListenerEntry("ChatBoost", fieldType = "ChatBoostUpdated"),
    UpdateListenerEntry("RemovedChatBoost", fieldType = "ChatBoostRemoved"),
)
