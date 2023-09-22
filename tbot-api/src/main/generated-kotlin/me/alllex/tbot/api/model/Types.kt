@file:OptIn(ExperimentalSerializationApi::class)

package me.alllex.tbot.api.model

import kotlinx.serialization.json.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

/**
 * Type of updates Telegram Bot can receive.
 */
enum class UpdateType {
    @SerialName("message") MESSAGE,
    @SerialName("edited_message") EDITED_MESSAGE,
    @SerialName("channel_post") CHANNEL_POST,
    @SerialName("edited_channel_post") EDITED_CHANNEL_POST,
    @SerialName("inline_query") INLINE_QUERY,
    @SerialName("chosen_inline_result") CHOSEN_INLINE_RESULT,
    @SerialName("callback_query") CALLBACK_QUERY,
    @SerialName("shipping_query") SHIPPING_QUERY,
    @SerialName("pre_checkout_query") PRE_CHECKOUT_QUERY,
    @SerialName("poll") POLL,
    @SerialName("poll_answer") POLL_ANSWER,
    @SerialName("my_chat_member") MY_CHAT_MEMBER,
    @SerialName("chat_member") CHAT_MEMBER,
    @SerialName("chat_join_request") CHAT_JOIN_REQUEST,
}

/**
 * This object represents an incoming update. At most one of the optional parameters can be present in any given update.
 *
 * Sub-types:
 * - [MessageUpdate]
 * - [EditedMessageUpdate]
 * - [ChannelPostUpdate]
 * - [EditedChannelPostUpdate]
 * - [InlineQueryUpdate]
 * - [ChosenInlineResultUpdate]
 * - [CallbackQueryUpdate]
 * - [ShippingQueryUpdate]
 * - [PreCheckoutQueryUpdate]
 * - [PollUpdate]
 * - [PollAnswerUpdate]
 * - [MyChatMemberUpdate]
 * - [ChatMemberUpdate]
 * - [ChatJoinRequestUpdate]
 */ 
@Serializable(with = UpdateSerializer::class)
sealed class Update {
    abstract val updateId: Long
    abstract val updateType: UpdateType
}

/**
 * New incoming message of any kind - text, photo, sticker, etc.
 */
@Serializable
data class MessageUpdate(
    override val updateId: Long,
    val message: Message,
): Update() {
    override val updateType: UpdateType get() = UpdateType.MESSAGE
}

/**
 * New version of a message that is known to the bot and was edited
 */
@Serializable
data class EditedMessageUpdate(
    override val updateId: Long,
    val editedMessage: Message,
): Update() {
    override val updateType: UpdateType get() = UpdateType.EDITED_MESSAGE
}

/**
 * New incoming channel post of any kind - text, photo, sticker, etc.
 */
@Serializable
data class ChannelPostUpdate(
    override val updateId: Long,
    val channelPost: Message,
): Update() {
    override val updateType: UpdateType get() = UpdateType.CHANNEL_POST
}

/**
 * New version of a channel post that is known to the bot and was edited
 */
@Serializable
data class EditedChannelPostUpdate(
    override val updateId: Long,
    val editedChannelPost: Message,
): Update() {
    override val updateType: UpdateType get() = UpdateType.EDITED_CHANNEL_POST
}

/**
 * New incoming inline query
 */
@Serializable
data class InlineQueryUpdate(
    override val updateId: Long,
    val inlineQuery: InlineQuery,
): Update() {
    override val updateType: UpdateType get() = UpdateType.INLINE_QUERY
}

/**
 * The result of an inline query that was chosen by a user and sent to their chat partner. Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
 */
@Serializable
data class ChosenInlineResultUpdate(
    override val updateId: Long,
    val chosenInlineResult: ChosenInlineResult,
): Update() {
    override val updateType: UpdateType get() = UpdateType.CHOSEN_INLINE_RESULT
}

/**
 * New incoming callback query
 */
@Serializable
data class CallbackQueryUpdate(
    override val updateId: Long,
    val callbackQuery: CallbackQuery,
): Update() {
    override val updateType: UpdateType get() = UpdateType.CALLBACK_QUERY
}

/**
 * New incoming shipping query. Only for invoices with flexible price
 */
@Serializable
data class ShippingQueryUpdate(
    override val updateId: Long,
    val shippingQuery: ShippingQuery,
): Update() {
    override val updateType: UpdateType get() = UpdateType.SHIPPING_QUERY
}

/**
 * New incoming pre-checkout query. Contains full information about checkout
 */
@Serializable
data class PreCheckoutQueryUpdate(
    override val updateId: Long,
    val preCheckoutQuery: PreCheckoutQuery,
): Update() {
    override val updateType: UpdateType get() = UpdateType.PRE_CHECKOUT_QUERY
}

/**
 * New poll state. Bots receive only updates about stopped polls and polls, which are sent by the bot
 */
@Serializable
data class PollUpdate(
    override val updateId: Long,
    val poll: Poll,
): Update() {
    override val updateType: UpdateType get() = UpdateType.POLL
}

/**
 * A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself.
 */
@Serializable
data class PollAnswerUpdate(
    override val updateId: Long,
    val pollAnswer: PollAnswer,
): Update() {
    override val updateType: UpdateType get() = UpdateType.POLL_ANSWER
}

/**
 * The bot's chat member status was updated in a chat. For private chats, this update is received only when the bot is blocked or unblocked by the user.
 */
@Serializable
data class MyChatMemberUpdate(
    override val updateId: Long,
    val myChatMember: ChatMemberUpdated,
): Update() {
    override val updateType: UpdateType get() = UpdateType.MY_CHAT_MEMBER
}

/**
 * A chat member's status was updated in a chat. The bot must be an administrator in the chat and must explicitly specify “chat_member” in the list of allowed_updates to receive these updates.
 */
@Serializable
data class ChatMemberUpdate(
    override val updateId: Long,
    val chatMember: ChatMemberUpdated,
): Update() {
    override val updateType: UpdateType get() = UpdateType.CHAT_MEMBER
}

/**
 * A request to join the chat has been sent. The bot must have the can_invite_users administrator right in the chat to receive these updates.
 */
@Serializable
data class ChatJoinRequestUpdate(
    override val updateId: Long,
    val chatJoinRequest: ChatJoinRequest,
): Update() {
    override val updateType: UpdateType get() = UpdateType.CHAT_JOIN_REQUEST
}

object UpdateSerializer : JsonContentPolymorphicSerializer<Update>(Update::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Update> {
        val json = element.jsonObject
        return when {
            "message" in json -> MessageUpdate.serializer()
            "edited_message" in json -> EditedMessageUpdate.serializer()
            "channel_post" in json -> ChannelPostUpdate.serializer()
            "edited_channel_post" in json -> EditedChannelPostUpdate.serializer()
            "inline_query" in json -> InlineQueryUpdate.serializer()
            "chosen_inline_result" in json -> ChosenInlineResultUpdate.serializer()
            "callback_query" in json -> CallbackQueryUpdate.serializer()
            "shipping_query" in json -> ShippingQueryUpdate.serializer()
            "pre_checkout_query" in json -> PreCheckoutQueryUpdate.serializer()
            "poll" in json -> PollUpdate.serializer()
            "poll_answer" in json -> PollAnswerUpdate.serializer()
            "my_chat_member" in json -> MyChatMemberUpdate.serializer()
            "chat_member" in json -> ChatMemberUpdate.serializer()
            "chat_join_request" in json -> ChatJoinRequestUpdate.serializer()
            else -> error("Failed to deserialize an update type: $json")
        }
    }
}
/**
 * Describes the current status of a webhook.
 * @param url Webhook URL, may be empty if webhook is not set up
 * @param hasCustomCertificate True, if a custom certificate was provided for webhook certificate checks
 * @param pendingUpdateCount Number of updates awaiting delivery
 * @param ipAddress Optional. Currently used webhook IP address
 * @param lastErrorDate Optional. Unix time for the most recent error that happened when trying to deliver an update via webhook
 * @param lastErrorMessage Optional. Error message in human-readable format for the most recent error that happened when trying to deliver an update via webhook
 * @param lastSynchronizationErrorDate Optional. Unix time of the most recent error that happened when trying to synchronize available updates with Telegram datacenters
 * @param maxConnections Optional. The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
 * @param allowedUpdates Optional. A list of update types the bot is subscribed to. Defaults to all update types except chat_member
 */
@Serializable
data class WebhookInfo(
    val url: String,
    val hasCustomCertificate: Boolean,
    val pendingUpdateCount: Long,
    val ipAddress: String? = null,
    val lastErrorDate: UnixTimestamp? = null,
    val lastErrorMessage: String? = null,
    val lastSynchronizationErrorDate: UnixTimestamp? = null,
    val maxConnections: Long? = null,
    val allowedUpdates: List<UpdateType>? = null,
) {
    override fun toString() = DebugStringBuilder("WebhookInfo").prop("url", url).prop("hasCustomCertificate", hasCustomCertificate).prop("pendingUpdateCount", pendingUpdateCount).prop("ipAddress", ipAddress).prop("lastErrorDate", lastErrorDate).prop("lastErrorMessage", lastErrorMessage).prop("lastSynchronizationErrorDate", lastSynchronizationErrorDate).prop("maxConnections", maxConnections).prop("allowedUpdates", allowedUpdates).toString()
}

/**
 * This object represents a Telegram user or bot.
 * @param id Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param isBot True, if this user is a bot
 * @param firstName User's or bot's first name
 * @param lastName Optional. User's or bot's last name
 * @param username Optional. User's or bot's username
 * @param languageCode Optional. IETF language tag of the user's language
 * @param isPremium Optional. True, if this user is a Telegram Premium user
 * @param addedToAttachmentMenu Optional. True, if this user added the bot to the attachment menu
 * @param canJoinGroups Optional. True, if the bot can be invited to groups. Returned only in getMe.
 * @param canReadAllGroupMessages Optional. True, if privacy mode is disabled for the bot. Returned only in getMe.
 * @param supportsInlineQueries Optional. True, if the bot supports inline queries. Returned only in getMe.
 */
@Serializable
data class User(
    val id: UserId,
    val isBot: Boolean,
    val firstName: String,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
    val isPremium: Boolean? = null,
    val addedToAttachmentMenu: Boolean? = null,
    val canJoinGroups: Boolean? = null,
    val canReadAllGroupMessages: Boolean? = null,
    val supportsInlineQueries: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("User").prop("id", id).prop("isBot", isBot).prop("firstName", firstName).prop("lastName", lastName).prop("username", username).prop("languageCode", languageCode).prop("isPremium", isPremium).prop("addedToAttachmentMenu", addedToAttachmentMenu).prop("canJoinGroups", canJoinGroups).prop("canReadAllGroupMessages", canReadAllGroupMessages).prop("supportsInlineQueries", supportsInlineQueries).toString()
}

/**
 * This object represents a chat.
 * @param id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param type Type of chat, can be either “private”, “group”, “supergroup” or “channel”
 * @param title Optional. Title, for supergroups, channels and group chats
 * @param username Optional. Username, for private chats, supergroups and channels if available
 * @param firstName Optional. First name of the other party in a private chat
 * @param lastName Optional. Last name of the other party in a private chat
 * @param isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 * @param photo Optional. Chat photo. Returned only in getChat.
 * @param activeUsernames Optional. If non-empty, the list of all active chat usernames; for private chats, supergroups and channels. Returned only in getChat.
 * @param emojiStatusCustomEmojiId Optional. Custom emoji identifier of emoji status of the other party in a private chat. Returned only in getChat.
 * @param emojiStatusExpirationDate Optional. Expiration date of the emoji status of the other party in a private chat, if any. Returned only in getChat.
 * @param bio Optional. Bio of the other party in a private chat. Returned only in getChat.
 * @param hasPrivateForwards Optional. True, if privacy settings of the other party in the private chat allows to use tg://user?id=<user_id> links only in chats with the user. Returned only in getChat.
 * @param hasRestrictedVoiceAndVideoMessages Optional. True, if the privacy settings of the other party restrict sending voice and video note messages in the private chat. Returned only in getChat.
 * @param joinToSendMessages Optional. True, if users need to join the supergroup before they can send messages. Returned only in getChat.
 * @param joinByRequest Optional. True, if all users directly joining the supergroup need to be approved by supergroup administrators. Returned only in getChat.
 * @param description Optional. Description, for groups, supergroups and channel chats. Returned only in getChat.
 * @param inviteLink Optional. Primary invite link, for groups, supergroups and channel chats. Returned only in getChat.
 * @param pinnedMessage Optional. The most recent pinned message (by sending date). Returned only in getChat.
 * @param permissions Optional. Default chat member permissions, for groups and supergroups. Returned only in getChat.
 * @param slowModeDelay Optional. For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user; in seconds. Returned only in getChat.
 * @param messageAutoDeleteTime Optional. The time after which all messages sent to the chat will be automatically deleted; in seconds. Returned only in getChat.
 * @param hasAggressiveAntiSpamEnabled Optional. True, if aggressive anti-spam checks are enabled in the supergroup. The field is only available to chat administrators. Returned only in getChat.
 * @param hasHiddenMembers Optional. True, if non-administrators can only get the list of bots and administrators in the chat. Returned only in getChat.
 * @param hasProtectedContent Optional. True, if messages from the chat can't be forwarded to other chats. Returned only in getChat.
 * @param stickerSetName Optional. For supergroups, name of group sticker set. Returned only in getChat.
 * @param canSetStickerSet Optional. True, if the bot can change the group sticker set. Returned only in getChat.
 * @param linkedChatId Optional. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier. Returned only in getChat.
 * @param location Optional. For supergroups, the location to which the supergroup is connected. Returned only in getChat.
 */
@Serializable
data class Chat(
    val id: ChatId,
    val type: String,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isForum: Boolean? = null,
    val photo: ChatPhoto? = null,
    val activeUsernames: List<String>? = null,
    val emojiStatusCustomEmojiId: CustomEmojiId? = null,
    val emojiStatusExpirationDate: UnixTimestamp? = null,
    val bio: String? = null,
    val hasPrivateForwards: Boolean? = null,
    val hasRestrictedVoiceAndVideoMessages: Boolean? = null,
    val joinToSendMessages: Boolean? = null,
    val joinByRequest: Boolean? = null,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Long? = null,
    val messageAutoDeleteTime: Long? = null,
    val hasAggressiveAntiSpamEnabled: Boolean? = null,
    val hasHiddenMembers: Boolean? = null,
    val hasProtectedContent: Boolean? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val linkedChatId: ChatId? = null,
    val location: ChatLocation? = null,
) {
    override fun toString() = DebugStringBuilder("Chat").prop("id", id).prop("type", type).prop("title", title).prop("username", username).prop("firstName", firstName).prop("lastName", lastName).prop("isForum", isForum).prop("photo", photo).prop("activeUsernames", activeUsernames).prop("emojiStatusCustomEmojiId", emojiStatusCustomEmojiId).prop("emojiStatusExpirationDate", emojiStatusExpirationDate).prop("bio", bio).prop("hasPrivateForwards", hasPrivateForwards).prop("hasRestrictedVoiceAndVideoMessages", hasRestrictedVoiceAndVideoMessages).prop("joinToSendMessages", joinToSendMessages).prop("joinByRequest", joinByRequest).prop("description", description).prop("inviteLink", inviteLink).prop("pinnedMessage", pinnedMessage).prop("permissions", permissions).prop("slowModeDelay", slowModeDelay).prop("messageAutoDeleteTime", messageAutoDeleteTime).prop("hasAggressiveAntiSpamEnabled", hasAggressiveAntiSpamEnabled).prop("hasHiddenMembers", hasHiddenMembers).prop("hasProtectedContent", hasProtectedContent).prop("stickerSetName", stickerSetName).prop("canSetStickerSet", canSetStickerSet).prop("linkedChatId", linkedChatId).prop("location", location).toString()
}

/**
 * This object represents a message.
 * @param messageId Unique message identifier inside this chat
 * @param date Date the message was sent in Unix time
 * @param chat Conversation the message belongs to
 * @param messageThreadId Optional. Unique identifier of a message thread to which the message belongs; for supergroups only
 * @param from Optional. Sender of the message; empty for messages sent to channels. For backward compatibility, the field contains a fake sender user in non-channel chats, if the message was sent on behalf of a chat.
 * @param senderChat Optional. Sender of the message, sent on behalf of a chat. For example, the channel itself for channel posts, the supergroup itself for messages from anonymous group administrators, the linked channel for messages automatically forwarded to the discussion group. For backward compatibility, the field from contains a fake sender user in non-channel chats, if the message was sent on behalf of a chat.
 * @param forwardFrom Optional. For forwarded messages, sender of the original message
 * @param forwardFromChat Optional. For messages forwarded from channels or from anonymous administrators, information about the original sender chat
 * @param forwardFromMessageId Optional. For messages forwarded from channels, identifier of the original message in the channel
 * @param forwardSignature Optional. For forwarded messages that were originally sent in channels or by an anonymous chat administrator, signature of the message sender if present
 * @param forwardSenderName Optional. Sender's name for messages forwarded from users who disallow adding a link to their account in forwarded messages
 * @param forwardDate Optional. For forwarded messages, date the original message was sent in Unix time
 * @param isTopicMessage Optional. True, if the message is sent to a forum topic
 * @param isAutomaticForward Optional. True, if the message is a channel post that was automatically forwarded to the connected discussion group
 * @param replyToMessage Optional. For replies, the original message. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @param viaBot Optional. Bot through which the message was sent
 * @param editDate Optional. Date the message was last edited in Unix time
 * @param hasProtectedContent Optional. True, if the message can't be forwarded
 * @param mediaGroupId Optional. The unique identifier of a media message group this message belongs to
 * @param authorSignature Optional. Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
 * @param text Optional. For text messages, the actual UTF-8 text of the message
 * @param entities Optional. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
 * @param animation Optional. Message is an animation, information about the animation. For backward compatibility, when this field is set, the document field will also be set
 * @param audio Optional. Message is an audio file, information about the file
 * @param document Optional. Message is a general file, information about the file
 * @param photo Optional. Message is a photo, available sizes of the photo
 * @param sticker Optional. Message is a sticker, information about the sticker
 * @param story Optional. Message is a forwarded story
 * @param video Optional. Message is a video, information about the video
 * @param videoNote Optional. Message is a video note, information about the video message
 * @param voice Optional. Message is a voice message, information about the file
 * @param caption Optional. Caption for the animation, audio, document, photo, video or voice
 * @param captionEntities Optional. For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
 * @param hasMediaSpoiler Optional. True, if the message media is covered by a spoiler animation
 * @param contact Optional. Message is a shared contact, information about the contact
 * @param dice Optional. Message is a dice with random value
 * @param game Optional. Message is a game, information about the game. More about games »
 * @param poll Optional. Message is a native poll, information about the poll
 * @param venue Optional. Message is a venue, information about the venue. For backward compatibility, when this field is set, the location field will also be set
 * @param location Optional. Message is a shared location, information about the location
 * @param newChatMembers Optional. New members that were added to the group or supergroup and information about them (the bot itself may be one of these members)
 * @param leftChatMember Optional. A member was removed from the group, information about them (this member may be the bot itself)
 * @param newChatTitle Optional. A chat title was changed to this value
 * @param newChatPhoto Optional. A chat photo was change to this value
 * @param deleteChatPhoto Optional. Service message: the chat photo was deleted
 * @param groupChatCreated Optional. Service message: the group has been created
 * @param supergroupChatCreated Optional. Service message: the supergroup has been created. This field can't be received in a message coming through updates, because bot can't be a member of a supergroup when it is created. It can only be found in reply_to_message if someone replies to a very first message in a directly created supergroup.
 * @param channelChatCreated Optional. Service message: the channel has been created. This field can't be received in a message coming through updates, because bot can't be a member of a channel when it is created. It can only be found in reply_to_message if someone replies to a very first message in a channel.
 * @param messageAutoDeleteTimerChanged Optional. Service message: auto-delete timer settings changed in the chat
 * @param migrateToChatId Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param migrateFromChatId Optional. The supergroup has been migrated from a group with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param pinnedMessage Optional. Specified message was pinned. Note that the Message object in this field will not contain further reply_to_message fields even if it is itself a reply.
 * @param invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments »
 * @param successfulPayment Optional. Message is a service message about a successful payment, information about the payment. More about payments »
 * @param userShared Optional. Service message: a user was shared with the bot
 * @param chatShared Optional. Service message: a chat was shared with the bot
 * @param connectedWebsite Optional. The domain name of the website on which the user has logged in. More about Telegram Login »
 * @param writeAccessAllowed Optional. Service message: the user allowed the bot added to the attachment menu to write messages
 * @param passportData Optional. Telegram Passport data
 * @param proximityAlertTriggered Optional. Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
 * @param forumTopicCreated Optional. Service message: forum topic created
 * @param forumTopicEdited Optional. Service message: forum topic edited
 * @param forumTopicClosed Optional. Service message: forum topic closed
 * @param forumTopicReopened Optional. Service message: forum topic reopened
 * @param generalForumTopicHidden Optional. Service message: the 'General' forum topic hidden
 * @param generalForumTopicUnhidden Optional. Service message: the 'General' forum topic unhidden
 * @param videoChatScheduled Optional. Service message: video chat scheduled
 * @param videoChatStarted Optional. Service message: video chat started
 * @param videoChatEnded Optional. Service message: video chat ended
 * @param videoChatParticipantsInvited Optional. Service message: new participants invited to a video chat
 * @param webAppData Optional. Service message: data sent by a Web App
 * @param replyMarkup Optional. Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
 */
@Serializable
data class Message(
    val messageId: MessageId,
    val date: Long,
    val chat: Chat,
    val messageThreadId: MessageThreadId? = null,
    val from: User? = null,
    val senderChat: Chat? = null,
    val forwardFrom: User? = null,
    val forwardFromChat: Chat? = null,
    val forwardFromMessageId: MessageId? = null,
    val forwardSignature: String? = null,
    val forwardSenderName: String? = null,
    val forwardDate: UnixTimestamp? = null,
    val isTopicMessage: Boolean? = null,
    val isAutomaticForward: Boolean? = null,
    val replyToMessage: Message? = null,
    val viaBot: User? = null,
    val editDate: UnixTimestamp? = null,
    val hasProtectedContent: Boolean? = null,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    val caption: String? = null,
    val captionEntities: List<MessageEntity>? = null,
    val hasMediaSpoiler: Boolean? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
    val location: Location? = null,
    val newChatMembers: List<User>? = null,
    val leftChatMember: User? = null,
    val newChatTitle: String? = null,
    val newChatPhoto: List<PhotoSize>? = null,
    val deleteChatPhoto: Boolean? = null,
    val groupChatCreated: Boolean? = null,
    val supergroupChatCreated: Boolean? = null,
    val channelChatCreated: Boolean? = null,
    val messageAutoDeleteTimerChanged: MessageAutoDeleteTimerChanged? = null,
    val migrateToChatId: ChatId? = null,
    val migrateFromChatId: ChatId? = null,
    val pinnedMessage: Message? = null,
    val invoice: Invoice? = null,
    val successfulPayment: SuccessfulPayment? = null,
    val userShared: UserShared? = null,
    val chatShared: ChatShared? = null,
    val connectedWebsite: String? = null,
    val writeAccessAllowed: WriteAccessAllowed? = null,
    val passportData: PassportData? = null,
    val proximityAlertTriggered: ProximityAlertTriggered? = null,
    val forumTopicCreated: ForumTopicCreated? = null,
    val forumTopicEdited: ForumTopicEdited? = null,
    val forumTopicClosed: ForumTopicClosed? = null,
    val forumTopicReopened: ForumTopicReopened? = null,
    val generalForumTopicHidden: GeneralForumTopicHidden? = null,
    val generalForumTopicUnhidden: GeneralForumTopicUnhidden? = null,
    val videoChatScheduled: VideoChatScheduled? = null,
    val videoChatStarted: VideoChatStarted? = null,
    val videoChatEnded: VideoChatEnded? = null,
    val videoChatParticipantsInvited: VideoChatParticipantsInvited? = null,
    val webAppData: WebAppData? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
) {
    override fun toString() = DebugStringBuilder("Message").prop("messageId", messageId).prop("date", date).prop("chat", chat).prop("messageThreadId", messageThreadId).prop("from", from).prop("senderChat", senderChat).prop("forwardFrom", forwardFrom).prop("forwardFromChat", forwardFromChat).prop("forwardFromMessageId", forwardFromMessageId).prop("forwardSignature", forwardSignature).prop("forwardSenderName", forwardSenderName).prop("forwardDate", forwardDate).prop("isTopicMessage", isTopicMessage).prop("isAutomaticForward", isAutomaticForward).prop("replyToMessage", replyToMessage).prop("viaBot", viaBot).prop("editDate", editDate).prop("hasProtectedContent", hasProtectedContent).prop("mediaGroupId", mediaGroupId).prop("authorSignature", authorSignature).prop("text", text).prop("entities", entities).prop("animation", animation).prop("audio", audio).prop("document", document).prop("photo", photo).prop("sticker", sticker).prop("story", story).prop("video", video).prop("videoNote", videoNote).prop("voice", voice).prop("caption", caption).prop("captionEntities", captionEntities).prop("hasMediaSpoiler", hasMediaSpoiler).prop("contact", contact).prop("dice", dice).prop("game", game).prop("poll", poll).prop("venue", venue).prop("location", location).prop("newChatMembers", newChatMembers).prop("leftChatMember", leftChatMember).prop("newChatTitle", newChatTitle).prop("newChatPhoto", newChatPhoto).prop("deleteChatPhoto", deleteChatPhoto).prop("groupChatCreated", groupChatCreated).prop("supergroupChatCreated", supergroupChatCreated).prop("channelChatCreated", channelChatCreated).prop("messageAutoDeleteTimerChanged", messageAutoDeleteTimerChanged).prop("migrateToChatId", migrateToChatId).prop("migrateFromChatId", migrateFromChatId).prop("pinnedMessage", pinnedMessage).prop("invoice", invoice).prop("successfulPayment", successfulPayment).prop("userShared", userShared).prop("chatShared", chatShared).prop("connectedWebsite", connectedWebsite).prop("writeAccessAllowed", writeAccessAllowed).prop("passportData", passportData).prop("proximityAlertTriggered", proximityAlertTriggered).prop("forumTopicCreated", forumTopicCreated).prop("forumTopicEdited", forumTopicEdited).prop("forumTopicClosed", forumTopicClosed).prop("forumTopicReopened", forumTopicReopened).prop("generalForumTopicHidden", generalForumTopicHidden).prop("generalForumTopicUnhidden", generalForumTopicUnhidden).prop("videoChatScheduled", videoChatScheduled).prop("videoChatStarted", videoChatStarted).prop("videoChatEnded", videoChatEnded).prop("videoChatParticipantsInvited", videoChatParticipantsInvited).prop("webAppData", webAppData).prop("replyMarkup", replyMarkup).toString()
}

/**
 * This object represents a unique message identifier.
 * @param messageId Unique message identifier
 */
@Serializable
data class MessageIdResult(
    val messageId: MessageId,
) {
    override fun toString() = DebugStringBuilder("MessageIdResult").prop("messageId", messageId).toString()
}

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 * @param type Type of the entity. Currently, can be “mention” (@username), “hashtag” (#hashtag), “cashtag” ($USD), “bot_command” (/start@jobs_bot), “url” (https://telegram.org), “email” (do-not-reply@telegram.org), “phone_number” (+1-212-555-0123), “bold” (bold text), “italic” (italic text), “underline” (underlined text), “strikethrough” (strikethrough text), “spoiler” (spoiler message), “code” (monowidth string), “pre” (monowidth block), “text_link” (for clickable text URLs), “text_mention” (for users without usernames), “custom_emoji” (for inline custom emoji stickers)
 * @param offset Offset in UTF-16 code units to the start of the entity
 * @param length Length of the entity in UTF-16 code units
 * @param url Optional. For “text_link” only, URL that will be opened after user taps on the text
 * @param user Optional. For “text_mention” only, the mentioned user
 * @param language Optional. For “pre” only, the programming language of the entity text
 * @param customEmojiId Optional. For “custom_emoji” only, unique identifier of the custom emoji. Use getCustomEmojiStickers to get full information about the sticker
 */
@Serializable
data class MessageEntity(
    val type: String,
    val offset: Long,
    val length: Long,
    val url: String? = null,
    val user: User? = null,
    val language: String? = null,
    val customEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("MessageEntity").prop("type", type).prop("offset", offset).prop("length", length).prop("url", url).prop("user", user).prop("language", language).prop("customEmojiId", customEmojiId).toString()
}

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Photo width
 * @param height Photo height
 * @param fileSize Optional. File size in bytes
 */
@Serializable
data class PhotoSize(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val width: Long,
    val height: Long,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("PhotoSize").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("width", width).prop("height", height).prop("fileSize", fileSize).toString()
}

/**
 * This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound).
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Video width as defined by sender
 * @param height Video height as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumbnail Optional. Animation thumbnail as defined by sender
 * @param fileName Optional. Original animation filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Animation(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val width: Long,
    val height: Long,
    val duration: Seconds,
    val thumbnail: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Animation").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("width", width).prop("height", height).prop("duration", duration).prop("thumbnail", thumbnail).prop("fileName", fileName).prop("mimeType", mimeType).prop("fileSize", fileSize).toString()
}

/**
 * This object represents an audio file to be treated as music by the Telegram clients.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param duration Duration of the audio in seconds as defined by sender
 * @param performer Optional. Performer of the audio as defined by sender or by audio tags
 * @param title Optional. Title of the audio as defined by sender or by audio tags
 * @param fileName Optional. Original filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 * @param thumbnail Optional. Thumbnail of the album cover to which the music file belongs
 */
@Serializable
data class Audio(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val duration: Seconds,
    val performer: String? = null,
    val title: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val thumbnail: PhotoSize? = null,
) {
    override fun toString() = DebugStringBuilder("Audio").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("duration", duration).prop("performer", performer).prop("title", title).prop("fileName", fileName).prop("mimeType", mimeType).prop("fileSize", fileSize).prop("thumbnail", thumbnail).toString()
}

/**
 * This object represents a general file (as opposed to photos, voice messages and audio files).
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param thumbnail Optional. Document thumbnail as defined by sender
 * @param fileName Optional. Original filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Document(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val thumbnail: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Document").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("thumbnail", thumbnail).prop("fileName", fileName).prop("mimeType", mimeType).prop("fileSize", fileSize).toString()
}

/**
 * This object represents a message about a forwarded story in the chat. Currently holds no information.
 */
@Serializable
data object Story

/**
 * This object represents a video file.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Video width as defined by sender
 * @param height Video height as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumbnail Optional. Video thumbnail
 * @param fileName Optional. Original filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Video(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val width: Long,
    val height: Long,
    val duration: Seconds,
    val thumbnail: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Video").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("width", width).prop("height", height).prop("duration", duration).prop("thumbnail", thumbnail).prop("fileName", fileName).prop("mimeType", mimeType).prop("fileSize", fileSize).toString()
}

/**
 * This object represents a video message (available in Telegram apps as of v.4.0).
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param length Video width and height (diameter of the video message) as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumbnail Optional. Video thumbnail
 * @param fileSize Optional. File size in bytes
 */
@Serializable
data class VideoNote(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val length: Long,
    val duration: Seconds,
    val thumbnail: PhotoSize? = null,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("VideoNote").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("length", length).prop("duration", duration).prop("thumbnail", thumbnail).prop("fileSize", fileSize).toString()
}

/**
 * This object represents a voice note.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param duration Duration of the audio in seconds as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Voice(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val duration: Seconds,
    val mimeType: String? = null,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Voice").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("duration", duration).prop("mimeType", mimeType).prop("fileSize", fileSize).toString()
}

/**
 * This object represents a phone contact.
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Optional. Contact's last name
 * @param userId Optional. Contact's user identifier in Telegram. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param vcard Optional. Additional data about the contact in the form of a vCard
 */
@Serializable
data class Contact(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: UserId? = null,
    val vcard: String? = null,
) {
    override fun toString() = DebugStringBuilder("Contact").prop("phoneNumber", phoneNumber).prop("firstName", firstName).prop("lastName", lastName).prop("userId", userId).prop("vcard", vcard).toString()
}

/**
 * This object represents an animated emoji that displays a random value.
 * @param emoji Emoji on which the dice throw animation is based
 * @param value Value of the dice, 1-6 for “”, “” and “” base emoji, 1-5 for “” and “” base emoji, 1-64 for “” base emoji
 */
@Serializable
data class Dice(
    val emoji: String,
    val value: Long,
) {
    override fun toString() = DebugStringBuilder("Dice").prop("emoji", emoji).prop("value", value).toString()
}

/**
 * This object contains information about one answer option in a poll.
 * @param text Option text, 1-100 characters
 * @param voterCount Number of users that voted for this option
 */
@Serializable
data class PollOption(
    val text: String,
    val voterCount: Long,
) {
    override fun toString() = DebugStringBuilder("PollOption").prop("text", text).prop("voterCount", voterCount).toString()
}

/**
 * This object represents an answer of a user in a non-anonymous poll.
 * @param pollId Unique poll identifier
 * @param optionIds 0-based identifiers of chosen answer options. May be empty if the vote was retracted.
 * @param voterChat Optional. The chat that changed the answer to the poll, if the voter is anonymous
 * @param user Optional. The user that changed the answer to the poll, if the voter isn't anonymous
 */
@Serializable
data class PollAnswer(
    val pollId: String,
    val optionIds: List<Long>,
    val voterChat: Chat? = null,
    val user: User? = null,
) {
    override fun toString() = DebugStringBuilder("PollAnswer").prop("pollId", pollId).prop("optionIds", optionIds).prop("voterChat", voterChat).prop("user", user).toString()
}

/**
 * This object contains information about a poll.
 * @param id Unique poll identifier
 * @param question Poll question, 1-300 characters
 * @param options List of poll options
 * @param totalVoterCount Total number of users that voted in the poll
 * @param isClosed True, if the poll is closed
 * @param isAnonymous True, if the poll is anonymous
 * @param type Poll type, currently can be “regular” or “quiz”
 * @param allowsMultipleAnswers True, if the poll allows multiple answers
 * @param correctOptionId Optional. 0-based identifier of the correct answer option. Available only for polls in the quiz mode, which are closed, or was sent (not forwarded) by the bot or to the private chat with the bot.
 * @param explanation Optional. Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll, 0-200 characters
 * @param explanationEntities Optional. Special entities like usernames, URLs, bot commands, etc. that appear in the explanation
 * @param openPeriod Optional. Amount of time in seconds the poll will be active after creation
 * @param closeDate Optional. Point in time (Unix timestamp) when the poll will be automatically closed
 */
@Serializable
data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val totalVoterCount: Long,
    val isClosed: Boolean,
    val isAnonymous: Boolean,
    val type: String,
    val allowsMultipleAnswers: Boolean,
    val correctOptionId: Long? = null,
    val explanation: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Seconds? = null,
    val closeDate: UnixTimestamp? = null,
) {
    override fun toString() = DebugStringBuilder("Poll").prop("id", id).prop("question", question).prop("options", options).prop("totalVoterCount", totalVoterCount).prop("isClosed", isClosed).prop("isAnonymous", isAnonymous).prop("type", type).prop("allowsMultipleAnswers", allowsMultipleAnswers).prop("correctOptionId", correctOptionId).prop("explanation", explanation).prop("explanationEntities", explanationEntities).prop("openPeriod", openPeriod).prop("closeDate", closeDate).toString()
}

/**
 * This object represents a point on the map.
 * @param longitude Longitude as defined by sender
 * @param latitude Latitude as defined by sender
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Time relative to the message sending date, during which the location can be updated; in seconds. For active live locations only.
 * @param heading Optional. The direction in which user is moving, in degrees; 1-360. For active live locations only.
 * @param proximityAlertRadius Optional. The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only.
 */
@Serializable
data class Location(
    val longitude: Double,
    val latitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Location").prop("longitude", longitude).prop("latitude", latitude).prop("horizontalAccuracy", horizontalAccuracy).prop("livePeriod", livePeriod).prop("heading", heading).prop("proximityAlertRadius", proximityAlertRadius).toString()
}

/**
 * This object represents a venue.
 * @param location Venue location. Can't be a live location
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Optional. Foursquare identifier of the venue
 * @param foursquareType Optional. Foursquare type of the venue. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
 * @param googlePlaceId Optional. Google Places identifier of the venue
 * @param googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 */
@Serializable
data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
) {
    override fun toString() = DebugStringBuilder("Venue").prop("location", location).prop("title", title).prop("address", address).prop("foursquareId", foursquareId).prop("foursquareType", foursquareType).prop("googlePlaceId", googlePlaceId).prop("googlePlaceType", googlePlaceType).toString()
}

/**
 * Describes data sent from a Web App to the bot.
 * @param data The data. Be aware that a bad client can send arbitrary data in this field.
 * @param buttonText Text of the web_app keyboard button from which the Web App was opened. Be aware that a bad client can send arbitrary data in this field.
 */
@Serializable
data class WebAppData(
    val data: String,
    val buttonText: String,
) {
    override fun toString() = DebugStringBuilder("WebAppData").prop("data", data).prop("buttonText", buttonText).toString()
}

/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 * @param traveler User that triggered the alert
 * @param watcher User that set the alert
 * @param distance The distance between the users
 */
@Serializable
data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Long,
) {
    override fun toString() = DebugStringBuilder("ProximityAlertTriggered").prop("traveler", traveler).prop("watcher", watcher).prop("distance", distance).toString()
}

/**
 * This object represents a service message about a change in auto-delete timer settings.
 * @param messageAutoDeleteTime New auto-delete time for messages in the chat; in seconds
 */
@Serializable
data class MessageAutoDeleteTimerChanged(
    val messageAutoDeleteTime: Long,
) {
    override fun toString() = DebugStringBuilder("MessageAutoDeleteTimerChanged").prop("messageAutoDeleteTime", messageAutoDeleteTime).toString()
}

/**
 * This object represents a service message about a new forum topic created in the chat.
 * @param name Name of the topic
 * @param iconColor Color of the topic icon in RGB format
 * @param iconCustomEmojiId Optional. Unique identifier of the custom emoji shown as the topic icon
 */
@Serializable
data class ForumTopicCreated(
    val name: String,
    val iconColor: Long,
    val iconCustomEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("ForumTopicCreated").prop("name", name).prop("iconColor", iconColor).prop("iconCustomEmojiId", iconCustomEmojiId).toString()
}

/**
 * This object represents a service message about a forum topic closed in the chat. Currently holds no information.
 */
@Serializable
data object ForumTopicClosed

/**
 * This object represents a service message about an edited forum topic.
 * @param name Optional. New name of the topic, if it was edited
 * @param iconCustomEmojiId Optional. New identifier of the custom emoji shown as the topic icon, if it was edited; an empty string if the icon was removed
 */
@Serializable
data class ForumTopicEdited(
    val name: String? = null,
    val iconCustomEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("ForumTopicEdited").prop("name", name).prop("iconCustomEmojiId", iconCustomEmojiId).toString()
}

/**
 * This object represents a service message about a forum topic reopened in the chat. Currently holds no information.
 */
@Serializable
data object ForumTopicReopened

/**
 * This object represents a service message about General forum topic hidden in the chat. Currently holds no information.
 */
@Serializable
data object GeneralForumTopicHidden

/**
 * This object represents a service message about General forum topic unhidden in the chat. Currently holds no information.
 */
@Serializable
data object GeneralForumTopicUnhidden

/**
 * This object contains information about the user whose identifier was shared with the bot using a KeyboardButtonRequestUser button.
 * @param requestId Identifier of the request
 * @param userId Identifier of the shared user. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the user and could be unable to use this identifier, unless the user is already known to the bot by some other means.
 */
@Serializable
data class UserShared(
    val requestId: Long,
    val userId: UserId,
) {
    override fun toString() = DebugStringBuilder("UserShared").prop("requestId", requestId).prop("userId", userId).toString()
}

/**
 * This object contains information about the chat whose identifier was shared with the bot using a KeyboardButtonRequestChat button.
 * @param requestId Identifier of the request
 * @param chatId Identifier of the shared chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the chat and could be unable to use this identifier, unless the chat is already known to the bot by some other means.
 */
@Serializable
data class ChatShared(
    val requestId: Long,
    val chatId: ChatId,
) {
    override fun toString() = DebugStringBuilder("ChatShared").prop("requestId", requestId).prop("chatId", chatId).toString()
}

/**
 * This object represents a service message about a user allowing a bot to write messages after adding the bot to the attachment menu or launching a Web App from a link.
 * @param webAppName Optional. Name of the Web App which was launched from a link
 */
@Serializable
data class WriteAccessAllowed(
    val webAppName: String? = null,
) {
    override fun toString() = DebugStringBuilder("WriteAccessAllowed").prop("webAppName", webAppName).toString()
}

/**
 * This object represents a service message about a video chat scheduled in the chat.
 * @param startDate Point in time (Unix timestamp) when the video chat is supposed to be started by a chat administrator
 */
@Serializable
data class VideoChatScheduled(
    val startDate: UnixTimestamp,
) {
    override fun toString() = DebugStringBuilder("VideoChatScheduled").prop("startDate", startDate).toString()
}

/**
 * This object represents a service message about a video chat started in the chat. Currently holds no information.
 */
@Serializable
data object VideoChatStarted

/**
 * This object represents a service message about a video chat ended in the chat.
 * @param duration Video chat duration in seconds
 */
@Serializable
data class VideoChatEnded(
    val duration: Seconds,
) {
    override fun toString() = DebugStringBuilder("VideoChatEnded").prop("duration", duration).toString()
}

/**
 * This object represents a service message about new members invited to a video chat.
 * @param users New members that were invited to the video chat
 */
@Serializable
data class VideoChatParticipantsInvited(
    val users: List<User>,
) {
    override fun toString() = DebugStringBuilder("VideoChatParticipantsInvited").prop("users", users).toString()
}

/**
 * This object represent a user's profile pictures.
 * @param totalCount Total number of profile pictures the target user has
 * @param photos Requested profile pictures (in up to 4 sizes each)
 */
@Serializable
data class UserProfilePhotos(
    val totalCount: Long,
    val photos: List<List<PhotoSize>>,
) {
    override fun toString() = DebugStringBuilder("UserProfilePhotos").prop("totalCount", totalCount).prop("photos", photos).toString()
}

/**
 * This object represents a file ready to be downloaded. The file can be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile.
 * 
 * The maximum file size to download is 20 MB
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 * @param filePath Optional. File path. Use https://api.telegram.org/file/bot<token>/<file_path> to get the file.
 */
@Serializable
data class File(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val fileSize: Long? = null,
    val filePath: String? = null,
) {
    override fun toString() = DebugStringBuilder("File").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("fileSize", fileSize).prop("filePath", filePath).toString()
}

/**
 * Describes a Web App.
 * @param url An HTTPS URL of a Web App to be opened with additional data as specified in Initializing Web Apps
 */
@Serializable
data class WebAppInfo(
    val url: String,
) {
    override fun toString() = DebugStringBuilder("WebAppInfo").prop("url", url).toString()
}

/**
 * This object represents a custom keyboard with reply options (see Introduction to bots for details and examples).
 * @param keyboard Array of button rows, each represented by an Array of KeyboardButton objects
 * @param isPersistent Optional. Requests clients to always show the keyboard when the regular keyboard is hidden. Defaults to false, in which case the custom keyboard can be hidden and opened with a keyboard icon.
 * @param resizeKeyboard Optional. Requests clients to resize the keyboard vertically for optimal fit (e.g., make the keyboard smaller if there are just two rows of buttons). Defaults to false, in which case the custom keyboard is always of the same height as the app's standard keyboard.
 * @param oneTimeKeyboard Optional. Requests clients to hide the keyboard as soon as it's been used. The keyboard will still be available, but clients will automatically display the usual letter-keyboard in the chat - the user can press a special button in the input field to see the custom keyboard again. Defaults to false.
 * @param inputFieldPlaceholder Optional. The placeholder to be shown in the input field when the keyboard is active; 1-64 characters
 * @param selective Optional. Use this parameter if you want to show the keyboard to specific users only. Targets: 1) users that are @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id), sender of the original message. Example: A user requests to change the bot's language, bot replies to the request with a keyboard to select the new language. Other users in the group don't see the keyboard.
 */
@Serializable
data class ReplyKeyboardMarkup(
    val keyboard: List<List<KeyboardButton>>,
    val isPersistent: Boolean? = null,
    val resizeKeyboard: Boolean? = null,
    val oneTimeKeyboard: Boolean? = null,
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean? = null,
) : ReplyMarkup() {
    override fun toString() = DebugStringBuilder("ReplyKeyboardMarkup").prop("keyboard", keyboard).prop("isPersistent", isPersistent).prop("resizeKeyboard", resizeKeyboard).prop("oneTimeKeyboard", oneTimeKeyboard).prop("inputFieldPlaceholder", inputFieldPlaceholder).prop("selective", selective).toString()
}

/**
 * This object represents one button of the reply keyboard. For simple text buttons, String can be used instead of this object to specify the button text. The optional fields web_app, request_user, request_chat, request_contact, request_location, and request_poll are mutually exclusive.
 * @param text Text of the button. If none of the optional fields are used, it will be sent as a message when the button is pressed
 * @param requestUser Optional. If specified, pressing the button will open a list of suitable users. Tapping on any user will send their identifier to the bot in a “user_shared” service message. Available in private chats only.
 * @param requestChat Optional. If specified, pressing the button will open a list of suitable chats. Tapping on a chat will send its identifier to the bot in a “chat_shared” service message. Available in private chats only.
 * @param requestContact Optional. If True, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only.
 * @param requestLocation Optional. If True, the user's current location will be sent when the button is pressed. Available in private chats only.
 * @param requestPoll Optional. If specified, the user will be asked to create a poll and send it to the bot when the button is pressed. Available in private chats only.
 * @param webApp Optional. If specified, the described Web App will be launched when the button is pressed. The Web App will be able to send a “web_app_data” service message. Available in private chats only.
 */
@Serializable
data class KeyboardButton(
    val text: String,
    val requestUser: KeyboardButtonRequestUser? = null,
    val requestChat: KeyboardButtonRequestChat? = null,
    val requestContact: Boolean? = null,
    val requestLocation: Boolean? = null,
    val requestPoll: KeyboardButtonPollType? = null,
    val webApp: WebAppInfo? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButton").prop("text", text).prop("requestUser", requestUser).prop("requestChat", requestChat).prop("requestContact", requestContact).prop("requestLocation", requestLocation).prop("requestPoll", requestPoll).prop("webApp", webApp).toString()
}

/**
 * This object defines the criteria used to request a suitable user. The identifier of the selected user will be shared with the bot when the corresponding button is pressed. More about requesting users »
 * @param requestId Signed 32-bit identifier of the request, which will be received back in the UserShared object. Must be unique within the message
 * @param userIsBot Optional. Pass True to request a bot, pass False to request a regular user. If not specified, no additional restrictions are applied.
 * @param userIsPremium Optional. Pass True to request a premium user, pass False to request a non-premium user. If not specified, no additional restrictions are applied.
 */
@Serializable
data class KeyboardButtonRequestUser(
    val requestId: Long,
    val userIsBot: Boolean? = null,
    val userIsPremium: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButtonRequestUser").prop("requestId", requestId).prop("userIsBot", userIsBot).prop("userIsPremium", userIsPremium).toString()
}

/**
 * This object defines the criteria used to request a suitable chat. The identifier of the selected chat will be shared with the bot when the corresponding button is pressed. More about requesting chats »
 * @param requestId Signed 32-bit identifier of the request, which will be received back in the ChatShared object. Must be unique within the message
 * @param chatIsChannel Pass True to request a channel chat, pass False to request a group or a supergroup chat.
 * @param chatIsForum Optional. Pass True to request a forum supergroup, pass False to request a non-forum chat. If not specified, no additional restrictions are applied.
 * @param chatHasUsername Optional. Pass True to request a supergroup or a channel with a username, pass False to request a chat without a username. If not specified, no additional restrictions are applied.
 * @param chatIsCreated Optional. Pass True to request a chat owned by the user. Otherwise, no additional restrictions are applied.
 * @param userAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the user in the chat. The rights must be a superset of bot_administrator_rights. If not specified, no additional restrictions are applied.
 * @param botAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the bot in the chat. The rights must be a subset of user_administrator_rights. If not specified, no additional restrictions are applied.
 * @param botIsMember Optional. Pass True to request a chat with the bot as a member. Otherwise, no additional restrictions are applied.
 */
@Serializable
data class KeyboardButtonRequestChat(
    val requestId: Long,
    val chatIsChannel: Boolean,
    val chatIsForum: Boolean? = null,
    val chatHasUsername: Boolean? = null,
    val chatIsCreated: Boolean? = null,
    val userAdministratorRights: ChatAdministratorRights? = null,
    val botAdministratorRights: ChatAdministratorRights? = null,
    val botIsMember: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButtonRequestChat").prop("requestId", requestId).prop("chatIsChannel", chatIsChannel).prop("chatIsForum", chatIsForum).prop("chatHasUsername", chatHasUsername).prop("chatIsCreated", chatIsCreated).prop("userAdministratorRights", userAdministratorRights).prop("botAdministratorRights", botAdministratorRights).prop("botIsMember", botIsMember).toString()
}

/**
 * This object represents type of a poll, which is allowed to be created and sent when the corresponding button is pressed.
 * @param type Optional. If quiz is passed, the user will be allowed to create only polls in the quiz mode. If regular is passed, only regular polls will be allowed. Otherwise, the user will be allowed to create a poll of any type.
 */
@Serializable
data class KeyboardButtonPollType(
    val type: String? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButtonPollType").prop("type", type).toString()
}

/**
 * Upon receiving a message with this object, Telegram clients will remove the current custom keyboard and display the default letter-keyboard. By default, custom keyboards are displayed until a new keyboard is sent by a bot. An exception is made for one-time keyboards that are hidden immediately after the user presses a button (see ReplyKeyboardMarkup).
 * @param removeKeyboard Requests clients to remove the custom keyboard (user will not be able to summon this keyboard; if you want to hide the keyboard from sight but keep it accessible, use one_time_keyboard in ReplyKeyboardMarkup)
 * @param selective Optional. Use this parameter if you want to remove the keyboard for specific users only. Targets: 1) users that are @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id), sender of the original message. Example: A user votes in a poll, bot returns confirmation message in reply to the vote and removes the keyboard for that user, while still showing the keyboard with poll options to users who haven't voted yet.
 */
@Serializable
data class ReplyKeyboardRemove(
    val removeKeyboard: Boolean,
    val selective: Boolean? = null,
) : ReplyMarkup() {
    override fun toString() = DebugStringBuilder("ReplyKeyboardRemove").prop("removeKeyboard", removeKeyboard).prop("selective", selective).toString()
}

/**
 * This object represents an inline keyboard that appears right next to the message it belongs to.
 * @param inlineKeyboard Array of button rows, each represented by an Array of InlineKeyboardButton objects
 */
@Serializable
data class InlineKeyboardMarkup(
    val inlineKeyboard: List<List<InlineKeyboardButton>>,
) : ReplyMarkup() {
    override fun toString() = DebugStringBuilder("InlineKeyboardMarkup").prop("inlineKeyboard", inlineKeyboard).toString()
}

/**
 * This object represents one button of an inline keyboard. You must use exactly one of the optional fields.
 * @param text Label text on the button
 * @param url Optional. HTTP or tg:// URL to be opened when the button is pressed. Links tg://user?id=<user_id> can be used to mention a user by their ID without using a username, if this is allowed by their privacy settings.
 * @param callbackData Optional. Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes
 * @param webApp Optional. Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery. Available only in private chats between a user and the bot.
 * @param loginUrl Optional. An HTTPS URL used to automatically authorize the user. Can be used as a replacement for the Telegram Login Widget.
 * @param switchInlineQuery Optional. If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the bot's username and the specified inline query in the input field. May be empty, in which case just the bot's username will be inserted.
 * @param switchInlineQueryCurrentChat Optional. If set, pressing the button will insert the bot's username and the specified inline query in the current chat's input field. May be empty, in which case only the bot's username will be inserted. This offers a quick way for the user to open your bot in inline mode in the same chat - good for selecting something from multiple options.
 * @param switchInlineQueryChosenChat Optional. If set, pressing the button will prompt the user to select one of their chats of the specified type, open that chat and insert the bot's username and the specified inline query in the input field
 * @param callbackGame Optional. Description of the game that will be launched when the user presses the button. NOTE: This type of button must always be the first button in the first row.
 * @param pay Optional. Specify True, to send a Pay button. NOTE: This type of button must always be the first button in the first row and can only be used in invoice messages.
 */
@Serializable
data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val callbackData: String? = null,
    val webApp: WebAppInfo? = null,
    val loginUrl: LoginUrl? = null,
    val switchInlineQuery: String? = null,
    val switchInlineQueryCurrentChat: String? = null,
    val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null,
    val callbackGame: CallbackGame? = null,
    val pay: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("InlineKeyboardButton").prop("text", text).prop("url", url).prop("callbackData", callbackData).prop("webApp", webApp).prop("loginUrl", loginUrl).prop("switchInlineQuery", switchInlineQuery).prop("switchInlineQueryCurrentChat", switchInlineQueryCurrentChat).prop("switchInlineQueryChosenChat", switchInlineQueryChosenChat).prop("callbackGame", callbackGame).prop("pay", pay).toString()
}

/**
 * This object represents a parameter of the inline keyboard button used to automatically authorize a user. Serves as a great replacement for the Telegram Login Widget when the user is coming from Telegram. All the user needs to do is tap/click a button and confirm that they want to log in:
 * 
 * 
 * 
 * Telegram apps support these buttons as of version 5.7.
 * 
 * Sample bot: @discussbot
 * @param url An HTTPS URL to be opened with user authorization data added to the query string when the button is pressed. If the user refuses to provide authorization data, the original URL without information about the user will be opened. The data added is the same as described in Receiving authorization data. NOTE: You must always check the hash of the received data to verify the authentication and the integrity of the data as described in Checking authorization.
 * @param forwardText Optional. New text of the button in forwarded messages.
 * @param botUsername Optional. Username of a bot, which will be used for user authorization. See Setting up a bot for more details. If not specified, the current bot's username will be assumed. The url's domain must be the same as the domain linked with the bot. See Linking your domain to the bot for more details.
 * @param requestWriteAccess Optional. Pass True to request the permission for your bot to send messages to the user.
 */
@Serializable
data class LoginUrl(
    val url: String,
    val forwardText: String? = null,
    val botUsername: String? = null,
    val requestWriteAccess: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("LoginUrl").prop("url", url).prop("forwardText", forwardText).prop("botUsername", botUsername).prop("requestWriteAccess", requestWriteAccess).toString()
}

/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat, with an optional default inline query.
 * @param query Optional. The default inline query to be inserted in the input field. If left empty, only the bot's username will be inserted
 * @param allowUserChats Optional. True, if private chats with users can be chosen
 * @param allowBotChats Optional. True, if private chats with bots can be chosen
 * @param allowGroupChats Optional. True, if group and supergroup chats can be chosen
 * @param allowChannelChats Optional. True, if channel chats can be chosen
 */
@Serializable
data class SwitchInlineQueryChosenChat(
    val query: String? = null,
    val allowUserChats: Boolean? = null,
    val allowBotChats: Boolean? = null,
    val allowGroupChats: Boolean? = null,
    val allowChannelChats: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("SwitchInlineQueryChosenChat").prop("query", query).prop("allowUserChats", allowUserChats).prop("allowBotChats", allowBotChats).prop("allowGroupChats", allowGroupChats).prop("allowChannelChats", allowChannelChats).toString()
}

/**
 * This object represents an incoming callback query from a callback button in an inline keyboard. If the button that originated the query was attached to a message sent by the bot, the field message will be present. If the button was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present. Exactly one of the fields data or game_short_name will be present.
 * @param id Unique identifier for this query
 * @param from Sender
 * @param chatInstance Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent. Useful for high scores in games.
 * @param message Optional. Message with the callback button that originated the query. Note that message content and message date will not be available if the message is too old
 * @param inlineMessageId Optional. Identifier of the message sent via the bot in inline mode, that originated the query.
 * @param data Optional. Data associated with the callback button. Be aware that the message originated the query can contain no callback buttons with this data.
 * @param gameShortName Optional. Short name of a Game to be returned, serves as the unique identifier for the game
 */
@Serializable
data class CallbackQuery(
    val id: CallbackQueryId,
    val from: User,
    val chatInstance: String,
    val message: Message? = null,
    val inlineMessageId: InlineMessageId? = null,
    val data: String? = null,
    val gameShortName: String? = null,
) {
    override fun toString() = DebugStringBuilder("CallbackQuery").prop("id", id).prop("from", from).prop("chatInstance", chatInstance).prop("message", message).prop("inlineMessageId", inlineMessageId).prop("data", data).prop("gameShortName", gameShortName).toString()
}

/**
 * Upon receiving a message with this object, Telegram clients will display a reply interface to the user (act as if the user has selected the bot's message and tapped 'Reply'). This can be extremely useful if you want to create user-friendly step-by-step interfaces without having to sacrifice privacy mode.
 * @param forceReply Shows reply interface to the user, as if they manually selected the bot's message and tapped 'Reply'
 * @param inputFieldPlaceholder Optional. The placeholder to be shown in the input field when the reply is active; 1-64 characters
 * @param selective Optional. Use this parameter if you want to force reply from specific users only. Targets: 1) users that are @mentioned in the text of the Message object; 2) if the bot's message is a reply (has reply_to_message_id), sender of the original message.
 */
@Serializable
data class ForceReply(
    val forceReply: Boolean,
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean? = null,
) : ReplyMarkup() {
    override fun toString() = DebugStringBuilder("ForceReply").prop("forceReply", forceReply).prop("inputFieldPlaceholder", inputFieldPlaceholder).prop("selective", selective).toString()
}

/**
 * This object represents a chat photo.
 * @param smallFileId File identifier of small (160x160) chat photo. This file_id can be used only for photo download and only for as long as the photo is not changed.
 * @param smallFileUniqueId Unique file identifier of small (160x160) chat photo, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param bigFileId File identifier of big (640x640) chat photo. This file_id can be used only for photo download and only for as long as the photo is not changed.
 * @param bigFileUniqueId Unique file identifier of big (640x640) chat photo, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 */
@Serializable
data class ChatPhoto(
    val smallFileId: FileId,
    val smallFileUniqueId: FileUniqueId,
    val bigFileId: FileId,
    val bigFileUniqueId: FileUniqueId,
) {
    override fun toString() = DebugStringBuilder("ChatPhoto").prop("smallFileId", smallFileId).prop("smallFileUniqueId", smallFileUniqueId).prop("bigFileId", bigFileId).prop("bigFileUniqueId", bigFileUniqueId).toString()
}

/**
 * Represents an invite link for a chat.
 * @param inviteLink The invite link. If the link was created by another chat administrator, then the second part of the link will be replaced with “…”.
 * @param creator Creator of the link
 * @param createsJoinRequest True, if users joining the chat via the link need to be approved by chat administrators
 * @param isPrimary True, if the link is primary
 * @param isRevoked True, if the link is revoked
 * @param name Optional. Invite link name
 * @param expireDate Optional. Point in time (Unix timestamp) when the link will expire or has been expired
 * @param memberLimit Optional. The maximum number of users that can be members of the chat simultaneously after joining the chat via this invite link; 1-99999
 * @param pendingJoinRequestCount Optional. Number of pending join requests created using this link
 */
@Serializable
data class ChatInviteLink(
    val inviteLink: String,
    val creator: User,
    val createsJoinRequest: Boolean,
    val isPrimary: Boolean,
    val isRevoked: Boolean,
    val name: String? = null,
    val expireDate: UnixTimestamp? = null,
    val memberLimit: Long? = null,
    val pendingJoinRequestCount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("ChatInviteLink").prop("inviteLink", inviteLink).prop("creator", creator).prop("createsJoinRequest", createsJoinRequest).prop("isPrimary", isPrimary).prop("isRevoked", isRevoked).prop("name", name).prop("expireDate", expireDate).prop("memberLimit", memberLimit).prop("pendingJoinRequestCount", pendingJoinRequestCount).toString()
}

/**
 * Represents the rights of an administrator in a chat.
 * @param isAnonymous True, if the user's presence in the chat is hidden
 * @param canManageChat True, if the administrator can access the chat event log, chat statistics, message statistics in channels, see channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any other administrator privilege
 * @param canDeleteMessages True, if the administrator can delete messages of other users
 * @param canManageVideoChats True, if the administrator can manage video chats
 * @param canRestrictMembers True, if the administrator can restrict, ban or unban chat members
 * @param canPromoteMembers True, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
 * @param canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @param canInviteUsers True, if the user is allowed to invite new users to the chat
 * @param canPostMessages Optional. True, if the administrator can post in the channel; channels only
 * @param canEditMessages Optional. True, if the administrator can edit messages of other users and can pin messages; channels only
 * @param canPinMessages Optional. True, if the user is allowed to pin messages; groups and supergroups only
 * @param canManageTopics Optional. True, if the user is allowed to create, rename, close, and reopen forum topics; supergroups only
 */
@Serializable
data class ChatAdministratorRights(
    val isAnonymous: Boolean,
    val canManageChat: Boolean,
    val canDeleteMessages: Boolean,
    val canManageVideoChats: Boolean,
    val canRestrictMembers: Boolean,
    val canPromoteMembers: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ChatAdministratorRights").prop("isAnonymous", isAnonymous).prop("canManageChat", canManageChat).prop("canDeleteMessages", canDeleteMessages).prop("canManageVideoChats", canManageVideoChats).prop("canRestrictMembers", canRestrictMembers).prop("canPromoteMembers", canPromoteMembers).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPostMessages", canPostMessages).prop("canEditMessages", canEditMessages).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).toString()
}

/**
 * This object contains information about one member of a chat. Currently, the following 6 types of chat members are supported:
 * - [ChatMemberOwner]
 * - [ChatMemberAdministrator]
 * - [ChatMemberMember]
 * - [ChatMemberRestricted]
 * - [ChatMemberLeft]
 * - [ChatMemberBanned]
 */ 
@Serializable
@JsonClassDiscriminator("status")
sealed class ChatMember

/**
 * Represents a chat member that owns the chat and has all administrator privileges.
 * @param user Information about the user
 * @param isAnonymous True, if the user's presence in the chat is hidden
 * @param customTitle Optional. Custom title for this user
 */
@Serializable
@SerialName("creator")
data class ChatMemberOwner(
    val user: User,
    val isAnonymous: Boolean,
    val customTitle: String? = null,
) : ChatMember() {
    override fun toString() = DebugStringBuilder("ChatMemberOwner").prop("user", user).prop("isAnonymous", isAnonymous).prop("customTitle", customTitle).toString()
}

/**
 * Represents a chat member that has some additional privileges.
 * @param user Information about the user
 * @param canBeEdited True, if the bot is allowed to edit administrator privileges of that user
 * @param isAnonymous True, if the user's presence in the chat is hidden
 * @param canManageChat True, if the administrator can access the chat event log, chat statistics, message statistics in channels, see channel members, see anonymous administrators in supergroups and ignore slow mode. Implied by any other administrator privilege
 * @param canDeleteMessages True, if the administrator can delete messages of other users
 * @param canManageVideoChats True, if the administrator can manage video chats
 * @param canRestrictMembers True, if the administrator can restrict, ban or unban chat members
 * @param canPromoteMembers True, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
 * @param canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @param canInviteUsers True, if the user is allowed to invite new users to the chat
 * @param canPostMessages Optional. True, if the administrator can post in the channel; channels only
 * @param canEditMessages Optional. True, if the administrator can edit messages of other users and can pin messages; channels only
 * @param canPinMessages Optional. True, if the user is allowed to pin messages; groups and supergroups only
 * @param canManageTopics Optional. True, if the user is allowed to create, rename, close, and reopen forum topics; supergroups only
 * @param customTitle Optional. Custom title for this user
 */
@Serializable
@SerialName("administrator")
data class ChatMemberAdministrator(
    val user: User,
    val canBeEdited: Boolean,
    val isAnonymous: Boolean,
    val canManageChat: Boolean,
    val canDeleteMessages: Boolean,
    val canManageVideoChats: Boolean,
    val canRestrictMembers: Boolean,
    val canPromoteMembers: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
    val customTitle: String? = null,
) : ChatMember() {
    override fun toString() = DebugStringBuilder("ChatMemberAdministrator").prop("user", user).prop("canBeEdited", canBeEdited).prop("isAnonymous", isAnonymous).prop("canManageChat", canManageChat).prop("canDeleteMessages", canDeleteMessages).prop("canManageVideoChats", canManageVideoChats).prop("canRestrictMembers", canRestrictMembers).prop("canPromoteMembers", canPromoteMembers).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPostMessages", canPostMessages).prop("canEditMessages", canEditMessages).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).prop("customTitle", customTitle).toString()
}

/**
 * Represents a chat member that has no additional privileges or restrictions.
 * @param user Information about the user
 */
@Serializable
@SerialName("member")
data class ChatMemberMember(
    val user: User,
) : ChatMember() {
    override fun toString() = DebugStringBuilder("ChatMemberMember").prop("user", user).toString()
}

/**
 * Represents a chat member that is under certain restrictions in the chat. Supergroups only.
 * @param user Information about the user
 * @param isMember True, if the user is a member of the chat at the moment of the request
 * @param canSendMessages True, if the user is allowed to send text messages, contacts, invoices, locations and venues
 * @param canSendAudios True, if the user is allowed to send audios
 * @param canSendDocuments True, if the user is allowed to send documents
 * @param canSendPhotos True, if the user is allowed to send photos
 * @param canSendVideos True, if the user is allowed to send videos
 * @param canSendVideoNotes True, if the user is allowed to send video notes
 * @param canSendVoiceNotes True, if the user is allowed to send voice notes
 * @param canSendPolls True, if the user is allowed to send polls
 * @param canSendOtherMessages True, if the user is allowed to send animations, games, stickers and use inline bots
 * @param canAddWebPagePreviews True, if the user is allowed to add web page previews to their messages
 * @param canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @param canInviteUsers True, if the user is allowed to invite new users to the chat
 * @param canPinMessages True, if the user is allowed to pin messages
 * @param canManageTopics True, if the user is allowed to create forum topics
 * @param untilDate Date when restrictions will be lifted for this user; unix time. If 0, then the user is restricted forever
 */
@Serializable
@SerialName("restricted")
data class ChatMemberRestricted(
    val user: User,
    val isMember: Boolean,
    val canSendMessages: Boolean,
    val canSendAudios: Boolean,
    val canSendDocuments: Boolean,
    val canSendPhotos: Boolean,
    val canSendVideos: Boolean,
    val canSendVideoNotes: Boolean,
    val canSendVoiceNotes: Boolean,
    val canSendPolls: Boolean,
    val canSendOtherMessages: Boolean,
    val canAddWebPagePreviews: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPinMessages: Boolean,
    val canManageTopics: Boolean,
    val untilDate: UnixTimestamp,
) : ChatMember() {
    override fun toString() = DebugStringBuilder("ChatMemberRestricted").prop("user", user).prop("isMember", isMember).prop("canSendMessages", canSendMessages).prop("canSendAudios", canSendAudios).prop("canSendDocuments", canSendDocuments).prop("canSendPhotos", canSendPhotos).prop("canSendVideos", canSendVideos).prop("canSendVideoNotes", canSendVideoNotes).prop("canSendVoiceNotes", canSendVoiceNotes).prop("canSendPolls", canSendPolls).prop("canSendOtherMessages", canSendOtherMessages).prop("canAddWebPagePreviews", canAddWebPagePreviews).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).prop("untilDate", untilDate).toString()
}

/**
 * Represents a chat member that isn't currently a member of the chat, but may join it themselves.
 * @param user Information about the user
 */
@Serializable
@SerialName("left")
data class ChatMemberLeft(
    val user: User,
) : ChatMember() {
    override fun toString() = DebugStringBuilder("ChatMemberLeft").prop("user", user).toString()
}

/**
 * Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
 * @param user Information about the user
 * @param untilDate Date when restrictions will be lifted for this user; unix time. If 0, then the user is banned forever
 */
@Serializable
@SerialName("kicked")
data class ChatMemberBanned(
    val user: User,
    val untilDate: UnixTimestamp,
) : ChatMember() {
    override fun toString() = DebugStringBuilder("ChatMemberBanned").prop("user", user).prop("untilDate", untilDate).toString()
}

/**
 * This object represents changes in the status of a chat member.
 * @param chat Chat the user belongs to
 * @param from Performer of the action, which resulted in the change
 * @param date Date the change was done in Unix time
 * @param oldChatMember Previous information about the chat member
 * @param newChatMember New information about the chat member
 * @param inviteLink Optional. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
 * @param viaChatFolderInviteLink Optional. True, if the user joined the chat via a chat folder invite link
 */
@Serializable
data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: Long,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    val viaChatFolderInviteLink: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ChatMemberUpdated").prop("chat", chat).prop("from", from).prop("date", date).prop("oldChatMember", oldChatMember).prop("newChatMember", newChatMember).prop("inviteLink", inviteLink).prop("viaChatFolderInviteLink", viaChatFolderInviteLink).toString()
}

/**
 * Represents a join request sent to a chat.
 * @param chat Chat to which the request was sent
 * @param from User that sent the join request
 * @param userChatId Identifier of a private chat with the user who sent the join request. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot can use this identifier for 24 hours to send messages until the join request is processed, assuming no other administrator contacted the user.
 * @param date Date the request was sent in Unix time
 * @param bio Optional. Bio of the user.
 * @param inviteLink Optional. Chat invite link that was used by the user to send the join request
 */
@Serializable
data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val userChatId: ChatId,
    val date: Long,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
) {
    override fun toString() = DebugStringBuilder("ChatJoinRequest").prop("chat", chat).prop("from", from).prop("userChatId", userChatId).prop("date", date).prop("bio", bio).prop("inviteLink", inviteLink).toString()
}

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 * @param canSendMessages Optional. True, if the user is allowed to send text messages, contacts, invoices, locations and venues
 * @param canSendAudios Optional. True, if the user is allowed to send audios
 * @param canSendDocuments Optional. True, if the user is allowed to send documents
 * @param canSendPhotos Optional. True, if the user is allowed to send photos
 * @param canSendVideos Optional. True, if the user is allowed to send videos
 * @param canSendVideoNotes Optional. True, if the user is allowed to send video notes
 * @param canSendVoiceNotes Optional. True, if the user is allowed to send voice notes
 * @param canSendPolls Optional. True, if the user is allowed to send polls
 * @param canSendOtherMessages Optional. True, if the user is allowed to send animations, games, stickers and use inline bots
 * @param canAddWebPagePreviews Optional. True, if the user is allowed to add web page previews to their messages
 * @param canChangeInfo Optional. True, if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups
 * @param canInviteUsers Optional. True, if the user is allowed to invite new users to the chat
 * @param canPinMessages Optional. True, if the user is allowed to pin messages. Ignored in public supergroups
 * @param canManageTopics Optional. True, if the user is allowed to create forum topics. If omitted defaults to the value of can_pin_messages
 */
@Serializable
data class ChatPermissions(
    val canSendMessages: Boolean? = null,
    val canSendAudios: Boolean? = null,
    val canSendDocuments: Boolean? = null,
    val canSendPhotos: Boolean? = null,
    val canSendVideos: Boolean? = null,
    val canSendVideoNotes: Boolean? = null,
    val canSendVoiceNotes: Boolean? = null,
    val canSendPolls: Boolean? = null,
    val canSendOtherMessages: Boolean? = null,
    val canAddWebPagePreviews: Boolean? = null,
    val canChangeInfo: Boolean? = null,
    val canInviteUsers: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ChatPermissions").prop("canSendMessages", canSendMessages).prop("canSendAudios", canSendAudios).prop("canSendDocuments", canSendDocuments).prop("canSendPhotos", canSendPhotos).prop("canSendVideos", canSendVideos).prop("canSendVideoNotes", canSendVideoNotes).prop("canSendVoiceNotes", canSendVoiceNotes).prop("canSendPolls", canSendPolls).prop("canSendOtherMessages", canSendOtherMessages).prop("canAddWebPagePreviews", canAddWebPagePreviews).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).toString()
}

/**
 * Represents a location to which a chat is connected.
 * @param location The location to which the supergroup is connected. Can't be a live location.
 * @param address Location address; 1-64 characters, as defined by the chat owner
 */
@Serializable
data class ChatLocation(
    val location: Location,
    val address: String,
) {
    override fun toString() = DebugStringBuilder("ChatLocation").prop("location", location).prop("address", address).toString()
}

/**
 * This object represents a forum topic.
 * @param messageThreadId Unique identifier of the forum topic
 * @param name Name of the topic
 * @param iconColor Color of the topic icon in RGB format
 * @param iconCustomEmojiId Optional. Unique identifier of the custom emoji shown as the topic icon
 */
@Serializable
data class ForumTopic(
    val messageThreadId: MessageThreadId,
    val name: String,
    val iconColor: Long,
    val iconCustomEmojiId: CustomEmojiId? = null,
) {
    override fun toString() = DebugStringBuilder("ForumTopic").prop("messageThreadId", messageThreadId).prop("name", name).prop("iconColor", iconColor).prop("iconCustomEmojiId", iconCustomEmojiId).toString()
}

/**
 * This object represents a bot command.
 * @param command Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores.
 * @param description Description of the command; 1-256 characters.
 */
@Serializable
data class BotCommand(
    val command: String,
    val description: String,
) {
    override fun toString() = DebugStringBuilder("BotCommand").prop("command", command).prop("description", description).toString()
}

/**
 * This object represents the scope to which bot commands are applied. Currently, the following 7 scopes are supported:
 * - [BotCommandScopeDefault]
 * - [BotCommandScopeAllPrivateChats]
 * - [BotCommandScopeAllGroupChats]
 * - [BotCommandScopeAllChatAdministrators]
 * - [BotCommandScopeChat]
 * - [BotCommandScopeChatAdministrators]
 * - [BotCommandScopeChatMember]
 */ 
@Serializable
@JsonClassDiscriminator("type")
sealed class BotCommandScope

/**
 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
 */
@Serializable
@SerialName("default")
data object BotCommandScopeDefault : BotCommandScope()

/**
 * Represents the scope of bot commands, covering all private chats.
 */
@Serializable
@SerialName("all_private_chats")
data object BotCommandScopeAllPrivateChats : BotCommandScope()

/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 */
@Serializable
@SerialName("all_group_chats")
data object BotCommandScopeAllGroupChats : BotCommandScope()

/**
 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
 */
@Serializable
@SerialName("all_chat_administrators")
data object BotCommandScopeAllChatAdministrators : BotCommandScope()

/**
 * Represents the scope of bot commands, covering a specific chat.
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
@SerialName("chat")
data class BotCommandScopeChat(
    val chatId: ChatId,
) : BotCommandScope() {
    override fun toString() = DebugStringBuilder("BotCommandScopeChat").prop("chatId", chatId).toString()
}

/**
 * Represents the scope of bot commands, covering all administrators of a specific group or supergroup chat.
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
@SerialName("chat_administrators")
data class BotCommandScopeChatAdministrators(
    val chatId: ChatId,
) : BotCommandScope() {
    override fun toString() = DebugStringBuilder("BotCommandScopeChatAdministrators").prop("chatId", chatId).toString()
}

/**
 * Represents the scope of bot commands, covering a specific member of a group or supergroup chat.
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 * @param userId Unique identifier of the target user
 */
@Serializable
@SerialName("chat_member")
data class BotCommandScopeChatMember(
    val chatId: ChatId,
    val userId: UserId,
) : BotCommandScope() {
    override fun toString() = DebugStringBuilder("BotCommandScopeChatMember").prop("chatId", chatId).prop("userId", userId).toString()
}

/**
 * This object represents the bot's name.
 * @param name The bot's name
 */
@Serializable
data class BotName(
    val name: String,
) {
    override fun toString() = DebugStringBuilder("BotName").prop("name", name).toString()
}

/**
 * This object represents the bot's description.
 * @param description The bot's description
 */
@Serializable
data class BotDescription(
    val description: String,
) {
    override fun toString() = DebugStringBuilder("BotDescription").prop("description", description).toString()
}

/**
 * This object represents the bot's short description.
 * @param shortDescription The bot's short description
 */
@Serializable
data class BotShortDescription(
    val shortDescription: String,
) {
    override fun toString() = DebugStringBuilder("BotShortDescription").prop("shortDescription", shortDescription).toString()
}

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 * - [MenuButtonCommands]
 * - [MenuButtonWebApp]
 * - [MenuButtonDefault]
 */ 
@Serializable
@JsonClassDiscriminator("type")
sealed class MenuButton

/**
 * Represents a menu button, which opens the bot's list of commands.
 */
@Serializable
@SerialName("commands")
data object MenuButtonCommands : MenuButton()

/**
 * Represents a menu button, which launches a Web App.
 * @param text Text on the button
 * @param webApp Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery.
 */
@Serializable
@SerialName("web_app")
data class MenuButtonWebApp(
    val text: String,
    val webApp: WebAppInfo,
) : MenuButton() {
    override fun toString() = DebugStringBuilder("MenuButtonWebApp").prop("text", text).prop("webApp", webApp).toString()
}

/**
 * Describes that no specific value for the menu button was set.
 */
@Serializable
@SerialName("default")
data object MenuButtonDefault : MenuButton()

/**
 * Describes why a request was unsuccessful.
 * @param migrateToChatId Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param retryAfter Optional. In case of exceeding flood control, the number of seconds left to wait before the request can be repeated
 */
@Serializable
data class ResponseParameters(
    val migrateToChatId: ChatId? = null,
    val retryAfter: Seconds? = null,
) {
    override fun toString() = DebugStringBuilder("ResponseParameters").prop("migrateToChatId", migrateToChatId).prop("retryAfter", retryAfter).toString()
}

/**
 * This object represents the content of a media message to be sent. It should be one of
 * - [InputMediaAnimation]
 * - [InputMediaDocument]
 * - [InputMediaAudio]
 * - [InputMediaPhoto]
 * - [InputMediaVideo]
 */ 
@Serializable
@JsonClassDiscriminator("type")
sealed class InputMedia

/**
 * Represents a photo to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param hasSpoiler Optional. Pass True if the photo needs to be covered with a spoiler animation
 */
@Serializable
@SerialName("photo")
data class InputMediaPhoto(
    val media: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val hasSpoiler: Boolean? = null,
) : InputMedia() {
    override fun toString() = DebugStringBuilder("InputMediaPhoto").prop("media", media).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("hasSpoiler", hasSpoiler).toString()
}

/**
 * Represents a video to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Optional. Caption of the video to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param width Optional. Video width
 * @param height Optional. Video height
 * @param duration Optional. Video duration in seconds
 * @param supportsStreaming Optional. Pass True if the uploaded video is suitable for streaming
 * @param hasSpoiler Optional. Pass True if the video needs to be covered with a spoiler animation
 */
@Serializable
@SerialName("video")
data class InputMediaVideo(
    val media: String,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Seconds? = null,
    val supportsStreaming: Boolean? = null,
    val hasSpoiler: Boolean? = null,
) : InputMedia() {
    override fun toString() = DebugStringBuilder("InputMediaVideo").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("width", width).prop("height", height).prop("duration", duration).prop("supportsStreaming", supportsStreaming).prop("hasSpoiler", hasSpoiler).toString()
}

/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Optional. Caption of the animation to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the animation caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param width Optional. Animation width
 * @param height Optional. Animation height
 * @param duration Optional. Animation duration in seconds
 * @param hasSpoiler Optional. Pass True if the animation needs to be covered with a spoiler animation
 */
@Serializable
@SerialName("animation")
data class InputMediaAnimation(
    val media: String,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Seconds? = null,
    val hasSpoiler: Boolean? = null,
) : InputMedia() {
    override fun toString() = DebugStringBuilder("InputMediaAnimation").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("width", width).prop("height", height).prop("duration", duration).prop("hasSpoiler", hasSpoiler).toString()
}

/**
 * Represents an audio file to be treated as music to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Optional. Caption of the audio to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the audio caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param duration Optional. Duration of the audio in seconds
 * @param performer Optional. Performer of the audio
 * @param title Optional. Title of the audio
 */
@Serializable
@SerialName("audio")
data class InputMediaAudio(
    val media: String,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Seconds? = null,
    val performer: String? = null,
    val title: String? = null,
) : InputMedia() {
    override fun toString() = DebugStringBuilder("InputMediaAudio").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("duration", duration).prop("performer", performer).prop("title", title).toString()
}

/**
 * Represents a general file to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Optional. Caption of the document to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the document caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param disableContentTypeDetection Optional. Disables automatic server-side content type detection for files uploaded using multipart/form-data. Always True, if the document is sent as part of an album.
 */
@Serializable
@SerialName("document")
data class InputMediaDocument(
    val media: String,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean? = null,
) : InputMedia() {
    override fun toString() = DebugStringBuilder("InputMediaDocument").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("disableContentTypeDetection", disableContentTypeDetection).toString()
}

/**
 * This object represents a sticker.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param type Type of the sticker, currently one of “regular”, “mask”, “custom_emoji”. The type of the sticker is independent from its format, which is determined by the fields is_animated and is_video.
 * @param width Sticker width
 * @param height Sticker height
 * @param isAnimated True, if the sticker is animated
 * @param isVideo True, if the sticker is a video sticker
 * @param thumbnail Optional. Sticker thumbnail in the .WEBP or .JPG format
 * @param emoji Optional. Emoji associated with the sticker
 * @param setName Optional. Name of the sticker set to which the sticker belongs
 * @param premiumAnimation Optional. For premium regular stickers, premium animation for the sticker
 * @param maskPosition Optional. For mask stickers, the position where the mask should be placed
 * @param customEmojiId Optional. For custom emoji stickers, unique identifier of the custom emoji
 * @param needsRepainting Optional. True, if the sticker must be repainted to a text color in messages, the color of the Telegram Premium badge in emoji status, white color on chat photos, or another appropriate color in other places
 * @param fileSize Optional. File size in bytes
 */
@Serializable
data class Sticker(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val type: String,
    val width: Long,
    val height: Long,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val thumbnail: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val premiumAnimation: File? = null,
    val maskPosition: MaskPosition? = null,
    val customEmojiId: CustomEmojiId? = null,
    val needsRepainting: Boolean? = null,
    val fileSize: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Sticker").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("type", type).prop("width", width).prop("height", height).prop("isAnimated", isAnimated).prop("isVideo", isVideo).prop("thumbnail", thumbnail).prop("emoji", emoji).prop("setName", setName).prop("premiumAnimation", premiumAnimation).prop("maskPosition", maskPosition).prop("customEmojiId", customEmojiId).prop("needsRepainting", needsRepainting).prop("fileSize", fileSize).toString()
}

/**
 * This object represents a sticker set.
 * @param name Sticker set name
 * @param title Sticker set title
 * @param stickerType Type of stickers in the set, currently one of “regular”, “mask”, “custom_emoji”
 * @param isAnimated True, if the sticker set contains animated stickers
 * @param isVideo True, if the sticker set contains video stickers
 * @param stickers List of all set stickers
 * @param thumbnail Optional. Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format
 */
@Serializable
data class StickerSet(
    val name: String,
    val title: String,
    val stickerType: String,
    val isAnimated: Boolean,
    val isVideo: Boolean,
    val stickers: List<Sticker>,
    val thumbnail: PhotoSize? = null,
) {
    override fun toString() = DebugStringBuilder("StickerSet").prop("name", name).prop("title", title).prop("stickerType", stickerType).prop("isAnimated", isAnimated).prop("isVideo", isVideo).prop("stickers", stickers).prop("thumbnail", thumbnail).toString()
}

/**
 * This object describes the position on faces where a mask should be placed by default.
 * @param point The part of the face relative to which the mask should be placed. One of “forehead”, “eyes”, “mouth”, or “chin”.
 * @param xShift Shift by X-axis measured in widths of the mask scaled to the face size, from left to right. For example, choosing -1.0 will place mask just to the left of the default mask position.
 * @param yShift Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom. For example, 1.0 will place the mask just below the default mask position.
 * @param scale Mask scaling coefficient. For example, 2.0 means double size.
 */
@Serializable
data class MaskPosition(
    val point: String,
    val xShift: Double,
    val yShift: Double,
    val scale: Double,
) {
    override fun toString() = DebugStringBuilder("MaskPosition").prop("point", point).prop("xShift", xShift).prop("yShift", yShift).prop("scale", scale).toString()
}

/**
 * This object describes a sticker to be added to a sticker set.
 * @param sticker The added sticker. Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, upload a new one using multipart/form-data, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. Animated and video stickers can't be uploaded via HTTP URL. More information on Sending Files »
 * @param emojiList List of 1-20 emoji associated with the sticker
 * @param maskPosition Optional. Position where the mask should be placed on faces. For “mask” stickers only.
 * @param keywords Optional. List of 0-20 search keywords for the sticker with total length of up to 64 characters. For “regular” and “custom_emoji” stickers only.
 */
@Serializable
data class InputSticker(
    val sticker: String,
    val emojiList: List<String>,
    val maskPosition: MaskPosition? = null,
    val keywords: List<String>? = null,
) {
    override fun toString() = DebugStringBuilder("InputSticker").prop("sticker", sticker).prop("emojiList", emojiList).prop("maskPosition", maskPosition).prop("keywords", keywords).toString()
}

/**
 * This object represents an incoming inline query. When the user sends an empty query, your bot could return some default or trending results.
 * @param id Unique identifier for this query
 * @param from Sender
 * @param query Text of the query (up to 256 characters)
 * @param offset Offset of the results to be returned, can be controlled by the bot
 * @param chatType Optional. Type of the chat from which the inline query was sent. Can be either “sender” for a private chat with the inline query sender, “private”, “group”, “supergroup”, or “channel”. The chat type should be always known for requests sent from official clients and most third-party clients, unless the request was sent from a secret chat
 * @param location Optional. Sender location, only for bots that request user location
 */
@Serializable
data class InlineQuery(
    val id: InlineQueryId,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: String? = null,
    val location: Location? = null,
) {
    override fun toString() = DebugStringBuilder("InlineQuery").prop("id", id).prop("from", from).prop("query", query).prop("offset", offset).prop("chatType", chatType).prop("location", location).toString()
}

/**
 * This object represents a button to be shown above inline query results. You must use exactly one of the optional fields.
 * @param text Label text on the button
 * @param webApp Optional. Description of the Web App that will be launched when the user presses the button. The Web App will be able to switch back to the inline mode using the method switchInlineQuery inside the Web App.
 * @param startParameter Optional. Deep-linking parameter for the /start message sent to the bot when a user presses the button. 1-64 characters, only A-Z, a-z, 0-9, _ and - are allowed. Example: An inline bot that sends YouTube videos can ask the user to connect the bot to their YouTube account to adapt search results accordingly. To do this, it displays a 'Connect your YouTube account' button above the results, or even before showing any. The user presses the button, switches to a private chat with the bot and, in doing so, passes a start parameter that instructs the bot to return an OAuth link. Once done, the bot can offer a switch_inline button so that the user can easily return to the chat where they wanted to use the bot's inline capabilities.
 */
@Serializable
data class InlineQueryResultsButton(
    val text: String,
    val webApp: WebAppInfo? = null,
    val startParameter: String? = null,
) {
    override fun toString() = DebugStringBuilder("InlineQueryResultsButton").prop("text", text).prop("webApp", webApp).prop("startParameter", startParameter).toString()
}

/**
 * This object represents one result of an inline query. Telegram clients currently support results of the following 20 types:
 * - [InlineQueryResultCachedAudio]
 * - [InlineQueryResultCachedDocument]
 * - [InlineQueryResultCachedGif]
 * - [InlineQueryResultCachedMpeg4Gif]
 * - [InlineQueryResultCachedPhoto]
 * - [InlineQueryResultCachedSticker]
 * - [InlineQueryResultCachedVideo]
 * - [InlineQueryResultCachedVoice]
 * - [InlineQueryResultArticle]
 * - [InlineQueryResultAudio]
 * - [InlineQueryResultContact]
 * - [InlineQueryResultGame]
 * - [InlineQueryResultDocument]
 * - [InlineQueryResultGif]
 * - [InlineQueryResultLocation]
 * - [InlineQueryResultMpeg4Gif]
 * - [InlineQueryResultPhoto]
 * - [InlineQueryResultVenue]
 * - [InlineQueryResultVideo]
 * - [InlineQueryResultVoice]
 */ 
@Serializable
@JsonClassDiscriminator("type")
sealed class InlineQueryResult

/**
 * Represents a link to an article or web page.
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param title Title of the result
 * @param inputMessageContent Content of the message to be sent
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param url Optional. URL of the result
 * @param hideUrl Optional. Pass True if you don't want the URL to be shown in the message
 * @param description Optional. Short description of the result
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
@SerialName("article")
data class InlineQueryResultArticle(
    val id: InlineQueryResultId,
    val title: String,
    val inputMessageContent: InputMessageContent,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val url: String? = null,
    val hideUrl: Boolean? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Long? = null,
    val thumbnailHeight: Long? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultArticle").prop("id", id).prop("title", title).prop("inputMessageContent", inputMessageContent).prop("replyMarkup", replyMarkup).prop("url", url).prop("hideUrl", hideUrl).prop("description", description).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
}

/**
 * Represents a link to a photo. By default, this photo will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param photoUrl A valid URL of the photo. Photo must be in JPEG format. Photo size must not exceed 5MB
 * @param thumbnailUrl URL of the thumbnail for the photo
 * @param photoWidth Optional. Width of the photo
 * @param photoHeight Optional. Height of the photo
 * @param title Optional. Title for the result
 * @param description Optional. Short description of the result
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the photo
 */
@Serializable
@SerialName("photo")
data class InlineQueryResultPhoto(
    val id: InlineQueryResultId,
    val photoUrl: String,
    val thumbnailUrl: String,
    val photoWidth: Long? = null,
    val photoHeight: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultPhoto").prop("id", id).prop("photoUrl", photoUrl).prop("thumbnailUrl", thumbnailUrl).prop("photoWidth", photoWidth).prop("photoHeight", photoHeight).prop("title", title).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gifUrl A valid URL for the GIF file. File size must not exceed 1MB
 * @param thumbnailUrl URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
 * @param gifWidth Optional. Width of the GIF
 * @param gifHeight Optional. Height of the GIF
 * @param gifDuration Optional. Duration of the GIF in seconds
 * @param thumbnailMimeType Optional. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the GIF animation
 */
@Serializable
@SerialName("gif")
data class InlineQueryResultGif(
    val id: InlineQueryResultId,
    val gifUrl: String,
    val thumbnailUrl: String,
    val gifWidth: Long? = null,
    val gifHeight: Long? = null,
    val gifDuration: Long? = null,
    val thumbnailMimeType: String? = null,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultGif").prop("id", id).prop("gifUrl", gifUrl).prop("thumbnailUrl", thumbnailUrl).prop("gifWidth", gifWidth).prop("gifHeight", gifHeight).prop("gifDuration", gifDuration).prop("thumbnailMimeType", thumbnailMimeType).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param mpeg4Url A valid URL for the MPEG4 file. File size must not exceed 1MB
 * @param thumbnailUrl URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
 * @param mpeg4Width Optional. Video width
 * @param mpeg4Height Optional. Video height
 * @param mpeg4Duration Optional. Video duration in seconds
 * @param thumbnailMimeType Optional. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the video animation
 */
@Serializable
@SerialName("mpeg4_gif")
data class InlineQueryResultMpeg4Gif(
    val id: InlineQueryResultId,
    val mpeg4Url: String,
    val thumbnailUrl: String,
    val mpeg4Width: Long? = null,
    val mpeg4Height: Long? = null,
    val mpeg4Duration: Long? = null,
    val thumbnailMimeType: String? = null,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultMpeg4Gif").prop("id", id).prop("mpeg4Url", mpeg4Url).prop("thumbnailUrl", thumbnailUrl).prop("mpeg4Width", mpeg4Width).prop("mpeg4Height", mpeg4Height).prop("mpeg4Duration", mpeg4Duration).prop("thumbnailMimeType", thumbnailMimeType).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
 * 
 * If an InlineQueryResultVideo message contains an embedded video (e.g., YouTube), you must replace its content using input_message_content.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param videoUrl A valid URL for the embedded video player or video file
 * @param mimeType MIME type of the content of the video URL, “text/html” or “video/mp4”
 * @param thumbnailUrl URL of the thumbnail (JPEG only) for the video
 * @param title Title for the result
 * @param caption Optional. Caption of the video to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param videoWidth Optional. Video width
 * @param videoHeight Optional. Video height
 * @param videoDuration Optional. Video duration in seconds
 * @param description Optional. Short description of the result
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the video. This field is required if InlineQueryResultVideo is used to send an HTML-page as a result (e.g., a YouTube video).
 */
@Serializable
@SerialName("video")
data class InlineQueryResultVideo(
    val id: InlineQueryResultId,
    val videoUrl: String,
    val mimeType: String,
    val thumbnailUrl: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val videoWidth: Long? = null,
    val videoHeight: Long? = null,
    val videoDuration: Long? = null,
    val description: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultVideo").prop("id", id).prop("videoUrl", videoUrl).prop("mimeType", mimeType).prop("thumbnailUrl", thumbnailUrl).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("videoWidth", videoWidth).prop("videoHeight", videoHeight).prop("videoDuration", videoDuration).prop("description", description).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to an MP3 audio file. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param audioUrl A valid URL for the audio file
 * @param title Title
 * @param caption Optional. Caption, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the audio caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param performer Optional. Performer
 * @param audioDuration Optional. Audio duration in seconds
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the audio
 */
@Serializable
@SerialName("audio")
data class InlineQueryResultAudio(
    val id: InlineQueryResultId,
    val audioUrl: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val performer: String? = null,
    val audioDuration: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultAudio").prop("id", id).prop("audioUrl", audioUrl).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("performer", performer).prop("audioDuration", audioDuration).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the the voice message.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param voiceUrl A valid URL for the voice recording
 * @param title Recording title
 * @param caption Optional. Caption, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param voiceDuration Optional. Recording duration in seconds
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the voice recording
 */
@Serializable
@SerialName("voice")
data class InlineQueryResultVoice(
    val id: InlineQueryResultId,
    val voiceUrl: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val voiceDuration: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultVoice").prop("id", id).prop("voiceUrl", voiceUrl).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("voiceDuration", voiceDuration).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a file. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file. Currently, only .PDF and .ZIP files can be sent using this method.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param title Title for the result
 * @param documentUrl A valid URL for the file
 * @param mimeType MIME type of the content of the file, either “application/pdf” or “application/zip”
 * @param caption Optional. Caption of the document to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the document caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param description Optional. Short description of the result
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the file
 * @param thumbnailUrl Optional. URL of the thumbnail (JPEG only) for the file
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
@SerialName("document")
data class InlineQueryResultDocument(
    val id: InlineQueryResultId,
    val title: String,
    val documentUrl: String,
    val mimeType: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val description: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Long? = null,
    val thumbnailHeight: Long? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultDocument").prop("id", id).prop("title", title).prop("documentUrl", documentUrl).prop("mimeType", mimeType).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("description", description).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
}

/**
 * Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the location.
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param latitude Location latitude in degrees
 * @param longitude Location longitude in degrees
 * @param title Location title
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Period in seconds for which the location can be updated, should be between 60 and 86400.
 * @param heading Optional. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius Optional. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the location
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
@SerialName("location")
data class InlineQueryResultLocation(
    val id: InlineQueryResultId,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Long? = null,
    val thumbnailHeight: Long? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultLocation").prop("id", id).prop("latitude", latitude).prop("longitude", longitude).prop("title", title).prop("horizontalAccuracy", horizontalAccuracy).prop("livePeriod", livePeriod).prop("heading", heading).prop("proximityAlertRadius", proximityAlertRadius).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
}

/**
 * Represents a venue. By default, the venue will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the venue.
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param latitude Latitude of the venue location in degrees
 * @param longitude Longitude of the venue location in degrees
 * @param title Title of the venue
 * @param address Address of the venue
 * @param foursquareId Optional. Foursquare identifier of the venue if known
 * @param foursquareType Optional. Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
 * @param googlePlaceId Optional. Google Places identifier of the venue
 * @param googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the venue
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
@SerialName("venue")
data class InlineQueryResultVenue(
    val id: InlineQueryResultId,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Long? = null,
    val thumbnailHeight: Long? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultVenue").prop("id", id).prop("latitude", latitude).prop("longitude", longitude).prop("title", title).prop("address", address).prop("foursquareId", foursquareId).prop("foursquareType", foursquareType).prop("googlePlaceId", googlePlaceId).prop("googlePlaceType", googlePlaceType).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
}

/**
 * Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the contact.
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Optional. Contact's last name
 * @param vcard Optional. Additional data about the contact in the form of a vCard, 0-2048 bytes
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the contact
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
@SerialName("contact")
data class InlineQueryResultContact(
    val id: InlineQueryResultId,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val vcard: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Long? = null,
    val thumbnailHeight: Long? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultContact").prop("id", id).prop("phoneNumber", phoneNumber).prop("firstName", firstName).prop("lastName", lastName).prop("vcard", vcard).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
}

/**
 * Represents a Game.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gameShortName Short name of the game
 * @param replyMarkup Optional. Inline keyboard attached to the message
 */
@Serializable
@SerialName("game")
data class InlineQueryResultGame(
    val id: InlineQueryResultId,
    val gameShortName: String,
    val replyMarkup: InlineKeyboardMarkup? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultGame").prop("id", id).prop("gameShortName", gameShortName).prop("replyMarkup", replyMarkup).toString()
}

/**
 * Represents a link to a photo stored on the Telegram servers. By default, this photo will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param photoFileId A valid file identifier of the photo
 * @param title Optional. Title for the result
 * @param description Optional. Short description of the result
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the photo
 */
@Serializable
@SerialName("photo")
data class InlineQueryResultCachedPhoto(
    val id: InlineQueryResultId,
    val photoFileId: FileId,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedPhoto").prop("id", id).prop("photoFileId", photoFileId).prop("title", title).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gifFileId A valid file identifier for the GIF file
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the GIF animation
 */
@Serializable
@SerialName("gif")
data class InlineQueryResultCachedGif(
    val id: InlineQueryResultId,
    val gifFileId: FileId,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedGif").prop("id", id).prop("gifFileId", gifFileId).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound) stored on the Telegram servers. By default, this animated MPEG-4 file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param mpeg4FileId A valid file identifier for the MPEG4 file
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the video animation
 */
@Serializable
@SerialName("mpeg4_gif")
data class InlineQueryResultCachedMpeg4Gif(
    val id: InlineQueryResultId,
    val mpeg4FileId: FileId,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedMpeg4Gif").prop("id", id).prop("mpeg4FileId", mpeg4FileId).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the sticker.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param stickerFileId A valid file identifier of the sticker
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the sticker
 */
@Serializable
@SerialName("sticker")
data class InlineQueryResultCachedSticker(
    val id: InlineQueryResultId,
    val stickerFileId: FileId,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedSticker").prop("id", id).prop("stickerFileId", stickerFileId).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a file stored on the Telegram servers. By default, this file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the file.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param title Title for the result
 * @param documentFileId A valid file identifier for the file
 * @param description Optional. Short description of the result
 * @param caption Optional. Caption of the document to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the document caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the file
 */
@Serializable
@SerialName("document")
data class InlineQueryResultCachedDocument(
    val id: InlineQueryResultId,
    val title: String,
    val documentFileId: FileId,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedDocument").prop("id", id).prop("title", title).prop("documentFileId", documentFileId).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a video file stored on the Telegram servers. By default, this video file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param videoFileId A valid file identifier for the video file
 * @param title Title for the result
 * @param description Optional. Short description of the result
 * @param caption Optional. Caption of the video to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the video
 */
@Serializable
@SerialName("video")
data class InlineQueryResultCachedVideo(
    val id: InlineQueryResultId,
    val videoFileId: FileId,
    val title: String,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedVideo").prop("id", id).prop("videoFileId", videoFileId).prop("title", title).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a voice message stored on the Telegram servers. By default, this voice message will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the voice message.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param voiceFileId A valid file identifier for the voice message
 * @param title Voice message title
 * @param caption Optional. Caption, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the voice message
 */
@Serializable
@SerialName("voice")
data class InlineQueryResultCachedVoice(
    val id: InlineQueryResultId,
    val voiceFileId: FileId,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedVoice").prop("id", id).prop("voiceFileId", voiceFileId).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to an MP3 audio file stored on the Telegram servers. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param audioFileId A valid file identifier for the audio file
 * @param caption Optional. Caption, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the audio caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the audio
 */
@Serializable
@SerialName("audio")
data class InlineQueryResultCachedAudio(
    val id: InlineQueryResultId,
    val audioFileId: FileId,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult() {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedAudio").prop("id", id).prop("audioFileId", audioFileId).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * This object represents the content of a message to be sent as a result of an inline query. Telegram clients currently support the following 5 types:
 * - [InputTextMessageContent]
 * - [InputLocationMessageContent]
 * - [InputVenueMessageContent]
 * - [InputContactMessageContent]
 * - [InputInvoiceMessageContent]
 */ 
@Serializable(with = InputMessageContentSerializer::class)
sealed class InputMessageContent

object InputMessageContentSerializer : JsonContentPolymorphicSerializer<InputMessageContent>(InputMessageContent::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<InputMessageContent> {
        val json = element.jsonObject
        return when {
            "message_text" in json -> InputTextMessageContent.serializer()
            "horizontal_accuracy" in json -> InputLocationMessageContent.serializer()
            "address" in json -> InputVenueMessageContent.serializer()
            "phone_number" in json -> InputContactMessageContent.serializer()
            "payload" in json -> InputInvoiceMessageContent.serializer()
            else -> error("Failed to deserialize an update type: $json")
        }
    }
}

/**
 * Represents the content of a text message to be sent as the result of an inline query.
 * @param messageText Text of the message to be sent, 1-4096 characters
 * @param parseMode Optional. Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities Optional. List of special entities that appear in message text, which can be specified instead of parse_mode
 * @param disableWebPagePreview Optional. Disables link previews for links in the sent message
 */
@Serializable
data class InputTextMessageContent(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val disableWebPagePreview: Boolean? = null,
) : InputMessageContent() {
    override fun toString() = DebugStringBuilder("InputTextMessageContent").prop("messageText", messageText).prop("parseMode", parseMode).prop("entities", entities).prop("disableWebPagePreview", disableWebPagePreview).toString()
}

/**
 * Represents the content of a location message to be sent as the result of an inline query.
 * @param latitude Latitude of the location in degrees
 * @param longitude Longitude of the location in degrees
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Period in seconds for which the location can be updated, should be between 60 and 86400.
 * @param heading Optional. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius Optional. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 */
@Serializable
data class InputLocationMessageContent(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
) : InputMessageContent() {
    override fun toString() = DebugStringBuilder("InputLocationMessageContent").prop("latitude", latitude).prop("longitude", longitude).prop("horizontalAccuracy", horizontalAccuracy).prop("livePeriod", livePeriod).prop("heading", heading).prop("proximityAlertRadius", proximityAlertRadius).toString()
}

/**
 * Represents the content of a venue message to be sent as the result of an inline query.
 * @param latitude Latitude of the venue in degrees
 * @param longitude Longitude of the venue in degrees
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Optional. Foursquare identifier of the venue, if known
 * @param foursquareType Optional. Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
 * @param googlePlaceId Optional. Google Places identifier of the venue
 * @param googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 */
@Serializable
data class InputVenueMessageContent(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
) : InputMessageContent() {
    override fun toString() = DebugStringBuilder("InputVenueMessageContent").prop("latitude", latitude).prop("longitude", longitude).prop("title", title).prop("address", address).prop("foursquareId", foursquareId).prop("foursquareType", foursquareType).prop("googlePlaceId", googlePlaceId).prop("googlePlaceType", googlePlaceType).toString()
}

/**
 * Represents the content of a contact message to be sent as the result of an inline query.
 * @param phoneNumber Contact's phone number
 * @param firstName Contact's first name
 * @param lastName Optional. Contact's last name
 * @param vcard Optional. Additional data about the contact in the form of a vCard, 0-2048 bytes
 */
@Serializable
data class InputContactMessageContent(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val vcard: String? = null,
) : InputMessageContent() {
    override fun toString() = DebugStringBuilder("InputContactMessageContent").prop("phoneNumber", phoneNumber).prop("firstName", firstName).prop("lastName", lastName).prop("vcard", vcard).toString()
}

/**
 * Represents the content of an invoice message to be sent as the result of an inline query.
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
 * @param providerToken Payment provider token, obtained via @BotFather
 * @param currency Three-letter ISO 4217 currency code, see more on currencies
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
 * @param maxTipAmount Optional. The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0
 * @param suggestedTipAmounts Optional. A JSON-serialized array of suggested amounts of tip in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param providerData Optional. A JSON-serialized object for data about the invoice, which will be shared with the payment provider. A detailed description of the required fields should be provided by the payment provider.
 * @param photoUrl Optional. URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @param photoSize Optional. Photo size in bytes
 * @param photoWidth Optional. Photo width
 * @param photoHeight Optional. Photo height
 * @param needName Optional. Pass True if you require the user's full name to complete the order
 * @param needPhoneNumber Optional. Pass True if you require the user's phone number to complete the order
 * @param needEmail Optional. Pass True if you require the user's email address to complete the order
 * @param needShippingAddress Optional. Pass True if you require the user's shipping address to complete the order
 * @param sendPhoneNumberToProvider Optional. Pass True if the user's phone number should be sent to provider
 * @param sendEmailToProvider Optional. Pass True if the user's email address should be sent to provider
 * @param isFlexible Optional. Pass True if the final price depends on the shipping method
 */
@Serializable
data class InputInvoiceMessageContent(
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
) : InputMessageContent() {
    override fun toString() = DebugStringBuilder("InputInvoiceMessageContent").prop("title", title).prop("description", description).prop("payload", payload).prop("providerToken", providerToken).prop("currency", currency).prop("prices", prices).prop("maxTipAmount", maxTipAmount).prop("suggestedTipAmounts", suggestedTipAmounts).prop("providerData", providerData).prop("photoUrl", photoUrl).prop("photoSize", photoSize).prop("photoWidth", photoWidth).prop("photoHeight", photoHeight).prop("needName", needName).prop("needPhoneNumber", needPhoneNumber).prop("needEmail", needEmail).prop("needShippingAddress", needShippingAddress).prop("sendPhoneNumberToProvider", sendPhoneNumberToProvider).prop("sendEmailToProvider", sendEmailToProvider).prop("isFlexible", isFlexible).toString()
}

/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 * @param resultId The unique identifier for the result that was chosen
 * @param from The user that chose the result
 * @param query The query that was used to obtain the result
 * @param location Optional. Sender location, only for bots that require user location
 * @param inlineMessageId Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message. Will be also received in callback queries and can be used to edit the message.
 */
@Serializable
data class ChosenInlineResult(
    val resultId: InlineQueryResultId,
    val from: User,
    val query: String,
    val location: Location? = null,
    val inlineMessageId: InlineMessageId? = null,
) {
    override fun toString() = DebugStringBuilder("ChosenInlineResult").prop("resultId", resultId).prop("from", from).prop("query", query).prop("location", location).prop("inlineMessageId", inlineMessageId).toString()
}

/**
 * Describes an inline message sent by a Web App on behalf of a user.
 * @param inlineMessageId Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
 */
@Serializable
data class SentWebAppMessage(
    val inlineMessageId: InlineMessageId? = null,
) {
    override fun toString() = DebugStringBuilder("SentWebAppMessage").prop("inlineMessageId", inlineMessageId).toString()
}

/**
 * This object represents a portion of the price for goods or services.
 * @param label Portion label
 * @param amount Price of the product in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 */
@Serializable
data class LabeledPrice(
    val label: String,
    val amount: Long,
) {
    override fun toString() = DebugStringBuilder("LabeledPrice").prop("label", label).prop("amount", amount).toString()
}

/**
 * This object contains basic information about an invoice.
 * @param title Product name
 * @param description Product description
 * @param startParameter Unique bot deep-linking parameter that can be used to generate this invoice
 * @param currency Three-letter ISO 4217 currency code
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 */
@Serializable
data class Invoice(
    val title: String,
    val description: String,
    val startParameter: String,
    val currency: String,
    val totalAmount: Long,
) {
    override fun toString() = DebugStringBuilder("Invoice").prop("title", title).prop("description", description).prop("startParameter", startParameter).prop("currency", currency).prop("totalAmount", totalAmount).toString()
}

/**
 * This object represents a shipping address.
 * @param countryCode Two-letter ISO 3166-1 alpha-2 country code
 * @param state State, if applicable
 * @param city City
 * @param streetLine1 First line for the address
 * @param streetLine2 Second line for the address
 * @param postCode Address post code
 */
@Serializable
data class ShippingAddress(
    val countryCode: String,
    val state: String,
    val city: String,
    val streetLine1: String,
    val streetLine2: String,
    val postCode: String,
) {
    override fun toString() = DebugStringBuilder("ShippingAddress").prop("countryCode", countryCode).prop("state", state).prop("city", city).prop("streetLine1", streetLine1).prop("streetLine2", streetLine2).prop("postCode", postCode).toString()
}

/**
 * This object represents information about an order.
 * @param name Optional. User name
 * @param phoneNumber Optional. User's phone number
 * @param email Optional. User email
 * @param shippingAddress Optional. User shipping address
 */
@Serializable
data class OrderInfo(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val shippingAddress: ShippingAddress? = null,
) {
    override fun toString() = DebugStringBuilder("OrderInfo").prop("name", name).prop("phoneNumber", phoneNumber).prop("email", email).prop("shippingAddress", shippingAddress).toString()
}

/**
 * This object represents one shipping option.
 * @param id Shipping option identifier
 * @param title Option title
 * @param prices List of price portions
 */
@Serializable
data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>,
) {
    override fun toString() = DebugStringBuilder("ShippingOption").prop("id", id).prop("title", title).prop("prices", prices).toString()
}

/**
 * This object contains basic information about a successful payment.
 * @param currency Three-letter ISO 4217 currency code
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot specified invoice payload
 * @param telegramPaymentChargeId Telegram payment identifier
 * @param providerPaymentChargeId Provider payment identifier
 * @param shippingOptionId Optional. Identifier of the shipping option chosen by the user
 * @param orderInfo Optional. Order information provided by the user
 */
@Serializable
data class SuccessfulPayment(
    val currency: String,
    val totalAmount: Long,
    val invoicePayload: String,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null,
) {
    override fun toString() = DebugStringBuilder("SuccessfulPayment").prop("currency", currency).prop("totalAmount", totalAmount).prop("invoicePayload", invoicePayload).prop("telegramPaymentChargeId", telegramPaymentChargeId).prop("providerPaymentChargeId", providerPaymentChargeId).prop("shippingOptionId", shippingOptionId).prop("orderInfo", orderInfo).toString()
}

/**
 * This object contains information about an incoming shipping query.
 * @param id Unique query identifier
 * @param from User who sent the query
 * @param invoicePayload Bot specified invoice payload
 * @param shippingAddress User specified shipping address
 */
@Serializable
data class ShippingQuery(
    val id: ShippingQueryId,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress,
) {
    override fun toString() = DebugStringBuilder("ShippingQuery").prop("id", id).prop("from", from).prop("invoicePayload", invoicePayload).prop("shippingAddress", shippingAddress).toString()
}

/**
 * This object contains information about an incoming pre-checkout query.
 * @param id Unique query identifier
 * @param from User who sent the query
 * @param currency Three-letter ISO 4217 currency code
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot specified invoice payload
 * @param shippingOptionId Optional. Identifier of the shipping option chosen by the user
 * @param orderInfo Optional. Order information provided by the user
 */
@Serializable
data class PreCheckoutQuery(
    val id: String,
    val from: User,
    val currency: String,
    val totalAmount: Long,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null,
) {
    override fun toString() = DebugStringBuilder("PreCheckoutQuery").prop("id", id).prop("from", from).prop("currency", currency).prop("totalAmount", totalAmount).prop("invoicePayload", invoicePayload).prop("shippingOptionId", shippingOptionId).prop("orderInfo", orderInfo).toString()
}

/**
 * Describes Telegram Passport data shared with the bot by the user.
 * @param data Array with information about documents and other Telegram Passport elements that was shared with the bot
 * @param credentials Encrypted credentials required to decrypt the data
 */
@Serializable
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials,
) {
    override fun toString() = DebugStringBuilder("PassportData").prop("data", data).prop("credentials", credentials).toString()
}

/**
 * This object represents a file uploaded to Telegram Passport. Currently all Telegram Passport files are in JPEG format when decrypted and don't exceed 10MB.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param fileSize File size in bytes
 * @param fileDate Unix time when the file was uploaded
 */
@Serializable
data class PassportFile(
    val fileId: FileId,
    val fileUniqueId: FileUniqueId,
    val fileSize: Long,
    val fileDate: UnixTimestamp,
) {
    override fun toString() = DebugStringBuilder("PassportFile").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("fileSize", fileSize).prop("fileDate", fileDate).toString()
}

/**
 * Describes documents or other Telegram Passport elements shared with the bot by the user.
 * @param type Element type. One of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”, “address”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”, “phone_number”, “email”.
 * @param hash Base64-encoded element hash for using in PassportElementErrorUnspecified
 * @param data Optional. Base64-encoded encrypted Telegram Passport element data provided by the user, available for “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport” and “address” types. Can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param phoneNumber Optional. User's verified phone number, available only for “phone_number” type
 * @param email Optional. User's verified email address, available only for “email” type
 * @param files Optional. Array of encrypted files with documents provided by the user, available for “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param frontSide Optional. Encrypted file with the front side of the document, provided by the user. Available for “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param reverseSide Optional. Encrypted file with the reverse side of the document, provided by the user. Available for “driver_license” and “identity_card”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param selfie Optional. Encrypted file with the selfie of the user holding a document, provided by the user; available for “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param translation Optional. Array of encrypted files with translated versions of documents provided by the user. Available if requested for “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 */
@Serializable
data class EncryptedPassportElement(
    val type: String,
    val hash: String,
    val data: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val files: List<PassportFile>? = null,
    val frontSide: PassportFile? = null,
    val reverseSide: PassportFile? = null,
    val selfie: PassportFile? = null,
    val translation: List<PassportFile>? = null,
) {
    override fun toString() = DebugStringBuilder("EncryptedPassportElement").prop("type", type).prop("hash", hash).prop("data", data).prop("phoneNumber", phoneNumber).prop("email", email).prop("files", files).prop("frontSide", frontSide).prop("reverseSide", reverseSide).prop("selfie", selfie).prop("translation", translation).toString()
}

/**
 * Describes data required for decrypting and authenticating EncryptedPassportElement. See the Telegram Passport Documentation for a complete description of the data decryption and authentication processes.
 * @param data Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required for EncryptedPassportElement decryption and authentication
 * @param hash Base64-encoded data hash for data authentication
 * @param secret Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption
 */
@Serializable
data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String,
) {
    override fun toString() = DebugStringBuilder("EncryptedCredentials").prop("data", data).prop("hash", hash).prop("secret", secret).toString()
}

/**
 * This object represents an error in the Telegram Passport element which was submitted that should be resolved by the user. It should be one of:
 * - [PassportElementErrorDataField]
 * - [PassportElementErrorFrontSide]
 * - [PassportElementErrorReverseSide]
 * - [PassportElementErrorSelfie]
 * - [PassportElementErrorFile]
 * - [PassportElementErrorFiles]
 * - [PassportElementErrorTranslationFile]
 * - [PassportElementErrorTranslationFiles]
 * - [PassportElementErrorUnspecified]
 */ 
@Serializable
@JsonClassDiscriminator("type")
sealed class PassportElementError

/**
 * Represents an issue in one of the data fields that was provided by the user. The error is considered resolved when the field's value changes.
 * @param source Error source, must be data
 * @param type The section of the user's Telegram Passport which has the error, one of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”, “address”
 * @param fieldName Name of the data field which has the error
 * @param dataHash Base64-encoded data hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorDataField(
    val source: String,
    val type: String,
    val fieldName: String,
    val dataHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorDataField").prop("source", source).prop("type", type).prop("fieldName", fieldName).prop("dataHash", dataHash).prop("message", message).toString()
}

/**
 * Represents an issue with the front side of a document. The error is considered resolved when the file with the front side of the document changes.
 * @param source Error source, must be front_side
 * @param type The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”
 * @param fileHash Base64-encoded hash of the file with the front side of the document
 * @param message Error message
 */
@Serializable
data class PassportElementErrorFrontSide(
    val source: String,
    val type: String,
    val fileHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorFrontSide").prop("source", source).prop("type", type).prop("fileHash", fileHash).prop("message", message).toString()
}

/**
 * Represents an issue with the reverse side of a document. The error is considered resolved when the file with reverse side of the document changes.
 * @param source Error source, must be reverse_side
 * @param type The section of the user's Telegram Passport which has the issue, one of “driver_license”, “identity_card”
 * @param fileHash Base64-encoded hash of the file with the reverse side of the document
 * @param message Error message
 */
@Serializable
data class PassportElementErrorReverseSide(
    val source: String,
    val type: String,
    val fileHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorReverseSide").prop("source", source).prop("type", type).prop("fileHash", fileHash).prop("message", message).toString()
}

/**
 * Represents an issue with the selfie with a document. The error is considered resolved when the file with the selfie changes.
 * @param source Error source, must be selfie
 * @param type The section of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”
 * @param fileHash Base64-encoded hash of the file with the selfie
 * @param message Error message
 */
@Serializable
data class PassportElementErrorSelfie(
    val source: String,
    val type: String,
    val fileHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorSelfie").prop("source", source).prop("type", type).prop("fileHash", fileHash).prop("message", message).toString()
}

/**
 * Represents an issue with a document scan. The error is considered resolved when the file with the document scan changes.
 * @param source Error source, must be file
 * @param type The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
 * @param fileHash Base64-encoded file hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorFile(
    val source: String,
    val type: String,
    val fileHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorFile").prop("source", source).prop("type", type).prop("fileHash", fileHash).prop("message", message).toString()
}

/**
 * Represents an issue with a list of scans. The error is considered resolved when the list of files containing the scans changes.
 * @param source Error source, must be files
 * @param type The section of the user's Telegram Passport which has the issue, one of “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
 * @param fileHashes List of base64-encoded file hashes
 * @param message Error message
 */
@Serializable
data class PassportElementErrorFiles(
    val source: String,
    val type: String,
    val fileHashes: List<String>,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorFiles").prop("source", source).prop("type", type).prop("fileHashes", fileHashes).prop("message", message).toString()
}

/**
 * Represents an issue with one of the files that constitute the translation of a document. The error is considered resolved when the file changes.
 * @param source Error source, must be translation_file
 * @param type Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
 * @param fileHash Base64-encoded file hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorTranslationFile(
    val source: String,
    val type: String,
    val fileHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorTranslationFile").prop("source", source).prop("type", type).prop("fileHash", fileHash).prop("message", message).toString()
}

/**
 * Represents an issue with the translated version of a document. The error is considered resolved when a file with the document translation change.
 * @param source Error source, must be translation_files
 * @param type Type of element of the user's Telegram Passport which has the issue, one of “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”
 * @param fileHashes List of base64-encoded file hashes
 * @param message Error message
 */
@Serializable
data class PassportElementErrorTranslationFiles(
    val source: String,
    val type: String,
    val fileHashes: List<String>,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorTranslationFiles").prop("source", source).prop("type", type).prop("fileHashes", fileHashes).prop("message", message).toString()
}

/**
 * Represents an issue in an unspecified place. The error is considered resolved when new data is added.
 * @param source Error source, must be unspecified
 * @param type Type of element of the user's Telegram Passport which has the issue
 * @param elementHash Base64-encoded element hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorUnspecified(
    val source: String,
    val type: String,
    val elementHash: String,
    val message: String,
) : PassportElementError() {
    override fun toString() = DebugStringBuilder("PassportElementErrorUnspecified").prop("source", source).prop("type", type).prop("elementHash", elementHash).prop("message", message).toString()
}

/**
 * This object represents a game. Use BotFather to create and edit games, their short names will act as unique identifiers.
 * @param title Title of the game
 * @param description Description of the game
 * @param photo Photo that will be displayed in the game message in chats.
 * @param text Optional. Brief description of the game or high scores included in the game message. Can be automatically edited to include current high scores for the game when the bot calls setGameScore, or manually edited using editMessageText. 0-4096 characters.
 * @param textEntities Optional. Special entities that appear in text, such as usernames, URLs, bot commands, etc.
 * @param animation Optional. Animation that will be displayed in the game message in chats. Upload via BotFather
 */
@Serializable
data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null,
) {
    override fun toString() = DebugStringBuilder("Game").prop("title", title).prop("description", description).prop("photo", photo).prop("text", text).prop("textEntities", textEntities).prop("animation", animation).toString()
}

/**
 * A placeholder, currently holds no information. Use BotFather to set up your game.
 */
@Serializable
data object CallbackGame

/**
 * This object represents one row of the high scores table for a game.
 * @param position Position in high score table for the game
 * @param user User
 * @param score Score
 */
@Serializable
data class GameHighScore(
    val position: Long,
    val user: User,
    val score: Long,
) {
    override fun toString() = DebugStringBuilder("GameHighScore").prop("position", position).prop("user", user).prop("score", score).toString()
}

/**
 * This object represents an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
 * - [InlineKeyboardMarkup]
 * - [ReplyKeyboardMarkup]
 * - [ReplyKeyboardRemove]
 * - [ForceReply]
 */ 
@Serializable(with = ReplyMarkupSerializer::class)
sealed class ReplyMarkup

object ReplyMarkupSerializer : JsonContentPolymorphicSerializer<ReplyMarkup>(ReplyMarkup::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ReplyMarkup> {
        val json = element.jsonObject
        return when {
            "inline_keyboard" in json -> InlineKeyboardMarkup.serializer()
            "keyboard" in json -> ReplyKeyboardMarkup.serializer()
            "remove_keyboard" in json -> ReplyKeyboardRemove.serializer()
            "force_reply" in json -> ForceReply.serializer()
            else -> error("Failed to deserialize an update type: $json")
        }
    }
}

@Serializable
@JvmInline
value class ChatId(val value: Long) {
    override fun toString(): String = "ChatId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class UserId(val value: Long) {
    override fun toString(): String = "UserId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class MessageId(val value: Long) {
    override fun toString(): String = "MessageId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class InlineMessageId(val value: String) {
    override fun toString(): String = "InlineMessageId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class MessageThreadId(val value: Long) {
    override fun toString(): String = "MessageThreadId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class CallbackQueryId(val value: String) {
    override fun toString(): String = "CallbackQueryId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class InlineQueryId(val value: String) {
    override fun toString(): String = "InlineQueryId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class InlineQueryResultId(val value: String) {
    override fun toString(): String = "InlineQueryResultId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class FileId(val value: String) {
    override fun toString(): String = "FileId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class FileUniqueId(val value: String) {
    override fun toString(): String = "FileUniqueId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class ShippingQueryId(val value: String) {
    override fun toString(): String = "ShippingQueryId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class WebAppQueryId(val value: String) {
    override fun toString(): String = "WebAppQueryId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class CustomEmojiId(val value: String) {
    override fun toString(): String = "CustomEmojiId(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class Seconds(val value: Long) {
    override fun toString(): String = "Seconds(${quoteWhenWhitespace(value)})"
}

@Serializable
@JvmInline
value class UnixTimestamp(val value: Long) {
    override fun toString(): String = "UnixTimestamp(${quoteWhenWhitespace(value)})"
}

