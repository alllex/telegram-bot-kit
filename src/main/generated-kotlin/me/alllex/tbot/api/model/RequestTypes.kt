package me.alllex.tbot.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString


/**
 * Request body for [getUpdates].
 *
 * @param offset Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue. All previous updates will be forgotten.
 * @param limit Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 * @param timeout Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling should be used for testing purposes only.
 * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify ["message", "edited_channel_post", "callback_query"] to only receive updates of these types. See Update for a complete list of available update types. Specify an empty list to receive all update types except chat_member, message_reaction, and message_reaction_count (default). If not specified, the previous setting will be used. Please note that this parameter doesn't affect updates created before the call to the getUpdates, so unwanted updates may be received for a short period of time.
 */
@Serializable
data class GetUpdatesRequest(
    val offset: Long? = null,
    val limit: Long? = null,
    val timeout: Seconds? = null,
    val allowedUpdates: List<UpdateType>? = null,
) {
    override fun toString() = DebugStringBuilder("GetUpdatesRequest").prop("offset", offset).prop("limit", limit).prop("timeout", timeout).prop("allowedUpdates", allowedUpdates).toString()
}

/**
 * Request body for [setWebhook].
 *
 * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration
 * @param certificate Upload your public key certificate so that the root certificate in use can be checked. See our self-signed guide for details.
 * @param ipAddress The fixed IP address which will be used to send webhook requests instead of the IP address resolved through DNS
 * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to 40. Use lower values to limit the load on your bot's server, and higher values to increase your bot's throughput.
 * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive. For example, specify ["message", "edited_channel_post", "callback_query"] to only receive updates of these types. See Update for a complete list of available update types. Specify an empty list to receive all update types except chat_member, message_reaction, and message_reaction_count (default). If not specified, the previous setting will be used. Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted updates may be received for a short period of time.
 * @param dropPendingUpdates Pass True to drop all pending updates
 * @param secretToken A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook request, 1-256 characters. Only characters A-Z, a-z, 0-9, _ and - are allowed. The header is useful to ensure that the request comes from a webhook set by you.
 */
@Serializable
data class SetWebhookRequest(
    val url: String,
    val certificate: String? = null,
    val ipAddress: String? = null,
    val maxConnections: Long? = null,
    val allowedUpdates: List<UpdateType>? = null,
    val dropPendingUpdates: Boolean? = null,
    val secretToken: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetWebhookRequest").prop("url", url).prop("certificate", certificate).prop("ipAddress", ipAddress).prop("maxConnections", maxConnections).prop("allowedUpdates", allowedUpdates).prop("dropPendingUpdates", dropPendingUpdates).prop("secretToken", secretToken).toString()
}

/**
 * Request body for [deleteWebhook].
 *
 * @param dropPendingUpdates Pass True to drop all pending updates
 */
@Serializable
data class DeleteWebhookRequest(
    val dropPendingUpdates: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("DeleteWebhookRequest").prop("dropPendingUpdates", dropPendingUpdates).toString()
}

/**
 * Request body for [sendMessage].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param text Text of the message to be sent, 1-4096 characters after entities parsing
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @param linkPreviewOptions Link preview generation options for the message
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendMessageRequest(
    val chatId: ChatId,
    val text: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendMessageRequest").prop("chatId", chatId).prop("text", text).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("parseMode", parseMode).prop("entities", entities).prop("linkPreviewOptions", linkPreviewOptions).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [forwardMessage].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in from_chat_id
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded message from forwarding and saving
 */
@Serializable
data class ForwardMessageRequest(
    val chatId: ChatId,
    val fromChatId: ChatId,
    val messageId: MessageId,
    val messageThreadId: MessageThreadId? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ForwardMessageRequest").prop("chatId", chatId).prop("fromChatId", fromChatId).prop("messageId", messageId).prop("messageThreadId", messageThreadId).prop("disableNotification", disableNotification).prop("protectContent", protectContent).toString()
}

/**
 * Request body for [forwardMessages].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original messages were sent (or channel username in the format @channelusername)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat from_chat_id to forward. The identifiers must be specified in a strictly increasing order.
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param disableNotification Sends the messages silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the forwarded messages from forwarding and saving
 */
@Serializable
data class ForwardMessagesRequest(
    val chatId: ChatId,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val messageThreadId: MessageThreadId? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ForwardMessagesRequest").prop("chatId", chatId).prop("fromChatId", fromChatId).prop("messageIds", messageIds).prop("messageThreadId", messageThreadId).prop("disableNotification", disableNotification).prop("protectContent", protectContent).toString()
}

/**
 * Request body for [copyMessage].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
 * @param messageId Message identifier in the chat specified in from_chat_id
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param caption New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept
 * @param parseMode Mode for parsing entities in the new caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of parse_mode
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
 */
@Serializable
data class CopyMessageRequest(
    val chatId: ChatId,
    val fromChatId: ChatId,
    val messageId: MessageId,
    val messageThreadId: MessageThreadId? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("CopyMessageRequest").prop("chatId", chatId).prop("fromChatId", fromChatId).prop("messageId", messageId).prop("messageThreadId", messageThreadId).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [copyMessages].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param fromChatId Unique identifier for the chat where the original messages were sent (or channel username in the format @channelusername)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages in the chat from_chat_id to copy. The identifiers must be specified in a strictly increasing order.
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param disableNotification Sends the messages silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent messages from forwarding and saving
 * @param removeCaption Pass True to copy the messages without their captions
 */
@Serializable
data class CopyMessagesRequest(
    val chatId: ChatId,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val messageThreadId: MessageThreadId? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val removeCaption: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("CopyMessagesRequest").prop("chatId", chatId).prop("fromChatId", fromChatId).prop("messageIds", messageIds).prop("messageThreadId", messageThreadId).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("removeCaption", removeCaption).toString()
}

/**
 * Request body for [sendPhoto].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param photo Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data. The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20. More information on Sending Files »
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param caption Photo caption (may also be used when resending photos by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Pass True if the photo needs to be covered with a spoiler animation
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendPhotoRequest(
    val chatId: ChatId,
    val photo: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val hasSpoiler: Boolean? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendPhotoRequest").prop("chatId", chatId).prop("photo", photo).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("hasSpoiler", hasSpoiler).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendAudio].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param audio Audio file to send. Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a new one using multipart/form-data. More information on Sending Files »
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param caption Audio caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the audio caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param duration Duration of the audio in seconds
 * @param performer Performer
 * @param title Track name
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendAudioRequest(
    val chatId: ChatId,
    val audio: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Seconds? = null,
    val performer: String? = null,
    val title: String? = null,
    val thumbnail: String? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendAudioRequest").prop("chatId", chatId).prop("audio", audio).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("duration", duration).prop("performer", performer).prop("title", title).prop("thumbnail", thumbnail).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendDocument].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param document File to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More information on Sending Files »
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Document caption (may also be used when resending documents by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the document caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param disableContentTypeDetection Disables automatic server-side content type detection for files uploaded using multipart/form-data
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendDocumentRequest(
    val chatId: ChatId,
    val document: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendDocumentRequest").prop("chatId", chatId).prop("document", document).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("disableContentTypeDetection", disableContentTypeDetection).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendVideo].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param video Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data. More information on Sending Files »
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param duration Duration of sent video in seconds
 * @param width Video width
 * @param height Video height
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Video caption (may also be used when resending videos by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Pass True if the video needs to be covered with a spoiler animation
 * @param supportsStreaming Pass True if the uploaded video is suitable for streaming
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendVideoRequest(
    val chatId: ChatId,
    val video: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val duration: Seconds? = null,
    val width: Long? = null,
    val height: Long? = null,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val hasSpoiler: Boolean? = null,
    val supportsStreaming: Boolean? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendVideoRequest").prop("chatId", chatId).prop("video", video).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("duration", duration).prop("width", width).prop("height", height).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("hasSpoiler", hasSpoiler).prop("supportsStreaming", supportsStreaming).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendAnimation].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param animation Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. More information on Sending Files »
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param duration Duration of sent animation in seconds
 * @param width Animation width
 * @param height Animation height
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Animation caption (may also be used when resending animation by file_id), 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the animation caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Pass True if the animation needs to be covered with a spoiler animation
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendAnimationRequest(
    val chatId: ChatId,
    val animation: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val duration: Seconds? = null,
    val width: Long? = null,
    val height: Long? = null,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val hasSpoiler: Boolean? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendAnimationRequest").prop("chatId", chatId).prop("animation", animation).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("duration", duration).prop("width", width).prop("height", height).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("hasSpoiler", hasSpoiler).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendVoice].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param voice Audio file to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More information on Sending Files »
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param caption Voice message caption, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param duration Duration of the voice message in seconds
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendVoiceRequest(
    val chatId: ChatId,
    val voice: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Seconds? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendVoiceRequest").prop("chatId", chatId).prop("voice", voice).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("duration", duration).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendVideoNote].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param videoNote Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers (recommended) or upload a new video using multipart/form-data. More information on Sending Files ». Sending video notes by a URL is currently unsupported
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param duration Duration of sent video in seconds
 * @param length Video width and height, i.e. diameter of the video message
 * @param thumbnail Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendVideoNoteRequest(
    val chatId: ChatId,
    val videoNote: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val duration: Seconds? = null,
    val length: Long? = null,
    val thumbnail: String? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendVideoNoteRequest").prop("chatId", chatId).prop("videoNote", videoNote).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("duration", duration).prop("length", length).prop("thumbnail", thumbnail).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendMediaGroup].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param media A JSON-serialized array describing messages to be sent, must include 2-10 items
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param disableNotification Sends messages silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent messages from forwarding and saving
 * @param replyParameters Description of the message to reply to
 */
@Serializable
data class SendMediaGroupRequest(
    val chatId: ChatId,
    val media: List<InputMedia>,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
) {
    override fun toString() = DebugStringBuilder("SendMediaGroupRequest").prop("chatId", chatId).prop("media", media).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).toString()
}

/**
 * Request body for [sendLocation].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Period in seconds for which the location will be updated (see Live Locations, should be between 60 and 86400.
 * @param heading For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendLocationRequest(
    val chatId: ChatId,
    val latitude: Double,
    val longitude: Double,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendLocationRequest").prop("chatId", chatId).prop("latitude", latitude).prop("longitude", longitude).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("horizontalAccuracy", horizontalAccuracy).prop("livePeriod", livePeriod).prop("heading", heading).prop("proximityAlertRadius", proximityAlertRadius).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendVenue].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param latitude Latitude of the venue
 * @param longitude Longitude of the venue
 * @param title Name of the venue
 * @param address Address of the venue
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param foursquareId Foursquare identifier of the venue
 * @param foursquareType Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
 * @param googlePlaceId Google Places identifier of the venue
 * @param googlePlaceType Google Places type of the venue. (See supported types.)
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendVenueRequest(
    val chatId: ChatId,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendVenueRequest").prop("chatId", chatId).prop("latitude", latitude).prop("longitude", longitude).prop("title", title).prop("address", address).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("foursquareId", foursquareId).prop("foursquareType", foursquareType).prop("googlePlaceId", googlePlaceId).prop("googlePlaceType", googlePlaceType).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendContact].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param lastName Contact's last name
 * @param vcard Additional data about the contact in the form of a vCard, 0-2048 bytes
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendContactRequest(
    val chatId: ChatId,
    val phoneNumber: String,
    val firstName: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val lastName: String? = null,
    val vcard: String? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendContactRequest").prop("chatId", chatId).prop("phoneNumber", phoneNumber).prop("firstName", firstName).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("lastName", lastName).prop("vcard", vcard).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendPoll].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param question Poll question, 1-300 characters
 * @param options A JSON-serialized list of answer options, 2-10 strings 1-100 characters each
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param isAnonymous True, if the poll needs to be anonymous, defaults to True
 * @param type Poll type, “quiz” or “regular”, defaults to “regular”
 * @param allowsMultipleAnswers True, if the poll allows multiple answers, ignored for polls in quiz mode, defaults to False
 * @param correctOptionId 0-based identifier of the correct answer option, required for polls in quiz mode
 * @param explanation Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters with at most 2 line feeds after entities parsing
 * @param explanationParseMode Mode for parsing entities in the explanation. See formatting options for more details.
 * @param explanationEntities A JSON-serialized list of special entities that appear in the poll explanation, which can be specified instead of parse_mode
 * @param openPeriod Amount of time in seconds the poll will be active after creation, 5-600. Can't be used together with close_date.
 * @param closeDate Point in time (Unix timestamp) when the poll will be automatically closed. Must be at least 5 and no more than 600 seconds in the future. Can't be used together with open_period.
 * @param isClosed Pass True if the poll needs to be immediately closed. This can be useful for poll preview.
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendPollRequest(
    val chatId: ChatId,
    val question: String,
    val options: List<String>,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val isAnonymous: Boolean? = null,
    val type: String? = null,
    val allowsMultipleAnswers: Boolean? = null,
    val correctOptionId: Long? = null,
    val explanation: String? = null,
    val explanationParseMode: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Seconds? = null,
    val closeDate: UnixTimestamp? = null,
    val isClosed: Boolean? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendPollRequest").prop("chatId", chatId).prop("question", question).prop("options", options).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("isAnonymous", isAnonymous).prop("type", type).prop("allowsMultipleAnswers", allowsMultipleAnswers).prop("correctOptionId", correctOptionId).prop("explanation", explanation).prop("explanationParseMode", explanationParseMode).prop("explanationEntities", explanationEntities).prop("openPeriod", openPeriod).prop("closeDate", closeDate).prop("isClosed", isClosed).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendDice].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param emoji Emoji on which the dice throw animation is based. Currently, must be one of “”, “”, “”, “”, “”, or “”. Dice can have values 1-6 for “”, “” and “”, values 1-5 for “” and “”, and values 1-64 for “”. Defaults to “”
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove a reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account
 */
@Serializable
data class SendDiceRequest(
    val chatId: ChatId,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val emoji: String? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendDiceRequest").prop("chatId", chatId).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("emoji", emoji).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [sendChatAction].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param action Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text messages, upload_photo for photos, record_video or upload_video for videos, record_voice or upload_voice for voice notes, upload_document for general files, choose_sticker for stickers, find_location for location data, record_video_note or upload_video_note for video notes.
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the action will be sent
 * @param messageThreadId Unique identifier for the target message thread; for supergroups only
 */
@Serializable
data class SendChatActionRequest(
    val chatId: ChatId,
    val action: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
) {
    override fun toString() = DebugStringBuilder("SendChatActionRequest").prop("chatId", chatId).prop("action", action).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).toString()
}

/**
 * Request body for [setMessageReaction].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of the target message. If the message belongs to a media group, the reaction is set to the first non-deleted message in the group instead.
 * @param reaction A JSON-serialized list of reaction types to set on the message. Currently, as non-premium users, bots can set up to one reaction per message. A custom emoji reaction can be used if it is either already present on the message or explicitly allowed by chat administrators.
 * @param isBig Pass True to set the reaction with a big animation
 */
@Serializable
data class SetMessageReactionRequest(
    val chatId: ChatId,
    val messageId: MessageId,
    val reaction: List<ReactionType>? = null,
    val isBig: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("SetMessageReactionRequest").prop("chatId", chatId).prop("messageId", messageId).prop("reaction", reaction).prop("isBig", isBig).toString()
}

/**
 * Request body for [getUserProfilePhotos].
 *
 * @param userId Unique identifier of the target user
 * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
 * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
 */
@Serializable
data class GetUserProfilePhotosRequest(
    val userId: UserId,
    val offset: Long? = null,
    val limit: Long? = null,
) {
    override fun toString() = DebugStringBuilder("GetUserProfilePhotosRequest").prop("userId", userId).prop("offset", offset).prop("limit", limit).toString()
}

/**
 * Request body for [getFile].
 *
 * @param fileId File identifier to get information about
 */
@Serializable
data class GetFileRequest(
    val fileId: FileId,
) {
    override fun toString() = DebugStringBuilder("GetFileRequest").prop("fileId", fileId).toString()
}

/**
 * Request body for [banChatMember].
 *
 * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @param untilDate Date when the user will be unbanned; Unix time. If user is banned for more than 366 days or less than 30 seconds from the current time they are considered to be banned forever. Applied for supergroups and channels only.
 * @param revokeMessages Pass True to delete all messages from the chat for the user that is being removed. If False, the user will be able to see messages in the group that were sent before the user was removed. Always True for supergroups and channels.
 */
@Serializable
data class BanChatMemberRequest(
    val chatId: ChatId,
    val userId: UserId,
    val untilDate: UnixTimestamp? = null,
    val revokeMessages: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("BanChatMemberRequest").prop("chatId", chatId).prop("userId", userId).prop("untilDate", untilDate).prop("revokeMessages", revokeMessages).toString()
}

/**
 * Request body for [unbanChatMember].
 *
 * @param chatId Unique identifier for the target group or username of the target supergroup or channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @param onlyIfBanned Do nothing if the user is not banned
 */
@Serializable
data class UnbanChatMemberRequest(
    val chatId: ChatId,
    val userId: UserId,
    val onlyIfBanned: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("UnbanChatMemberRequest").prop("chatId", chatId).prop("userId", userId).prop("onlyIfBanned", onlyIfBanned).toString()
}

/**
 * Request body for [restrictChatMember].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param userId Unique identifier of the target user
 * @param permissions A JSON-serialized object for new user permissions
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 * @param untilDate Date when restrictions will be lifted for the user; Unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
 */
@Serializable
data class RestrictChatMemberRequest(
    val chatId: ChatId,
    val userId: UserId,
    val permissions: ChatPermissions,
    val useIndependentChatPermissions: Boolean? = null,
    val untilDate: UnixTimestamp? = null,
) {
    override fun toString() = DebugStringBuilder("RestrictChatMemberRequest").prop("chatId", chatId).prop("userId", userId).prop("permissions", permissions).prop("useIndependentChatPermissions", useIndependentChatPermissions).prop("untilDate", untilDate).toString()
}

/**
 * Request body for [promoteChatMember].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 * @param isAnonymous Pass True if the administrator's presence in the chat is hidden
 * @param canManageChat Pass True if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
 * @param canDeleteMessages Pass True if the administrator can delete messages of other users
 * @param canManageVideoChats Pass True if the administrator can manage video chats
 * @param canRestrictMembers Pass True if the administrator can restrict, ban or unban chat members, or access supergroup statistics
 * @param canPromoteMembers Pass True if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by him)
 * @param canChangeInfo Pass True if the administrator can change chat title, photo and other settings
 * @param canInviteUsers Pass True if the administrator can invite new users to the chat
 * @param canPostStories Pass True if the administrator can post stories to the chat
 * @param canEditStories Pass True if the administrator can edit stories posted by other users
 * @param canDeleteStories Pass True if the administrator can delete stories posted by other users
 * @param canPostMessages Pass True if the administrator can post messages in the channel, or access channel statistics; for channels only
 * @param canEditMessages Pass True if the administrator can edit messages of other users and can pin messages; for channels only
 * @param canPinMessages Pass True if the administrator can pin messages; for supergroups only
 * @param canManageTopics Pass True if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
 */
@Serializable
data class PromoteChatMemberRequest(
    val chatId: ChatId,
    val userId: UserId,
    val isAnonymous: Boolean? = null,
    val canManageChat: Boolean? = null,
    val canDeleteMessages: Boolean? = null,
    val canManageVideoChats: Boolean? = null,
    val canRestrictMembers: Boolean? = null,
    val canPromoteMembers: Boolean? = null,
    val canChangeInfo: Boolean? = null,
    val canInviteUsers: Boolean? = null,
    val canPostStories: Boolean? = null,
    val canEditStories: Boolean? = null,
    val canDeleteStories: Boolean? = null,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("PromoteChatMemberRequest").prop("chatId", chatId).prop("userId", userId).prop("isAnonymous", isAnonymous).prop("canManageChat", canManageChat).prop("canDeleteMessages", canDeleteMessages).prop("canManageVideoChats", canManageVideoChats).prop("canRestrictMembers", canRestrictMembers).prop("canPromoteMembers", canPromoteMembers).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPostStories", canPostStories).prop("canEditStories", canEditStories).prop("canDeleteStories", canDeleteStories).prop("canPostMessages", canPostMessages).prop("canEditMessages", canEditMessages).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).toString()
}

/**
 * Request body for [setChatAdministratorCustomTitle].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param userId Unique identifier of the target user
 * @param customTitle New custom title for the administrator; 0-16 characters, emoji are not allowed
 */
@Serializable
data class SetChatAdministratorCustomTitleRequest(
    val chatId: ChatId,
    val userId: UserId,
    val customTitle: String,
) {
    override fun toString() = DebugStringBuilder("SetChatAdministratorCustomTitleRequest").prop("chatId", chatId).prop("userId", userId).prop("customTitle", customTitle).toString()
}

/**
 * Request body for [banChatSenderChat].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param senderChatId Unique identifier of the target sender chat
 */
@Serializable
data class BanChatSenderChatRequest(
    val chatId: ChatId,
    val senderChatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("BanChatSenderChatRequest").prop("chatId", chatId).prop("senderChatId", senderChatId).toString()
}

/**
 * Request body for [unbanChatSenderChat].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param senderChatId Unique identifier of the target sender chat
 */
@Serializable
data class UnbanChatSenderChatRequest(
    val chatId: ChatId,
    val senderChatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("UnbanChatSenderChatRequest").prop("chatId", chatId).prop("senderChatId", senderChatId).toString()
}

/**
 * Request body for [setChatPermissions].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param permissions A JSON-serialized object for new default chat permissions
 * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages, can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions; the can_send_polls permission will imply the can_send_messages permission.
 */
@Serializable
data class SetChatPermissionsRequest(
    val chatId: ChatId,
    val permissions: ChatPermissions,
    val useIndependentChatPermissions: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("SetChatPermissionsRequest").prop("chatId", chatId).prop("permissions", permissions).prop("useIndependentChatPermissions", useIndependentChatPermissions).toString()
}

/**
 * Request body for [exportChatInviteLink].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 */
@Serializable
data class ExportChatInviteLinkRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("ExportChatInviteLinkRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [createChatInviteLink].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param name Invite link name; 0-32 characters
 * @param expireDate Point in time (Unix timestamp) when the link will expire
 * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit can't be specified
 */
@Serializable
data class CreateChatInviteLinkRequest(
    val chatId: ChatId,
    val name: String? = null,
    val expireDate: UnixTimestamp? = null,
    val memberLimit: Long? = null,
    val createsJoinRequest: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("CreateChatInviteLinkRequest").prop("chatId", chatId).prop("name", name).prop("expireDate", expireDate).prop("memberLimit", memberLimit).prop("createsJoinRequest", createsJoinRequest).toString()
}

/**
 * Request body for [editChatInviteLink].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param inviteLink The invite link to edit
 * @param name Invite link name; 0-32 characters
 * @param expireDate Point in time (Unix timestamp) when the link will expire
 * @param memberLimit The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators. If True, member_limit can't be specified
 */
@Serializable
data class EditChatInviteLinkRequest(
    val chatId: ChatId,
    val inviteLink: String,
    val name: String? = null,
    val expireDate: UnixTimestamp? = null,
    val memberLimit: Long? = null,
    val createsJoinRequest: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("EditChatInviteLinkRequest").prop("chatId", chatId).prop("inviteLink", inviteLink).prop("name", name).prop("expireDate", expireDate).prop("memberLimit", memberLimit).prop("createsJoinRequest", createsJoinRequest).toString()
}

/**
 * Request body for [revokeChatInviteLink].
 *
 * @param chatId Unique identifier of the target chat or username of the target channel (in the format @channelusername)
 * @param inviteLink The invite link to revoke
 */
@Serializable
data class RevokeChatInviteLinkRequest(
    val chatId: ChatId,
    val inviteLink: String,
) {
    override fun toString() = DebugStringBuilder("RevokeChatInviteLinkRequest").prop("chatId", chatId).prop("inviteLink", inviteLink).toString()
}

/**
 * Request body for [approveChatJoinRequest].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 */
@Serializable
data class ApproveChatJoinRequestRequest(
    val chatId: ChatId,
    val userId: UserId,
) {
    override fun toString() = DebugStringBuilder("ApproveChatJoinRequestRequest").prop("chatId", chatId).prop("userId", userId).toString()
}

/**
 * Request body for [declineChatJoinRequest].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 */
@Serializable
data class DeclineChatJoinRequestRequest(
    val chatId: ChatId,
    val userId: UserId,
) {
    override fun toString() = DebugStringBuilder("DeclineChatJoinRequestRequest").prop("chatId", chatId).prop("userId", userId).toString()
}

/**
 * Request body for [setChatPhoto].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param photo New chat photo, uploaded using multipart/form-data
 */
@Serializable
data class SetChatPhotoRequest(
    val chatId: ChatId,
    val photo: String,
) {
    override fun toString() = DebugStringBuilder("SetChatPhotoRequest").prop("chatId", chatId).prop("photo", photo).toString()
}

/**
 * Request body for [deleteChatPhoto].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 */
@Serializable
data class DeleteChatPhotoRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("DeleteChatPhotoRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [setChatTitle].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param title New chat title, 1-128 characters
 */
@Serializable
data class SetChatTitleRequest(
    val chatId: ChatId,
    val title: String,
) {
    override fun toString() = DebugStringBuilder("SetChatTitleRequest").prop("chatId", chatId).prop("title", title).toString()
}

/**
 * Request body for [setChatDescription].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param description New chat description, 0-255 characters
 */
@Serializable
data class SetChatDescriptionRequest(
    val chatId: ChatId,
    val description: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetChatDescriptionRequest").prop("chatId", chatId).prop("description", description).toString()
}

/**
 * Request body for [pinChatMessage].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of a message to pin
 * @param disableNotification Pass True if it is not necessary to send a notification to all chat members about the new pinned message. Notifications are always disabled in channels and private chats.
 */
@Serializable
data class PinChatMessageRequest(
    val chatId: ChatId,
    val messageId: MessageId,
    val disableNotification: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("PinChatMessageRequest").prop("chatId", chatId).prop("messageId", messageId).prop("disableNotification", disableNotification).toString()
}

/**
 * Request body for [unpinChatMessage].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of a message to unpin. If not specified, the most recent pinned message (by sending date) will be unpinned.
 */
@Serializable
data class UnpinChatMessageRequest(
    val chatId: ChatId,
    val messageId: MessageId? = null,
) {
    override fun toString() = DebugStringBuilder("UnpinChatMessageRequest").prop("chatId", chatId).prop("messageId", messageId).toString()
}

/**
 * Request body for [unpinAllChatMessages].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 */
@Serializable
data class UnpinAllChatMessagesRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("UnpinAllChatMessagesRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [leaveChat].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 */
@Serializable
data class LeaveChatRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("LeaveChatRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [getChat].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 */
@Serializable
data class GetChatRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("GetChatRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [getChatAdministrators].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 */
@Serializable
data class GetChatAdministratorsRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("GetChatAdministratorsRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [getChatMemberCount].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 */
@Serializable
data class GetChatMemberCountRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("GetChatMemberCountRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [getChatMember].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 */
@Serializable
data class GetChatMemberRequest(
    val chatId: ChatId,
    val userId: UserId,
) {
    override fun toString() = DebugStringBuilder("GetChatMemberRequest").prop("chatId", chatId).prop("userId", userId).toString()
}

/**
 * Request body for [setChatStickerSet].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param stickerSetName Name of the sticker set to be set as the group sticker set
 */
@Serializable
data class SetChatStickerSetRequest(
    val chatId: ChatId,
    val stickerSetName: String,
) {
    override fun toString() = DebugStringBuilder("SetChatStickerSetRequest").prop("chatId", chatId).prop("stickerSetName", stickerSetName).toString()
}

/**
 * Request body for [deleteChatStickerSet].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class DeleteChatStickerSetRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("DeleteChatStickerSetRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [createForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param name Topic name, 1-128 characters
 * @param iconColor Color of the topic icon in RGB format. Currently, must be one of 7322096 (0x6FB9F0), 16766590 (0xFFD67E), 13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2), or 16478047 (0xFB6F5F)
 * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all allowed custom emoji identifiers.
 */
@Serializable
data class CreateForumTopicRequest(
    val chatId: ChatId,
    val name: String,
    val iconColor: Long? = null,
    val iconCustomEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("CreateForumTopicRequest").prop("chatId", chatId).prop("name", name).prop("iconColor", iconColor).prop("iconCustomEmojiId", iconCustomEmojiId).toString()
}

/**
 * Request body for [editForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 * @param name New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
 * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon. Use getForumTopicIconStickers to get all allowed custom emoji identifiers. Pass an empty string to remove the icon. If not specified, the current icon will be kept
 */
@Serializable
data class EditForumTopicRequest(
    val chatId: ChatId,
    val messageThreadId: MessageThreadId,
    val name: String? = null,
    val iconCustomEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("EditForumTopicRequest").prop("chatId", chatId).prop("messageThreadId", messageThreadId).prop("name", name).prop("iconCustomEmojiId", iconCustomEmojiId).toString()
}

/**
 * Request body for [closeForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
@Serializable
data class CloseForumTopicRequest(
    val chatId: ChatId,
    val messageThreadId: MessageThreadId,
) {
    override fun toString() = DebugStringBuilder("CloseForumTopicRequest").prop("chatId", chatId).prop("messageThreadId", messageThreadId).toString()
}

/**
 * Request body for [reopenForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
@Serializable
data class ReopenForumTopicRequest(
    val chatId: ChatId,
    val messageThreadId: MessageThreadId,
) {
    override fun toString() = DebugStringBuilder("ReopenForumTopicRequest").prop("chatId", chatId).prop("messageThreadId", messageThreadId).toString()
}

/**
 * Request body for [deleteForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
@Serializable
data class DeleteForumTopicRequest(
    val chatId: ChatId,
    val messageThreadId: MessageThreadId,
) {
    override fun toString() = DebugStringBuilder("DeleteForumTopicRequest").prop("chatId", chatId).prop("messageThreadId", messageThreadId).toString()
}

/**
 * Request body for [unpinAllForumTopicMessages].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param messageThreadId Unique identifier for the target message thread of the forum topic
 */
@Serializable
data class UnpinAllForumTopicMessagesRequest(
    val chatId: ChatId,
    val messageThreadId: MessageThreadId,
) {
    override fun toString() = DebugStringBuilder("UnpinAllForumTopicMessagesRequest").prop("chatId", chatId).prop("messageThreadId", messageThreadId).toString()
}

/**
 * Request body for [editGeneralForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param name New topic name, 1-128 characters
 */
@Serializable
data class EditGeneralForumTopicRequest(
    val chatId: ChatId,
    val name: String,
) {
    override fun toString() = DebugStringBuilder("EditGeneralForumTopicRequest").prop("chatId", chatId).prop("name", name).toString()
}

/**
 * Request body for [closeGeneralForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class CloseGeneralForumTopicRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("CloseGeneralForumTopicRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [reopenGeneralForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class ReopenGeneralForumTopicRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("ReopenGeneralForumTopicRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [hideGeneralForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class HideGeneralForumTopicRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("HideGeneralForumTopicRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [unhideGeneralForumTopic].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class UnhideGeneralForumTopicRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("UnhideGeneralForumTopicRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [unpinAllGeneralForumTopicMessages].
 *
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
data class UnpinAllGeneralForumTopicMessagesRequest(
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("UnpinAllGeneralForumTopicMessagesRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [answerCallbackQuery].
 *
 * @param callbackQueryId Unique identifier for the query to be answered
 * @param text Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters
 * @param showAlert If True, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to false.
 * @param url URL that will be opened by the user's client. If you have created a Game and accepted the conditions via @BotFather, specify the URL that opens your game - note that this will only work if the query comes from a callback_game button. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
 * @param cacheTime The maximum amount of time in seconds that the result of the callback query may be cached client-side. Telegram apps will support caching starting in version 3.14. Defaults to 0.
 */
@Serializable
data class AnswerCallbackQueryRequest(
    val callbackQueryId: CallbackQueryId,
    val text: String? = null,
    val showAlert: Boolean? = null,
    val url: String? = null,
    val cacheTime: Seconds? = null,
) {
    override fun toString() = DebugStringBuilder("AnswerCallbackQueryRequest").prop("callbackQueryId", callbackQueryId).prop("text", text).prop("showAlert", showAlert).prop("url", url).prop("cacheTime", cacheTime).toString()
}

/**
 * Request body for [getUserChatBoosts].
 *
 * @param chatId Unique identifier for the chat or username of the channel (in the format @channelusername)
 * @param userId Unique identifier of the target user
 */
@Serializable
data class GetUserChatBoostsRequest(
    val chatId: ChatId,
    val userId: UserId,
) {
    override fun toString() = DebugStringBuilder("GetUserChatBoostsRequest").prop("chatId", chatId).prop("userId", userId).toString()
}

/**
 * Request body for [getBusinessConnection].
 *
 * @param businessConnectionId Unique identifier of the business connection
 */
@Serializable
data class GetBusinessConnectionRequest(
    val businessConnectionId: String,
) {
    override fun toString() = DebugStringBuilder("GetBusinessConnectionRequest").prop("businessConnectionId", businessConnectionId).toString()
}

/**
 * Request body for [setMyCommands].
 *
 * @param commands A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
 * @param scope A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 */
@Serializable
data class SetMyCommandsRequest(
    val commands: List<BotCommand>,
    val scope: BotCommandScope? = null,
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetMyCommandsRequest").prop("commands", commands).prop("scope", scope).prop("languageCode", languageCode).toString()
}

/**
 * Request body for [deleteMyCommands].
 *
 * @param scope A JSON-serialized object, describing scope of users for which the commands are relevant. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 */
@Serializable
data class DeleteMyCommandsRequest(
    val scope: BotCommandScope? = null,
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("DeleteMyCommandsRequest").prop("scope", scope).prop("languageCode", languageCode).toString()
}

/**
 * Request body for [getMyCommands].
 *
 * @param scope A JSON-serialized object, describing scope of users. Defaults to BotCommandScopeDefault.
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 */
@Serializable
data class GetMyCommandsRequest(
    val scope: BotCommandScope? = null,
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("GetMyCommandsRequest").prop("scope", scope).prop("languageCode", languageCode).toString()
}

/**
 * Request body for [setMyName].
 *
 * @param name New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose language there is no dedicated name.
 */
@Serializable
data class SetMyNameRequest(
    val name: String? = null,
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetMyNameRequest").prop("name", name).prop("languageCode", languageCode).toString()
}

/**
 * Request body for [getMyName].
 *
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 */
@Serializable
data class GetMyNameRequest(
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("GetMyNameRequest").prop("languageCode", languageCode).toString()
}

/**
 * Request body for [setMyDescription].
 *
 * @param description New bot description; 0-512 characters. Pass an empty string to remove the dedicated description for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the description will be applied to all users for whose language there is no dedicated description.
 */
@Serializable
data class SetMyDescriptionRequest(
    val description: String? = null,
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetMyDescriptionRequest").prop("description", description).prop("languageCode", languageCode).toString()
}

/**
 * Request body for [getMyDescription].
 *
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 */
@Serializable
data class GetMyDescriptionRequest(
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("GetMyDescriptionRequest").prop("languageCode", languageCode).toString()
}

/**
 * Request body for [setMyShortDescription].
 *
 * @param shortDescription New short description for the bot; 0-120 characters. Pass an empty string to remove the dedicated short description for the given language.
 * @param languageCode A two-letter ISO 639-1 language code. If empty, the short description will be applied to all users for whose language there is no dedicated short description.
 */
@Serializable
data class SetMyShortDescriptionRequest(
    val shortDescription: String? = null,
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetMyShortDescriptionRequest").prop("shortDescription", shortDescription).prop("languageCode", languageCode).toString()
}

/**
 * Request body for [getMyShortDescription].
 *
 * @param languageCode A two-letter ISO 639-1 language code or an empty string
 */
@Serializable
data class GetMyShortDescriptionRequest(
    val languageCode: String? = null,
) {
    override fun toString() = DebugStringBuilder("GetMyShortDescriptionRequest").prop("languageCode", languageCode).toString()
}

/**
 * Request body for [setChatMenuButton].
 *
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
 * @param menuButton A JSON-serialized object for the bot's new menu button. Defaults to MenuButtonDefault
 */
@Serializable
data class SetChatMenuButtonRequest(
    val chatId: ChatId? = null,
    val menuButton: MenuButton? = null,
) {
    override fun toString() = DebugStringBuilder("SetChatMenuButtonRequest").prop("chatId", chatId).prop("menuButton", menuButton).toString()
}

/**
 * Request body for [getChatMenuButton].
 *
 * @param chatId Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
 */
@Serializable
data class GetChatMenuButtonRequest(
    val chatId: ChatId? = null,
) {
    override fun toString() = DebugStringBuilder("GetChatMenuButtonRequest").prop("chatId", chatId).toString()
}

/**
 * Request body for [setMyDefaultAdministratorRights].
 *
 * @param rights A JSON-serialized object describing new default administrator rights. If not specified, the default administrator rights will be cleared.
 * @param forChannels Pass True to change the default administrator rights of the bot in channels. Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
 */
@Serializable
data class SetMyDefaultAdministratorRightsRequest(
    val rights: ChatAdministratorRights? = null,
    val forChannels: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("SetMyDefaultAdministratorRightsRequest").prop("rights", rights).prop("forChannels", forChannels).toString()
}

/**
 * Request body for [getMyDefaultAdministratorRights].
 *
 * @param forChannels Pass True to get default administrator rights of the bot in channels. Otherwise, default administrator rights of the bot for groups and supergroups will be returned.
 */
@Serializable
data class GetMyDefaultAdministratorRightsRequest(
    val forChannels: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("GetMyDefaultAdministratorRightsRequest").prop("forChannels", forChannels).toString()
}

/**
 * Request body for [editMessageText].
 *
 * @param text New text of the message, 1-4096 characters after entities parsing
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param parseMode Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode
 * @param linkPreviewOptions Link preview generation options for the message
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 */
@Serializable
data class EditMessageTextRequest(
    val text: String,
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("EditMessageTextRequest").prop("text", text).prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).prop("parseMode", parseMode).prop("entities", entities).prop("linkPreviewOptions", linkPreviewOptions).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [editMessageCaption].
 *
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param caption New caption of the message, 0-1024 characters after entities parsing
 * @param parseMode Mode for parsing entities in the message caption. See formatting options for more details.
 * @param captionEntities A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 */
@Serializable
data class EditMessageCaptionRequest(
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("EditMessageCaptionRequest").prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [editMessageMedia].
 *
 * @param media A JSON-serialized object for a new media content of the message
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 */
@Serializable
data class EditMessageMediaRequest(
    val media: InputMedia,
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("EditMessageMediaRequest").prop("media", media).prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [editMessageLiveLocation].
 *
 * @param latitude Latitude of new location
 * @param longitude Longitude of new location
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param horizontalAccuracy The radius of uncertainty for the location, measured in meters; 0-1500
 * @param heading Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 */
@Serializable
data class EditMessageLiveLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
    val horizontalAccuracy: Double? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("EditMessageLiveLocationRequest").prop("latitude", latitude).prop("longitude", longitude).prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).prop("horizontalAccuracy", horizontalAccuracy).prop("heading", heading).prop("proximityAlertRadius", proximityAlertRadius).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [stopMessageLiveLocation].
 *
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message with live location to stop
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for a new inline keyboard.
 */
@Serializable
data class StopMessageLiveLocationRequest(
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("StopMessageLiveLocationRequest").prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [editMessageReplyMarkup].
 *
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Required if inline_message_id is not specified. Identifier of the message to edit
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 * @param replyMarkup A JSON-serialized object for an inline keyboard.
 */
@Serializable
data class EditMessageReplyMarkupRequest(
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("EditMessageReplyMarkupRequest").prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [stopPoll].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of the original message with the poll
 * @param replyMarkup A JSON-serialized object for a new message inline keyboard.
 */
@Serializable
data class StopPollRequest(
    val chatId: ChatId,
    val messageId: MessageId,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("StopPollRequest").prop("chatId", chatId).prop("messageId", messageId).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [deleteMessage].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageId Identifier of the message to delete
 */
@Serializable
data class DeleteMessageRequest(
    val chatId: ChatId,
    val messageId: MessageId,
) {
    override fun toString() = DebugStringBuilder("DeleteMessageRequest").prop("chatId", chatId).prop("messageId", messageId).toString()
}

/**
 * Request body for [deleteMessages].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param messageIds A JSON-serialized list of 1-100 identifiers of messages to delete. See deleteMessage for limitations on which messages can be deleted
 */
@Serializable
data class DeleteMessagesRequest(
    val chatId: ChatId,
    val messageIds: List<Long>,
) {
    override fun toString() = DebugStringBuilder("DeleteMessagesRequest").prop("chatId", chatId).prop("messageIds", messageIds).toString()
}

/**
 * Request body for [sendSticker].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param sticker Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a .WEBP sticker from the Internet, or upload a new .WEBP, .TGS, or .WEBM sticker using multipart/form-data. More information on Sending Files ». Video and animated stickers can't be sent via an HTTP URL.
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param emoji Emoji associated with the sticker; only for just uploaded stickers
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. Not supported for messages sent on behalf of a business account.
 */
@Serializable
data class SendStickerRequest(
    val chatId: ChatId,
    val sticker: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val emoji: String? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendStickerRequest").prop("chatId", chatId).prop("sticker", sticker).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("emoji", emoji).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [getStickerSet].
 *
 * @param name Name of the sticker set
 */
@Serializable
data class GetStickerSetRequest(
    val name: String,
) {
    override fun toString() = DebugStringBuilder("GetStickerSetRequest").prop("name", name).toString()
}

/**
 * Request body for [getCustomEmojiStickers].
 *
 * @param customEmojiIds A JSON-serialized list of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
 */
@Serializable
data class GetCustomEmojiStickersRequest(
    val customEmojiIds: List<String>,
) {
    override fun toString() = DebugStringBuilder("GetCustomEmojiStickersRequest").prop("customEmojiIds", customEmojiIds).toString()
}

/**
 * Request body for [uploadStickerFile].
 *
 * @param userId User identifier of sticker file owner
 * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format. See https://core.telegram.org/stickers for technical requirements. More information on Sending Files »
 * @param stickerFormat Format of the sticker, must be one of “static”, “animated”, “video”
 */
@Serializable
data class UploadStickerFileRequest(
    val userId: UserId,
    val sticker: String,
    val stickerFormat: String,
) {
    override fun toString() = DebugStringBuilder("UploadStickerFileRequest").prop("userId", userId).prop("sticker", sticker).prop("stickerFormat", stickerFormat).toString()
}

/**
 * Request body for [createNewStickerSet].
 *
 * @param userId User identifier of created sticker set owner
 * @param name Short name of sticker set, to be used in t.me/addstickers/ URLs (e.g., animals). Can contain only English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in "_by_<bot_username>". <bot_username> is case insensitive. 1-64 characters.
 * @param title Sticker set title, 1-64 characters
 * @param stickers A JSON-serialized list of 1-50 initial stickers to be added to the sticker set
 * @param stickerType Type of stickers in the set, pass “regular”, “mask”, or “custom_emoji”. By default, a regular sticker set is created.
 * @param needsRepainting Pass True if stickers in the sticker set must be repainted to the color of text when used in messages, the accent color if used as emoji status, white on chat photos, or another appropriate color based on context; for custom emoji sticker sets only
 */
@Serializable
data class CreateNewStickerSetRequest(
    val userId: UserId,
    val name: String,
    val title: String,
    val stickers: List<InputSticker>,
    val stickerType: String? = null,
    val needsRepainting: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("CreateNewStickerSetRequest").prop("userId", userId).prop("name", name).prop("title", title).prop("stickers", stickers).prop("stickerType", stickerType).prop("needsRepainting", needsRepainting).toString()
}

/**
 * Request body for [addStickerToSet].
 *
 * @param userId User identifier of sticker set owner
 * @param name Sticker set name
 * @param sticker A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set isn't changed.
 */
@Serializable
data class AddStickerToSetRequest(
    val userId: UserId,
    val name: String,
    val sticker: InputSticker,
) {
    override fun toString() = DebugStringBuilder("AddStickerToSetRequest").prop("userId", userId).prop("name", name).prop("sticker", sticker).toString()
}

/**
 * Request body for [setStickerPositionInSet].
 *
 * @param sticker File identifier of the sticker
 * @param position New sticker position in the set, zero-based
 */
@Serializable
data class SetStickerPositionInSetRequest(
    val sticker: String,
    val position: Long,
) {
    override fun toString() = DebugStringBuilder("SetStickerPositionInSetRequest").prop("sticker", sticker).prop("position", position).toString()
}

/**
 * Request body for [deleteStickerFromSet].
 *
 * @param sticker File identifier of the sticker
 */
@Serializable
data class DeleteStickerFromSetRequest(
    val sticker: String,
) {
    override fun toString() = DebugStringBuilder("DeleteStickerFromSetRequest").prop("sticker", sticker).toString()
}

/**
 * Request body for [replaceStickerInSet].
 *
 * @param userId User identifier of the sticker set owner
 * @param name Sticker set name
 * @param oldSticker File identifier of the replaced sticker
 * @param sticker A JSON-serialized object with information about the added sticker. If exactly the same sticker had already been added to the set, then the set remains unchanged.
 */
@Serializable
data class ReplaceStickerInSetRequest(
    val userId: UserId,
    val name: String,
    val oldSticker: String,
    val sticker: InputSticker,
) {
    override fun toString() = DebugStringBuilder("ReplaceStickerInSetRequest").prop("userId", userId).prop("name", name).prop("oldSticker", oldSticker).prop("sticker", sticker).toString()
}

/**
 * Request body for [setStickerEmojiList].
 *
 * @param sticker File identifier of the sticker
 * @param emojiList A JSON-serialized list of 1-20 emoji associated with the sticker
 */
@Serializable
data class SetStickerEmojiListRequest(
    val sticker: String,
    val emojiList: List<String>,
) {
    override fun toString() = DebugStringBuilder("SetStickerEmojiListRequest").prop("sticker", sticker).prop("emojiList", emojiList).toString()
}

/**
 * Request body for [setStickerKeywords].
 *
 * @param sticker File identifier of the sticker
 * @param keywords A JSON-serialized list of 0-20 search keywords for the sticker with total length of up to 64 characters
 */
@Serializable
data class SetStickerKeywordsRequest(
    val sticker: String,
    val keywords: List<String>? = null,
) {
    override fun toString() = DebugStringBuilder("SetStickerKeywordsRequest").prop("sticker", sticker).prop("keywords", keywords).toString()
}

/**
 * Request body for [setStickerMaskPosition].
 *
 * @param sticker File identifier of the sticker
 * @param maskPosition A JSON-serialized object with the position where the mask should be placed on faces. Omit the parameter to remove the mask position.
 */
@Serializable
data class SetStickerMaskPositionRequest(
    val sticker: String,
    val maskPosition: MaskPosition? = null,
) {
    override fun toString() = DebugStringBuilder("SetStickerMaskPositionRequest").prop("sticker", sticker).prop("maskPosition", maskPosition).toString()
}

/**
 * Request body for [setStickerSetTitle].
 *
 * @param name Sticker set name
 * @param title Sticker set title, 1-64 characters
 */
@Serializable
data class SetStickerSetTitleRequest(
    val name: String,
    val title: String,
) {
    override fun toString() = DebugStringBuilder("SetStickerSetTitleRequest").prop("name", name).prop("title", title).toString()
}

/**
 * Request body for [setStickerSetThumbnail].
 *
 * @param name Sticker set name
 * @param userId User identifier of the sticker set owner
 * @param format Format of the thumbnail, must be one of “static” for a .WEBP or .PNG image, “animated” for a .TGS animation, or “video” for a WEBM video
 * @param thumbnail A .WEBP or .PNG image with the thumbnail, must be up to 128 kilobytes in size and have a width and height of exactly 100px, or a .TGS animation with a thumbnail up to 32 kilobytes in size (see https://core.telegram.org/stickers#animated-sticker-requirements for animated sticker technical requirements), or a WEBM video with the thumbnail up to 32 kilobytes in size; see https://core.telegram.org/stickers#video-sticker-requirements for video sticker technical requirements. Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More information on Sending Files ». Animated and video sticker set thumbnails can't be uploaded via HTTP URL. If omitted, then the thumbnail is dropped and the first sticker is used as the thumbnail.
 */
@Serializable
data class SetStickerSetThumbnailRequest(
    val name: String,
    val userId: UserId,
    val format: String,
    val thumbnail: String? = null,
) {
    override fun toString() = DebugStringBuilder("SetStickerSetThumbnailRequest").prop("name", name).prop("userId", userId).prop("format", format).prop("thumbnail", thumbnail).toString()
}

/**
 * Request body for [setCustomEmojiStickerSetThumbnail].
 *
 * @param name Sticker set name
 * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
 */
@Serializable
data class SetCustomEmojiStickerSetThumbnailRequest(
    val name: String,
    val customEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("SetCustomEmojiStickerSetThumbnailRequest").prop("name", name).prop("customEmojiId", customEmojiId).toString()
}

/**
 * Request body for [deleteStickerSet].
 *
 * @param name Sticker set name
 */
@Serializable
data class DeleteStickerSetRequest(
    val name: String,
) {
    override fun toString() = DebugStringBuilder("DeleteStickerSetRequest").prop("name", name).toString()
}

/**
 * Request body for [answerInlineQuery].
 *
 * @param inlineQueryId Unique identifier for the answered query
 * @param results A JSON-serialized array of results for the inline query
 * @param cacheTime The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
 * @param isPersonal Pass True if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query.
 * @param nextOffset Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
 * @param button A JSON-serialized object describing a button to be shown above inline query results
 */
@Serializable
data class AnswerInlineQueryRequest(
    val inlineQueryId: InlineQueryId,
    val results: List<InlineQueryResult>,
    val cacheTime: Seconds? = null,
    val isPersonal: Boolean? = null,
    val nextOffset: String? = null,
    val button: InlineQueryResultsButton? = null,
) {
    override fun toString() = DebugStringBuilder("AnswerInlineQueryRequest").prop("inlineQueryId", inlineQueryId).prop("results", results).prop("cacheTime", cacheTime).prop("isPersonal", isPersonal).prop("nextOffset", nextOffset).prop("button", button).toString()
}

/**
 * Request body for [answerWebAppQuery].
 *
 * @param webAppQueryId Unique identifier for the query to be answered
 * @param result A JSON-serialized object describing the message to be sent
 */
@Serializable
data class AnswerWebAppQueryRequest(
    val webAppQueryId: WebAppQueryId,
    val result: InlineQueryResult,
) {
    override fun toString() = DebugStringBuilder("AnswerWebAppQueryRequest").prop("webAppQueryId", webAppQueryId).prop("result", result).toString()
}

/**
 * Request body for [sendInvoice].
 *
 * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
 * @param providerToken Payment provider token, obtained via @BotFather
 * @param currency Three-letter ISO 4217 currency code, see more on currencies
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param maxTipAmount The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0
 * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param startParameter Unique deep-linking parameter. If left empty, forwarded copies of the sent message will have a Pay button, allowing multiple users to pay directly from the forwarded message, using the same invoice. If non-empty, forwarded copies of the sent message will have a URL button with a deep link to the bot (instead of a Pay button), with the value used as the start parameter
 * @param providerData JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
 * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People like it better when they see what they are paying for.
 * @param photoSize Photo size in bytes
 * @param photoWidth Photo width
 * @param photoHeight Photo height
 * @param needName Pass True if you require the user's full name to complete the order
 * @param needPhoneNumber Pass True if you require the user's phone number to complete the order
 * @param needEmail Pass True if you require the user's email address to complete the order
 * @param needShippingAddress Pass True if you require the user's shipping address to complete the order
 * @param sendPhoneNumberToProvider Pass True if the user's phone number should be sent to provider
 * @param sendEmailToProvider Pass True if the user's email address should be sent to provider
 * @param isFlexible Pass True if the final price depends on the shipping method
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not empty, the first button must be a Pay button.
 */
@Serializable
data class SendInvoiceRequest(
    val chatId: ChatId,
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    val messageThreadId: MessageThreadId? = null,
    val maxTipAmount: Long? = null,
    val suggestedTipAmounts: List<Long>? = null,
    val startParameter: String? = null,
    val providerData: String? = null,
    val photoUrl: String? = null,
    val photoSize: Long? = null,
    val photoWidth: Long? = null,
    val photoHeight: Long? = null,
    val needName: Boolean? = null,
    val needPhoneNumber: Boolean? = null,
    val needEmail: Boolean? = null,
    val needShippingAddress: Boolean? = null,
    val sendPhoneNumberToProvider: Boolean? = null,
    val sendEmailToProvider: Boolean? = null,
    val isFlexible: Boolean? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendInvoiceRequest").prop("chatId", chatId).prop("title", title).prop("description", description).prop("payload", payload).prop("providerToken", providerToken).prop("currency", currency).prop("prices", prices).prop("messageThreadId", messageThreadId).prop("maxTipAmount", maxTipAmount).prop("suggestedTipAmounts", suggestedTipAmounts).prop("startParameter", startParameter).prop("providerData", providerData).prop("photoUrl", photoUrl).prop("photoSize", photoSize).prop("photoWidth", photoWidth).prop("photoHeight", photoHeight).prop("needName", needName).prop("needPhoneNumber", needPhoneNumber).prop("needEmail", needEmail).prop("needShippingAddress", needShippingAddress).prop("sendPhoneNumberToProvider", sendPhoneNumberToProvider).prop("sendEmailToProvider", sendEmailToProvider).prop("isFlexible", isFlexible).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [createInvoiceLink].
 *
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
 * @param providerToken Payment provider token, obtained via BotFather
 * @param currency Three-letter ISO 4217 currency code, see more on currencies
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
 * @param maxTipAmount The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0
 * @param suggestedTipAmounts A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param providerData JSON-serialized data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider.
 * @param photoUrl URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @param photoSize Photo size in bytes
 * @param photoWidth Photo width
 * @param photoHeight Photo height
 * @param needName Pass True if you require the user's full name to complete the order
 * @param needPhoneNumber Pass True if you require the user's phone number to complete the order
 * @param needEmail Pass True if you require the user's email address to complete the order
 * @param needShippingAddress Pass True if you require the user's shipping address to complete the order
 * @param sendPhoneNumberToProvider Pass True if the user's phone number should be sent to the provider
 * @param sendEmailToProvider Pass True if the user's email address should be sent to the provider
 * @param isFlexible Pass True if the final price depends on the shipping method
 */
@Serializable
data class CreateInvoiceLinkRequest(
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    val maxTipAmount: Long? = null,
    val suggestedTipAmounts: List<Long>? = null,
    val providerData: String? = null,
    val photoUrl: String? = null,
    val photoSize: Long? = null,
    val photoWidth: Long? = null,
    val photoHeight: Long? = null,
    val needName: Boolean? = null,
    val needPhoneNumber: Boolean? = null,
    val needEmail: Boolean? = null,
    val needShippingAddress: Boolean? = null,
    val sendPhoneNumberToProvider: Boolean? = null,
    val sendEmailToProvider: Boolean? = null,
    val isFlexible: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("CreateInvoiceLinkRequest").prop("title", title).prop("description", description).prop("payload", payload).prop("providerToken", providerToken).prop("currency", currency).prop("prices", prices).prop("maxTipAmount", maxTipAmount).prop("suggestedTipAmounts", suggestedTipAmounts).prop("providerData", providerData).prop("photoUrl", photoUrl).prop("photoSize", photoSize).prop("photoWidth", photoWidth).prop("photoHeight", photoHeight).prop("needName", needName).prop("needPhoneNumber", needPhoneNumber).prop("needEmail", needEmail).prop("needShippingAddress", needShippingAddress).prop("sendPhoneNumberToProvider", sendPhoneNumberToProvider).prop("sendEmailToProvider", sendEmailToProvider).prop("isFlexible", isFlexible).toString()
}

/**
 * Request body for [answerShippingQuery].
 *
 * @param shippingQueryId Unique identifier for the query to be answered
 * @param ok Pass True if delivery to the specified address is possible and False if there are any problems (for example, if delivery to the specified address is not possible)
 * @param shippingOptions Required if ok is True. A JSON-serialized array of available shipping options.
 * @param errorMessage Required if ok is False. Error message in human readable form that explains why it is impossible to complete the order (e.g. "Sorry, delivery to your desired address is unavailable'). Telegram will display this message to the user.
 */
@Serializable
data class AnswerShippingQueryRequest(
    val shippingQueryId: ShippingQueryId,
    val ok: Boolean,
    val shippingOptions: List<ShippingOption>? = null,
    val errorMessage: String? = null,
) {
    override fun toString() = DebugStringBuilder("AnswerShippingQueryRequest").prop("shippingQueryId", shippingQueryId).prop("ok", ok).prop("shippingOptions", shippingOptions).prop("errorMessage", errorMessage).toString()
}

/**
 * Request body for [answerPreCheckoutQuery].
 *
 * @param preCheckoutQueryId Unique identifier for the query to be answered
 * @param ok Specify True if everything is alright (goods are available, etc.) and the bot is ready to proceed with the order. Use False if there are any problems.
 * @param errorMessage Required if ok is False. Error message in human readable form that explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details. Please choose a different color or garment!"). Telegram will display this message to the user.
 */
@Serializable
data class AnswerPreCheckoutQueryRequest(
    val preCheckoutQueryId: String,
    val ok: Boolean,
    val errorMessage: String? = null,
) {
    override fun toString() = DebugStringBuilder("AnswerPreCheckoutQueryRequest").prop("preCheckoutQueryId", preCheckoutQueryId).prop("ok", ok).prop("errorMessage", errorMessage).toString()
}

/**
 * Request body for [setPassportDataErrors].
 *
 * @param userId User identifier
 * @param errors A JSON-serialized array describing the errors
 */
@Serializable
data class SetPassportDataErrorsRequest(
    val userId: UserId,
    val errors: List<PassportElementError>,
) {
    override fun toString() = DebugStringBuilder("SetPassportDataErrorsRequest").prop("userId", userId).prop("errors", errors).toString()
}

/**
 * Request body for [sendGame].
 *
 * @param chatId Unique identifier for the target chat
 * @param gameShortName Short name of the game, serves as the unique identifier for the game. Set up your games via @BotFather.
 * @param businessConnectionId Unique identifier of the business connection on behalf of which the message will be sent
 * @param messageThreadId Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
 * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
 * @param protectContent Protects the contents of the sent message from forwarding and saving
 * @param replyParameters Description of the message to reply to
 * @param replyMarkup A JSON-serialized object for an inline keyboard. If empty, one 'Play game_title' button will be shown. If not empty, the first button must launch the game. Not supported for messages sent on behalf of a business account.
 */
@Serializable
data class SendGameRequest(
    val chatId: ChatId,
    val gameShortName: String,
    val businessConnectionId: String? = null,
    val messageThreadId: MessageThreadId? = null,
    val disableNotification: Boolean? = null,
    val protectContent: Boolean? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("SendGameRequest").prop("chatId", chatId).prop("gameShortName", gameShortName).prop("businessConnectionId", businessConnectionId).prop("messageThreadId", messageThreadId).prop("disableNotification", disableNotification).prop("protectContent", protectContent).prop("replyParameters", replyParameters).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Request body for [setGameScore].
 *
 * @param userId User identifier
 * @param score New score, must be non-negative
 * @param force Pass True if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters
 * @param disableEditMessage Pass True if the game message should not be automatically edited to include the current scoreboard
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat
 * @param messageId Required if inline_message_id is not specified. Identifier of the sent message
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 */
@Serializable
data class SetGameScoreRequest(
    val userId: UserId,
    val score: Long,
    val force: Boolean? = null,
    val disableEditMessage: Boolean? = null,
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
) {
    override fun toString() = DebugStringBuilder("SetGameScoreRequest").prop("userId", userId).prop("score", score).prop("force", force).prop("disableEditMessage", disableEditMessage).prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).toString()
}

/**
 * Request body for [getGameHighScores].
 *
 * @param userId Target user id
 * @param chatId Required if inline_message_id is not specified. Unique identifier for the target chat
 * @param messageId Required if inline_message_id is not specified. Identifier of the sent message
 * @param inlineMessageId Required if chat_id and message_id are not specified. Identifier of the inline message
 */
@Serializable
data class GetGameHighScoresRequest(
    val userId: UserId,
    val chatId: ChatId? = null,
    val messageId: MessageId? = null,
    val inlineMessageId: InlineMessageId? = null,
) {
    override fun toString() = DebugStringBuilder("GetGameHighScoresRequest").prop("userId", userId).prop("chatId", chatId).prop("messageId", messageId).prop("inlineMessageId", inlineMessageId).toString()
}

