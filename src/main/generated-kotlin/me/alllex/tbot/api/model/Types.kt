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
    @SerialName("message_reaction") MESSAGE_REACTION,
    @SerialName("message_reaction_count") MESSAGE_REACTION_COUNT,
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
    @SerialName("chat_boost") CHAT_BOOST,
    @SerialName("removed_chat_boost") REMOVED_CHAT_BOOST,
    @SerialName("business_connection") BUSINESS_CONNECTION,
    @SerialName("business_message") BUSINESS_MESSAGE,
    @SerialName("edited_business_message") EDITED_BUSINESS_MESSAGE,
    @SerialName("deleted_business_messages") DELETED_BUSINESS_MESSAGES,
    @SerialName("purchased_paid_media") PURCHASED_PAID_MEDIA,
}

/**
 * This object represents an incoming update. At most one of the optional parameters can be present in any given update.
 *
 * Sub-types:
 * - [MessageUpdate]
 * - [EditedMessageUpdate]
 * - [ChannelPostUpdate]
 * - [EditedChannelPostUpdate]
 * - [MessageReactionUpdate]
 * - [MessageReactionCountUpdate]
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
 * - [ChatBoostUpdate]
 * - [RemovedChatBoostUpdate]
 * - [BusinessConnectionUpdate]
 * - [BusinessMessageUpdate]
 * - [EditedBusinessMessageUpdate]
 * - [DeletedBusinessMessagesUpdate]
 * - [PurchasedPaidMediaUpdate]
 */
@Serializable(with = UpdateSerializer::class)
sealed interface Update {
    val updateId: Long
    val updateType: UpdateType
}

/**
 * New incoming message of any kind - text, photo, sticker, etc.
 */
@Serializable
data class MessageUpdate(
    override val updateId: Long,
    val message: Message,
): Update {
    override val updateType: UpdateType get() = UpdateType.MESSAGE
}

/**
 * New version of a message that is known to the bot and was edited. This update may at times be triggered by changes to message fields that are either unavailable or not actively used by your bot.
 */
@Serializable
data class EditedMessageUpdate(
    override val updateId: Long,
    val editedMessage: Message,
): Update {
    override val updateType: UpdateType get() = UpdateType.EDITED_MESSAGE
}

/**
 * New incoming channel post of any kind - text, photo, sticker, etc.
 */
@Serializable
data class ChannelPostUpdate(
    override val updateId: Long,
    val channelPost: Message,
): Update {
    override val updateType: UpdateType get() = UpdateType.CHANNEL_POST
}

/**
 * New version of a channel post that is known to the bot and was edited. This update may at times be triggered by changes to message fields that are either unavailable or not actively used by your bot.
 */
@Serializable
data class EditedChannelPostUpdate(
    override val updateId: Long,
    val editedChannelPost: Message,
): Update {
    override val updateType: UpdateType get() = UpdateType.EDITED_CHANNEL_POST
}

/**
 * A reaction to a message was changed by a user. The bot must be an administrator in the chat and must explicitly specify "message_reaction" in the list of allowed_updates to receive these updates. The update isn't received for reactions set by bots.
 */
@Serializable
data class MessageReactionUpdate(
    override val updateId: Long,
    val messageReaction: MessageReactionUpdated,
): Update {
    override val updateType: UpdateType get() = UpdateType.MESSAGE_REACTION
}

/**
 * Reactions to a message with anonymous reactions were changed. The bot must be an administrator in the chat and must explicitly specify "message_reaction_count" in the list of allowed_updates to receive these updates. The updates are grouped and can be sent with delay up to a few minutes.
 */
@Serializable
data class MessageReactionCountUpdate(
    override val updateId: Long,
    val messageReactionCount: MessageReactionCountUpdated,
): Update {
    override val updateType: UpdateType get() = UpdateType.MESSAGE_REACTION_COUNT
}

/**
 * New incoming inline query
 */
@Serializable
data class InlineQueryUpdate(
    override val updateId: Long,
    val inlineQuery: InlineQuery,
): Update {
    override val updateType: UpdateType get() = UpdateType.INLINE_QUERY
}

/**
 * The result of an inline query that was chosen by a user and sent to their chat partner. Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
 */
@Serializable
data class ChosenInlineResultUpdate(
    override val updateId: Long,
    val chosenInlineResult: ChosenInlineResult,
): Update {
    override val updateType: UpdateType get() = UpdateType.CHOSEN_INLINE_RESULT
}

/**
 * New incoming callback query
 */
@Serializable
data class CallbackQueryUpdate(
    override val updateId: Long,
    val callbackQuery: CallbackQuery,
): Update {
    override val updateType: UpdateType get() = UpdateType.CALLBACK_QUERY
}

/**
 * New incoming shipping query. Only for invoices with flexible price
 */
@Serializable
data class ShippingQueryUpdate(
    override val updateId: Long,
    val shippingQuery: ShippingQuery,
): Update {
    override val updateType: UpdateType get() = UpdateType.SHIPPING_QUERY
}

/**
 * New incoming pre-checkout query. Contains full information about checkout
 */
@Serializable
data class PreCheckoutQueryUpdate(
    override val updateId: Long,
    val preCheckoutQuery: PreCheckoutQuery,
): Update {
    override val updateType: UpdateType get() = UpdateType.PRE_CHECKOUT_QUERY
}

/**
 * New poll state. Bots receive only updates about manually stopped polls and polls, which are sent by the bot
 */
@Serializable
data class PollUpdate(
    override val updateId: Long,
    val poll: Poll,
): Update {
    override val updateType: UpdateType get() = UpdateType.POLL
}

/**
 * A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself.
 */
@Serializable
data class PollAnswerUpdate(
    override val updateId: Long,
    val pollAnswer: PollAnswer,
): Update {
    override val updateType: UpdateType get() = UpdateType.POLL_ANSWER
}

/**
 * The bot's chat member status was updated in a chat. For private chats, this update is received only when the bot is blocked or unblocked by the user.
 */
@Serializable
data class MyChatMemberUpdate(
    override val updateId: Long,
    val myChatMember: ChatMemberUpdated,
): Update {
    override val updateType: UpdateType get() = UpdateType.MY_CHAT_MEMBER
}

/**
 * A chat member's status was updated in a chat. The bot must be an administrator in the chat and must explicitly specify "chat_member" in the list of allowed_updates to receive these updates.
 */
@Serializable
data class ChatMemberUpdate(
    override val updateId: Long,
    val chatMember: ChatMemberUpdated,
): Update {
    override val updateType: UpdateType get() = UpdateType.CHAT_MEMBER
}

/**
 * A request to join the chat has been sent. The bot must have the can_invite_users administrator right in the chat to receive these updates.
 */
@Serializable
data class ChatJoinRequestUpdate(
    override val updateId: Long,
    val chatJoinRequest: ChatJoinRequest,
): Update {
    override val updateType: UpdateType get() = UpdateType.CHAT_JOIN_REQUEST
}

/**
 * A chat boost was added or changed. The bot must be an administrator in the chat to receive these updates.
 */
@Serializable
data class ChatBoostUpdate(
    override val updateId: Long,
    val chatBoost: ChatBoostUpdated,
): Update {
    override val updateType: UpdateType get() = UpdateType.CHAT_BOOST
}

/**
 * A boost was removed from a chat. The bot must be an administrator in the chat to receive these updates.
 */
@Serializable
data class RemovedChatBoostUpdate(
    override val updateId: Long,
    val removedChatBoost: ChatBoostRemoved,
): Update {
    override val updateType: UpdateType get() = UpdateType.REMOVED_CHAT_BOOST
}

/**
 * The bot was connected to or disconnected from a business account, or a user edited an existing connection with the bot
 */
@Serializable
data class BusinessConnectionUpdate(
    override val updateId: Long,
    val businessConnection: BusinessConnection,
): Update {
    override val updateType: UpdateType get() = UpdateType.BUSINESS_CONNECTION
}

/**
 * New message from a connected business account
 */
@Serializable
data class BusinessMessageUpdate(
    override val updateId: Long,
    val businessMessage: Message,
): Update {
    override val updateType: UpdateType get() = UpdateType.BUSINESS_MESSAGE
}

/**
 * New version of a message from a connected business account
 */
@Serializable
data class EditedBusinessMessageUpdate(
    override val updateId: Long,
    val editedBusinessMessage: Message,
): Update {
    override val updateType: UpdateType get() = UpdateType.EDITED_BUSINESS_MESSAGE
}

/**
 * Messages were deleted from a connected business account
 */
@Serializable
data class DeletedBusinessMessagesUpdate(
    override val updateId: Long,
    val deletedBusinessMessages: BusinessMessagesDeleted,
): Update {
    override val updateType: UpdateType get() = UpdateType.DELETED_BUSINESS_MESSAGES
}

/**
 * A user purchased paid media with a non-empty payload sent by the bot in a non-channel chat
 */
@Serializable
data class PurchasedPaidMediaUpdate(
    override val updateId: Long,
    val purchasedPaidMedia: PaidMediaPurchased,
): Update {
    override val updateType: UpdateType get() = UpdateType.PURCHASED_PAID_MEDIA
}

object UpdateSerializer : JsonContentPolymorphicSerializer<Update>(Update::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Update> {
        val json = element.jsonObject
        return when {
            "message" in json -> MessageUpdate.serializer()
            "edited_message" in json -> EditedMessageUpdate.serializer()
            "channel_post" in json -> ChannelPostUpdate.serializer()
            "edited_channel_post" in json -> EditedChannelPostUpdate.serializer()
            "message_reaction" in json -> MessageReactionUpdate.serializer()
            "message_reaction_count" in json -> MessageReactionCountUpdate.serializer()
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
            "chat_boost" in json -> ChatBoostUpdate.serializer()
            "removed_chat_boost" in json -> RemovedChatBoostUpdate.serializer()
            "business_connection" in json -> BusinessConnectionUpdate.serializer()
            "business_message" in json -> BusinessMessageUpdate.serializer()
            "edited_business_message" in json -> EditedBusinessMessageUpdate.serializer()
            "deleted_business_messages" in json -> DeletedBusinessMessagesUpdate.serializer()
            "purchased_paid_media" in json -> PurchasedPaidMediaUpdate.serializer()
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
 * @param canConnectToBusiness Optional. True, if the bot can be connected to a Telegram Business account to receive its messages. Returned only in getMe.
 * @param hasMainWebApp Optional. True, if the bot has a main Web App. Returned only in getMe.
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
    val canConnectToBusiness: Boolean? = null,
    val hasMainWebApp: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("User").prop("id", id).prop("isBot", isBot).prop("firstName", firstName).prop("lastName", lastName).prop("username", username).prop("languageCode", languageCode).prop("isPremium", isPremium).prop("addedToAttachmentMenu", addedToAttachmentMenu).prop("canJoinGroups", canJoinGroups).prop("canReadAllGroupMessages", canReadAllGroupMessages).prop("supportsInlineQueries", supportsInlineQueries).prop("canConnectToBusiness", canConnectToBusiness).prop("hasMainWebApp", hasMainWebApp).toString()
}

/**
 * This object represents a chat.
 * @param id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param type Type of the chat, can be either “private”, “group”, “supergroup” or “channel”
 * @param title Optional. Title, for supergroups, channels and group chats
 * @param username Optional. Username, for private chats, supergroups and channels if available
 * @param firstName Optional. First name of the other party in a private chat
 * @param lastName Optional. Last name of the other party in a private chat
 * @param isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
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
) {
    override fun toString() = DebugStringBuilder("Chat").prop("id", id).prop("type", type).prop("title", title).prop("username", username).prop("firstName", firstName).prop("lastName", lastName).prop("isForum", isForum).toString()
}

/**
 * This object contains full information about a chat.
 * @param id Unique identifier for this chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param type Type of the chat, can be either “private”, “group”, “supergroup” or “channel”
 * @param accentColorId Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header, and link preview. See accent colors for more details.
 * @param maxReactionCount The maximum number of reactions that can be set on a message in the chat
 * @param title Optional. Title, for supergroups, channels and group chats
 * @param username Optional. Username, for private chats, supergroups and channels if available
 * @param firstName Optional. First name of the other party in a private chat
 * @param lastName Optional. Last name of the other party in a private chat
 * @param isForum Optional. True, if the supergroup chat is a forum (has topics enabled)
 * @param photo Optional. Chat photo
 * @param activeUsernames Optional. If non-empty, the list of all active chat usernames; for private chats, supergroups and channels
 * @param birthdate Optional. For private chats, the date of birth of the user
 * @param businessIntro Optional. For private chats with business accounts, the intro of the business
 * @param businessLocation Optional. For private chats with business accounts, the location of the business
 * @param businessOpeningHours Optional. For private chats with business accounts, the opening hours of the business
 * @param personalChat Optional. For private chats, the personal channel of the user
 * @param availableReactions Optional. List of available reactions allowed in the chat. If omitted, then all emoji reactions are allowed.
 * @param backgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for the reply header and link preview background
 * @param profileAccentColorId Optional. Identifier of the accent color for the chat's profile background. See profile accent colors for more details.
 * @param profileBackgroundCustomEmojiId Optional. Custom emoji identifier of the emoji chosen by the chat for its profile background
 * @param emojiStatusCustomEmojiId Optional. Custom emoji identifier of the emoji status of the chat or the other party in a private chat
 * @param emojiStatusExpirationDate Optional. Expiration date of the emoji status of the chat or the other party in a private chat, in Unix time, if any
 * @param bio Optional. Bio of the other party in a private chat
 * @param hasPrivateForwards Optional. True, if privacy settings of the other party in the private chat allows to use tg://user?id=<user_id> links only in chats with the user
 * @param hasRestrictedVoiceAndVideoMessages Optional. True, if the privacy settings of the other party restrict sending voice and video note messages in the private chat
 * @param joinToSendMessages Optional. True, if users need to join the supergroup before they can send messages
 * @param joinByRequest Optional. True, if all users directly joining the supergroup without using an invite link need to be approved by supergroup administrators
 * @param description Optional. Description, for groups, supergroups and channel chats
 * @param inviteLink Optional. Primary invite link, for groups, supergroups and channel chats
 * @param pinnedMessage Optional. The most recent pinned message (by sending date)
 * @param permissions Optional. Default chat member permissions, for groups and supergroups
 * @param slowModeDelay Optional. For supergroups, the minimum allowed delay between consecutive messages sent by each unprivileged user; in seconds
 * @param unrestrictBoostCount Optional. For supergroups, the minimum number of boosts that a non-administrator user needs to add in order to ignore slow mode and chat permissions
 * @param messageAutoDeleteTime Optional. The time after which all messages sent to the chat will be automatically deleted; in seconds
 * @param hasAggressiveAntiSpamEnabled Optional. True, if aggressive anti-spam checks are enabled in the supergroup. The field is only available to chat administrators.
 * @param hasHiddenMembers Optional. True, if non-administrators can only get the list of bots and administrators in the chat
 * @param hasProtectedContent Optional. True, if messages from the chat can't be forwarded to other chats
 * @param hasVisibleHistory Optional. True, if new chat members will have access to old messages; available only to chat administrators
 * @param stickerSetName Optional. For supergroups, name of the group sticker set
 * @param canSetStickerSet Optional. True, if the bot can change the group sticker set
 * @param customEmojiStickerSetName Optional. For supergroups, the name of the group's custom emoji sticker set. Custom emoji from this set can be used by all users and bots in the group.
 * @param linkedChatId Optional. Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa; for supergroups and channel chats. This identifier may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier.
 * @param location Optional. For supergroups, the location to which the supergroup is connected
 * @param canSendPaidMedia Optional. True, if paid media messages can be sent or forwarded to the channel chat. The field is available only for channel chats.
 * @param acceptedGiftTypes Information about types of gifts that are accepted by the chat or by the corresponding user for private chats
 */
@Serializable
data class ChatFullInfo(
    val id: Long,
    val type: String,
    val accentColorId: Long,
    val maxReactionCount: Long,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val isForum: Boolean? = null,
    val photo: ChatPhoto? = null,
    val activeUsernames: List<String>? = null,
    val birthdate: Birthdate? = null,
    val businessIntro: BusinessIntro? = null,
    val businessLocation: BusinessLocation? = null,
    val businessOpeningHours: BusinessOpeningHours? = null,
    val personalChat: Chat? = null,
    val availableReactions: List<ReactionType>? = null,
    val backgroundCustomEmojiId: CustomEmojiId? = null,
    val profileAccentColorId: Long? = null,
    val profileBackgroundCustomEmojiId: CustomEmojiId? = null,
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
    val unrestrictBoostCount: Long? = null,
    val messageAutoDeleteTime: Long? = null,
    val hasAggressiveAntiSpamEnabled: Boolean? = null,
    val hasHiddenMembers: Boolean? = null,
    val hasProtectedContent: Boolean? = null,
    val hasVisibleHistory: Boolean? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean? = null,
    val customEmojiStickerSetName: String? = null,
    val linkedChatId: ChatId? = null,
    val location: ChatLocation? = null,
    val canSendPaidMedia: Boolean? = null,
    val acceptedGiftTypes: AcceptedGiftTypes,
) {
    override fun toString() = DebugStringBuilder("ChatFullInfo").prop("id", id).prop("type", type).prop("accentColorId", accentColorId).prop("maxReactionCount", maxReactionCount).prop("title", title).prop("username", username).prop("firstName", firstName).prop("lastName", lastName).prop("isForum", isForum).prop("photo", photo).prop("activeUsernames", activeUsernames).prop("birthdate", birthdate).prop("businessIntro", businessIntro).prop("businessLocation", businessLocation).prop("businessOpeningHours", businessOpeningHours).prop("personalChat", personalChat).prop("availableReactions", availableReactions).prop("backgroundCustomEmojiId", backgroundCustomEmojiId).prop("profileAccentColorId", profileAccentColorId).prop("profileBackgroundCustomEmojiId", profileBackgroundCustomEmojiId).prop("emojiStatusCustomEmojiId", emojiStatusCustomEmojiId).prop("emojiStatusExpirationDate", emojiStatusExpirationDate).prop("bio", bio).prop("hasPrivateForwards", hasPrivateForwards).prop("hasRestrictedVoiceAndVideoMessages", hasRestrictedVoiceAndVideoMessages).prop("joinToSendMessages", joinToSendMessages).prop("joinByRequest", joinByRequest).prop("description", description).prop("inviteLink", inviteLink).prop("pinnedMessage", pinnedMessage).prop("permissions", permissions).prop("slowModeDelay", slowModeDelay).prop("unrestrictBoostCount", unrestrictBoostCount).prop("messageAutoDeleteTime", messageAutoDeleteTime).prop("hasAggressiveAntiSpamEnabled", hasAggressiveAntiSpamEnabled).prop("hasHiddenMembers", hasHiddenMembers).prop("hasProtectedContent", hasProtectedContent).prop("hasVisibleHistory", hasVisibleHistory).prop("stickerSetName", stickerSetName).prop("canSetStickerSet", canSetStickerSet).prop("customEmojiStickerSetName", customEmojiStickerSetName).prop("linkedChatId", linkedChatId).prop("location", location).prop("canSendPaidMedia", canSendPaidMedia).prop("acceptedGiftTypes", acceptedGiftTypes).toString()
}

/**
 * This object represents a message.
 * @param messageId Unique message identifier inside this chat. In specific instances (e.g., message containing a video sent to a big chat), the server might automatically schedule a message instead of sending it immediately. In such cases, this field will be 0 and the relevant message will be unusable until it is actually sent
 * @param date Date the message was sent in Unix time. It is always a positive number, representing a valid date.
 * @param chat Chat the message belongs to
 * @param messageThreadId Optional. Unique identifier of a message thread to which the message belongs; for supergroups only
 * @param from Optional. Sender of the message; may be empty for messages sent to channels. For backward compatibility, if the message was sent on behalf of a chat, the field contains a fake sender user in non-channel chats
 * @param senderChat Optional. Sender of the message when sent on behalf of a chat. For example, the supergroup itself for messages sent by its anonymous administrators or a linked channel for messages automatically forwarded to the channel's discussion group. For backward compatibility, if the message was sent on behalf of a chat, the field from contains a fake sender user in non-channel chats.
 * @param senderBoostCount Optional. If the sender of the message boosted the chat, the number of boosts added by the user
 * @param senderBusinessBot Optional. The bot that actually sent the message on behalf of the business account. Available only for outgoing messages sent on behalf of the connected business account.
 * @param businessConnectionId Optional. Unique identifier of the business connection from which the message was received. If non-empty, the message belongs to a chat of the corresponding business account that is independent from any potential bot chat which might share the same identifier.
 * @param forwardOrigin Optional. Information about the original message for forwarded messages
 * @param isTopicMessage Optional. True, if the message is sent to a forum topic
 * @param isAutomaticForward Optional. True, if the message is a channel post that was automatically forwarded to the connected discussion group
 * @param replyToMessage Optional. For replies in the same chat and message thread, the original message. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @param externalReply Optional. Information about the message that is being replied to, which may come from another chat or forum topic
 * @param quote Optional. For replies that quote part of the original message, the quoted part of the message
 * @param replyToStory Optional. For replies to a story, the original story
 * @param viaBot Optional. Bot through which the message was sent
 * @param editDate Optional. Date the message was last edited in Unix time
 * @param hasProtectedContent Optional. True, if the message can't be forwarded
 * @param isFromOffline Optional. True, if the message was sent by an implicit action, for example, as an away or a greeting business message, or as a scheduled message
 * @param mediaGroupId Optional. The unique identifier of a media message group this message belongs to
 * @param authorSignature Optional. Signature of the post author for messages in channels, or the custom title of an anonymous group administrator
 * @param text Optional. For text messages, the actual UTF-8 text of the message
 * @param entities Optional. For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
 * @param linkPreviewOptions Optional. Options used for link preview generation for the message, if it is a text message and link preview options were changed
 * @param effectId Optional. Unique identifier of the message effect added to the message
 * @param animation Optional. Message is an animation, information about the animation. For backward compatibility, when this field is set, the document field will also be set
 * @param audio Optional. Message is an audio file, information about the file
 * @param document Optional. Message is a general file, information about the file
 * @param photo Optional. Message is a photo, available sizes of the photo
 * @param sticker Optional. Message is a sticker, information about the sticker
 * @param story Optional. Message is a forwarded story
 * @param video Optional. Message is a video, information about the video
 * @param videoNote Optional. Message is a video note, information about the video message
 * @param voice Optional. Message is a voice message, information about the file
 * @param caption Optional. Caption for the animation, audio, document, paid media, photo, video or voice
 * @param captionEntities Optional. For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
 * @param showCaptionAboveMedia Optional. True, if the caption must be shown above the message media
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
 * @param pinnedMessage Optional. Specified message was pinned. Note that the Message object in this field will not contain further reply_to_message fields even if it itself is a reply.
 * @param invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments »
 * @param successfulPayment Optional. Message is a service message about a successful payment, information about the payment. More about payments »
 * @param usersShared Optional. Service message: users were shared with the bot
 * @param chatShared Optional. Service message: a chat was shared with the bot
 * @param connectedWebsite Optional. The domain name of the website on which the user has logged in. More about Telegram Login »
 * @param writeAccessAllowed Optional. Service message: the user allowed the bot to write messages after adding it to the attachment or side menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess
 * @param passportData Optional. Telegram Passport data
 * @param proximityAlertTriggered Optional. Service message. A user in the chat triggered another user's proximity alert while sharing Live Location.
 * @param boostAdded Optional. Service message: user boosted the chat
 * @param chatBackgroundSet Optional. Service message: chat background set
 * @param forumTopicCreated Optional. Service message: forum topic created
 * @param forumTopicEdited Optional. Service message: forum topic edited
 * @param forumTopicClosed Optional. Service message: forum topic closed
 * @param forumTopicReopened Optional. Service message: forum topic reopened
 * @param generalForumTopicHidden Optional. Service message: the 'General' forum topic hidden
 * @param generalForumTopicUnhidden Optional. Service message: the 'General' forum topic unhidden
 * @param giveawayCreated Optional. Service message: a scheduled giveaway was created
 * @param giveaway Optional. The message is a scheduled giveaway message
 * @param giveawayWinners Optional. A giveaway with public winners was completed
 * @param giveawayCompleted Optional. Service message: a giveaway without public winners was completed
 * @param videoChatScheduled Optional. Service message: video chat scheduled
 * @param videoChatStarted Optional. Service message: video chat started
 * @param videoChatEnded Optional. Service message: video chat ended
 * @param videoChatParticipantsInvited Optional. Service message: new participants invited to a video chat
 * @param webAppData Optional. Service message: data sent by a Web App
 * @param replyMarkup Optional. Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons.
 * @param paidMedia Optional. Message contains paid media; information about the paid media
 * @param refundedPayment Optional. Message is a service message about a refunded payment, information about the payment. More about payments »
 * @param paidStarCount Optional. The number of Telegram Stars that were paid by the sender of the message to send it
 * @param gift Optional. Service message: a regular gift was sent or received
 * @param uniqueGift Optional. Service message: a unique gift was sent or received
 * @param paidMessagePriceChanged Optional. Service message: the price for paid messages has changed in the chat
 */
@Serializable
data class Message(
    val messageId: MessageId,
    val date: UnixTimestamp,
    val chat: Chat,
    val messageThreadId: MessageThreadId? = null,
    val from: User? = null,
    val senderChat: Chat? = null,
    val senderBoostCount: Long? = null,
    val senderBusinessBot: User? = null,
    val businessConnectionId: BusinessConnectionId? = null,
    val forwardOrigin: MessageOrigin? = null,
    val isTopicMessage: Boolean? = null,
    val isAutomaticForward: Boolean? = null,
    val replyToMessage: Message? = null,
    val externalReply: ExternalReplyInfo? = null,
    val quote: TextQuote? = null,
    val replyToStory: Story? = null,
    val viaBot: User? = null,
    val editDate: UnixTimestamp? = null,
    val hasProtectedContent: Boolean? = null,
    val isFromOffline: Boolean? = null,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val effectId: MessageEffectId? = null,
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
    val showCaptionAboveMedia: Boolean? = null,
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
    val usersShared: UsersShared? = null,
    val chatShared: ChatShared? = null,
    val connectedWebsite: String? = null,
    val writeAccessAllowed: WriteAccessAllowed? = null,
    val passportData: PassportData? = null,
    val proximityAlertTriggered: ProximityAlertTriggered? = null,
    val boostAdded: ChatBoostAdded? = null,
    val chatBackgroundSet: ChatBackground? = null,
    val forumTopicCreated: ForumTopicCreated? = null,
    val forumTopicEdited: ForumTopicEdited? = null,
    val forumTopicClosed: ForumTopicClosed? = null,
    val forumTopicReopened: ForumTopicReopened? = null,
    val generalForumTopicHidden: GeneralForumTopicHidden? = null,
    val generalForumTopicUnhidden: GeneralForumTopicUnhidden? = null,
    val giveawayCreated: GiveawayCreated? = null,
    val giveaway: Giveaway? = null,
    val giveawayWinners: GiveawayWinners? = null,
    val giveawayCompleted: GiveawayCompleted? = null,
    val videoChatScheduled: VideoChatScheduled? = null,
    val videoChatStarted: VideoChatStarted? = null,
    val videoChatEnded: VideoChatEnded? = null,
    val videoChatParticipantsInvited: VideoChatParticipantsInvited? = null,
    val webAppData: WebAppData? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val paidMedia: PaidMediaInfo? = null,
    val refundedPayment: RefundedPayment? = null,
    val paidStarCount: Long? = null,
    val gift: GiftInfo? = null,
    val uniqueGift: UniqueGiftInfo? = null,
    val paidMessagePriceChanged: PaidMessagePriceChanged? = null,
) {
    override fun toString() = DebugStringBuilder("Message").prop("messageId", messageId).prop("date", date).prop("chat", chat).prop("messageThreadId", messageThreadId).prop("from", from).prop("senderChat", senderChat).prop("senderBoostCount", senderBoostCount).prop("senderBusinessBot", senderBusinessBot).prop("businessConnectionId", businessConnectionId).prop("forwardOrigin", forwardOrigin).prop("isTopicMessage", isTopicMessage).prop("isAutomaticForward", isAutomaticForward).prop("replyToMessage", replyToMessage).prop("externalReply", externalReply).prop("quote", quote).prop("replyToStory", replyToStory).prop("viaBot", viaBot).prop("editDate", editDate).prop("hasProtectedContent", hasProtectedContent).prop("isFromOffline", isFromOffline).prop("mediaGroupId", mediaGroupId).prop("authorSignature", authorSignature).prop("text", text).prop("entities", entities).prop("linkPreviewOptions", linkPreviewOptions).prop("effectId", effectId).prop("animation", animation).prop("audio", audio).prop("document", document).prop("photo", photo).prop("sticker", sticker).prop("story", story).prop("video", video).prop("videoNote", videoNote).prop("voice", voice).prop("caption", caption).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("hasMediaSpoiler", hasMediaSpoiler).prop("contact", contact).prop("dice", dice).prop("game", game).prop("poll", poll).prop("venue", venue).prop("location", location).prop("newChatMembers", newChatMembers).prop("leftChatMember", leftChatMember).prop("newChatTitle", newChatTitle).prop("newChatPhoto", newChatPhoto).prop("deleteChatPhoto", deleteChatPhoto).prop("groupChatCreated", groupChatCreated).prop("supergroupChatCreated", supergroupChatCreated).prop("channelChatCreated", channelChatCreated).prop("messageAutoDeleteTimerChanged", messageAutoDeleteTimerChanged).prop("migrateToChatId", migrateToChatId).prop("migrateFromChatId", migrateFromChatId).prop("pinnedMessage", pinnedMessage).prop("invoice", invoice).prop("successfulPayment", successfulPayment).prop("usersShared", usersShared).prop("chatShared", chatShared).prop("connectedWebsite", connectedWebsite).prop("writeAccessAllowed", writeAccessAllowed).prop("passportData", passportData).prop("proximityAlertTriggered", proximityAlertTriggered).prop("boostAdded", boostAdded).prop("chatBackgroundSet", chatBackgroundSet).prop("forumTopicCreated", forumTopicCreated).prop("forumTopicEdited", forumTopicEdited).prop("forumTopicClosed", forumTopicClosed).prop("forumTopicReopened", forumTopicReopened).prop("generalForumTopicHidden", generalForumTopicHidden).prop("generalForumTopicUnhidden", generalForumTopicUnhidden).prop("giveawayCreated", giveawayCreated).prop("giveaway", giveaway).prop("giveawayWinners", giveawayWinners).prop("giveawayCompleted", giveawayCompleted).prop("videoChatScheduled", videoChatScheduled).prop("videoChatStarted", videoChatStarted).prop("videoChatEnded", videoChatEnded).prop("videoChatParticipantsInvited", videoChatParticipantsInvited).prop("webAppData", webAppData).prop("replyMarkup", replyMarkup).prop("paidMedia", paidMedia).prop("refundedPayment", refundedPayment).prop("paidStarCount", paidStarCount).prop("gift", gift).prop("uniqueGift", uniqueGift).prop("paidMessagePriceChanged", paidMessagePriceChanged).toString()
}

/**
 * This object represents a unique message identifier.
 * @param messageId Unique message identifier. In specific instances (e.g., message containing a video sent to a big chat), the server might automatically schedule a message instead of sending it immediately. In such cases, this field will be 0 and the relevant message will be unusable until it is actually sent
 */
@Serializable
data class MessageRef(
    val messageId: MessageId,
) {
    override fun toString() = DebugStringBuilder("MessageRef").prop("messageId", messageId).toString()
}

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 * @param type Type of the entity. Currently, can be “mention” (@username), “hashtag” (#hashtag or #hashtag@chatusername), “cashtag” ($USD or $USD@chatusername), “bot_command” (/start@jobs_bot), “url” (https://telegram.org), “email” (do-not-reply@telegram.org), “phone_number” (+1-212-555-0123), “bold” (bold text), “italic” (italic text), “underline” (underlined text), “strikethrough” (strikethrough text), “spoiler” (spoiler message), “blockquote” (block quotation), “expandable_blockquote” (collapsed-by-default block quotation), “code” (monowidth string), “pre” (monowidth block), “text_link” (for clickable text URLs), “text_mention” (for users without usernames), “custom_emoji” (for inline custom emoji stickers)
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
 * This object contains information about the quoted part of a message that is replied to by the given message.
 * @param text Text of the quoted part of a message that is replied to by the given message
 * @param position Approximate quote position in the original message in UTF-16 code units as specified by the sender
 * @param entities Optional. Special entities that appear in the quote. Currently, only bold, italic, underline, strikethrough, spoiler, and custom_emoji entities are kept in quotes.
 * @param isManual Optional. True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server.
 */
@Serializable
data class TextQuote(
    val text: String,
    val position: Long,
    val entities: List<MessageEntity>? = null,
    val isManual: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("TextQuote").prop("text", text).prop("position", position).prop("entities", entities).prop("isManual", isManual).toString()
}

/**
 * This object contains information about a message that is being replied to, which may come from another chat or forum topic.
 * @param origin Origin of the message replied to by the given message
 * @param chat Optional. Chat the original message belongs to. Available only if the chat is a supergroup or a channel.
 * @param messageId Optional. Unique message identifier inside the original chat. Available only if the original chat is a supergroup or a channel.
 * @param linkPreviewOptions Optional. Options used for link preview generation for the original message, if it is a text message
 * @param animation Optional. Message is an animation, information about the animation
 * @param audio Optional. Message is an audio file, information about the file
 * @param document Optional. Message is a general file, information about the file
 * @param photo Optional. Message is a photo, available sizes of the photo
 * @param sticker Optional. Message is a sticker, information about the sticker
 * @param story Optional. Message is a forwarded story
 * @param video Optional. Message is a video, information about the video
 * @param videoNote Optional. Message is a video note, information about the video message
 * @param voice Optional. Message is a voice message, information about the file
 * @param hasMediaSpoiler Optional. True, if the message media is covered by a spoiler animation
 * @param contact Optional. Message is a shared contact, information about the contact
 * @param dice Optional. Message is a dice with random value
 * @param game Optional. Message is a game, information about the game. More about games »
 * @param giveaway Optional. Message is a scheduled giveaway, information about the giveaway
 * @param giveawayWinners Optional. A giveaway with public winners was completed
 * @param invoice Optional. Message is an invoice for a payment, information about the invoice. More about payments »
 * @param location Optional. Message is a shared location, information about the location
 * @param poll Optional. Message is a native poll, information about the poll
 * @param venue Optional. Message is a venue, information about the venue
 * @param paidMedia Optional. Message contains paid media; information about the paid media
 */
@Serializable
data class ExternalReplyInfo(
    val origin: MessageOrigin,
    val chat: Chat? = null,
    val messageId: MessageId? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    val hasMediaSpoiler: Boolean? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val giveaway: Giveaway? = null,
    val giveawayWinners: GiveawayWinners? = null,
    val invoice: Invoice? = null,
    val location: Location? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
    val paidMedia: PaidMediaInfo? = null,
) {
    override fun toString() = DebugStringBuilder("ExternalReplyInfo").prop("origin", origin).prop("chat", chat).prop("messageId", messageId).prop("linkPreviewOptions", linkPreviewOptions).prop("animation", animation).prop("audio", audio).prop("document", document).prop("photo", photo).prop("sticker", sticker).prop("story", story).prop("video", video).prop("videoNote", videoNote).prop("voice", voice).prop("hasMediaSpoiler", hasMediaSpoiler).prop("contact", contact).prop("dice", dice).prop("game", game).prop("giveaway", giveaway).prop("giveawayWinners", giveawayWinners).prop("invoice", invoice).prop("location", location).prop("poll", poll).prop("venue", venue).prop("paidMedia", paidMedia).toString()
}

/**
 * Describes reply parameters for the message that is being sent.
 * @param messageId Identifier of the message that will be replied to in the current chat, or in the chat chat_id if it is specified
 * @param chatId Optional. If the message to be replied to is from a different chat, unique identifier for the chat or username of the channel (in the format @channelusername). Not supported for messages sent on behalf of a business account.
 * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified message to be replied to is not found. Always False for replies in another chat or forum topic. Always True for messages sent on behalf of a business account.
 * @param quote Optional. Quoted part of the message to be replied to; 0-1024 characters after entities parsing. The quote must be an exact substring of the message to be replied to, including bold, italic, underline, strikethrough, spoiler, and custom_emoji entities. The message will fail to send if the quote isn't found in the original message.
 * @param quoteParseMode Optional. Mode for parsing entities in the quote. See formatting options for more details.
 * @param quoteEntities Optional. A JSON-serialized list of special entities that appear in the quote. It can be specified instead of quote_parse_mode.
 * @param quotePosition Optional. Position of the quote in the original message in UTF-16 code units
 */
@Serializable
data class ReplyParameters(
    val messageId: MessageId,
    val chatId: ChatId? = null,
    val allowSendingWithoutReply: Boolean? = null,
    val quote: String? = null,
    val quoteParseMode: String? = null,
    val quoteEntities: List<MessageEntity>? = null,
    val quotePosition: Long? = null,
) {
    override fun toString() = DebugStringBuilder("ReplyParameters").prop("messageId", messageId).prop("chatId", chatId).prop("allowSendingWithoutReply", allowSendingWithoutReply).prop("quote", quote).prop("quoteParseMode", quoteParseMode).prop("quoteEntities", quoteEntities).prop("quotePosition", quotePosition).toString()
}

/**
 * This object describes the origin of a message. It can be one of
 * - [MessageOriginUser]
 * - [MessageOriginHiddenUser]
 * - [MessageOriginChat]
 * - [MessageOriginChannel]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface MessageOrigin

/**
 * The message was originally sent by a known user.
 * @param date Date the message was sent originally in Unix time
 * @param senderUser User that sent the message originally
 */
@Serializable
@SerialName("user")
data class MessageOriginUser(
    val date: UnixTimestamp,
    val senderUser: User,
) : MessageOrigin {
    override fun toString() = DebugStringBuilder("MessageOriginUser").prop("date", date).prop("senderUser", senderUser).toString()
}

/**
 * The message was originally sent by an unknown user.
 * @param date Date the message was sent originally in Unix time
 * @param senderUserName Name of the user that sent the message originally
 */
@Serializable
@SerialName("hidden_user")
data class MessageOriginHiddenUser(
    val date: UnixTimestamp,
    val senderUserName: String,
) : MessageOrigin {
    override fun toString() = DebugStringBuilder("MessageOriginHiddenUser").prop("date", date).prop("senderUserName", senderUserName).toString()
}

/**
 * The message was originally sent on behalf of a chat to a group chat.
 * @param date Date the message was sent originally in Unix time
 * @param senderChat Chat that sent the message originally
 * @param authorSignature Optional. For messages originally sent by an anonymous chat administrator, original message author signature
 */
@Serializable
@SerialName("chat")
data class MessageOriginChat(
    val date: UnixTimestamp,
    val senderChat: Chat,
    val authorSignature: String? = null,
) : MessageOrigin {
    override fun toString() = DebugStringBuilder("MessageOriginChat").prop("date", date).prop("senderChat", senderChat).prop("authorSignature", authorSignature).toString()
}

/**
 * The message was originally sent to a channel chat.
 * @param date Date the message was sent originally in Unix time
 * @param chat Channel chat to which the message was originally sent
 * @param messageId Unique message identifier inside the chat
 * @param authorSignature Optional. Signature of the original post author
 */
@Serializable
@SerialName("channel")
data class MessageOriginChannel(
    val date: UnixTimestamp,
    val chat: Chat,
    val messageId: MessageId,
    val authorSignature: String? = null,
) : MessageOrigin {
    override fun toString() = DebugStringBuilder("MessageOriginChannel").prop("date", date).prop("chat", chat).prop("messageId", messageId).prop("authorSignature", authorSignature).toString()
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
 * @param width Video width as defined by the sender
 * @param height Video height as defined by the sender
 * @param duration Duration of the video in seconds as defined by the sender
 * @param thumbnail Optional. Animation thumbnail as defined by the sender
 * @param fileName Optional. Original animation filename as defined by the sender
 * @param mimeType Optional. MIME type of the file as defined by the sender
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
 * @param duration Duration of the audio in seconds as defined by the sender
 * @param performer Optional. Performer of the audio as defined by the sender or by audio tags
 * @param title Optional. Title of the audio as defined by the sender or by audio tags
 * @param fileName Optional. Original filename as defined by the sender
 * @param mimeType Optional. MIME type of the file as defined by the sender
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
 * @param thumbnail Optional. Document thumbnail as defined by the sender
 * @param fileName Optional. Original filename as defined by the sender
 * @param mimeType Optional. MIME type of the file as defined by the sender
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
 * This object represents a story.
 * @param chat Chat that posted the story
 * @param id Unique identifier for the story in the chat
 */
@Serializable
data class Story(
    val chat: Chat,
    val id: Long,
) {
    override fun toString() = DebugStringBuilder("Story").prop("chat", chat).prop("id", id).toString()
}

/**
 * This object represents a video file.
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Video width as defined by the sender
 * @param height Video height as defined by the sender
 * @param duration Duration of the video in seconds as defined by the sender
 * @param thumbnail Optional. Video thumbnail
 * @param fileName Optional. Original filename as defined by the sender
 * @param mimeType Optional. MIME type of the file as defined by the sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 * @param cover Optional. Available sizes of the cover of the video in the message
 * @param startTimestamp Optional. Timestamp in seconds from which the video will play in the message
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
    val cover: List<PhotoSize>? = null,
    val startTimestamp: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Video").prop("fileId", fileId).prop("fileUniqueId", fileUniqueId).prop("width", width).prop("height", height).prop("duration", duration).prop("thumbnail", thumbnail).prop("fileName", fileName).prop("mimeType", mimeType).prop("fileSize", fileSize).prop("cover", cover).prop("startTimestamp", startTimestamp).toString()
}

/**
 * This object represents a video message (available in Telegram apps as of v.4.0).
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param length Video width and height (diameter of the video message) as defined by the sender
 * @param duration Duration of the video in seconds as defined by the sender
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
 * @param duration Duration of the audio in seconds as defined by the sender
 * @param mimeType Optional. MIME type of the file as defined by the sender
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
 * Describes the paid media added to a message.
 * @param starCount The number of Telegram Stars that must be paid to buy access to the media
 * @param paidMedia Information about the paid media
 */
@Serializable
data class PaidMediaInfo(
    val starCount: Long,
    val paidMedia: List<PaidMedia>,
) {
    override fun toString() = DebugStringBuilder("PaidMediaInfo").prop("starCount", starCount).prop("paidMedia", paidMedia).toString()
}

/**
 * This object describes paid media. Currently, it can be one of
 * - [PaidMediaPreview]
 * - [PaidMediaPhoto]
 * - [PaidMediaVideo]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface PaidMedia

/**
 * The paid media isn't available before the payment.
 * @param width Optional. Media width as defined by the sender
 * @param height Optional. Media height as defined by the sender
 * @param duration Optional. Duration of the media in seconds as defined by the sender
 */
@Serializable
@SerialName("preview")
data class PaidMediaPreview(
    val width: Long? = null,
    val height: Long? = null,
    val duration: Seconds? = null,
) : PaidMedia {
    override fun toString() = DebugStringBuilder("PaidMediaPreview").prop("width", width).prop("height", height).prop("duration", duration).toString()
}

/**
 * The paid media is a photo.
 * @param photo The photo
 */
@Serializable
@SerialName("photo")
data class PaidMediaPhoto(
    val photo: List<PhotoSize>,
) : PaidMedia {
    override fun toString() = DebugStringBuilder("PaidMediaPhoto").prop("photo", photo).toString()
}

/**
 * The paid media is a video.
 * @param video The video
 */
@Serializable
@SerialName("video")
data class PaidMediaVideo(
    val video: Video,
) : PaidMedia {
    override fun toString() = DebugStringBuilder("PaidMediaVideo").prop("video", video).toString()
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
 * @param textEntities Optional. Special entities that appear in the option text. Currently, only custom emoji entities are allowed in poll option texts
 */
@Serializable
data class PollOption(
    val text: String,
    val voterCount: Long,
    val textEntities: List<MessageEntity>? = null,
) {
    override fun toString() = DebugStringBuilder("PollOption").prop("text", text).prop("voterCount", voterCount).prop("textEntities", textEntities).toString()
}

/**
 * This object contains information about one answer option in a poll to be sent.
 * @param text Option text, 1-100 characters
 * @param textParseMode Optional. Mode for parsing entities in the text. See formatting options for more details. Currently, only custom emoji entities are allowed
 * @param textEntities Optional. A JSON-serialized list of special entities that appear in the poll option text. It can be specified instead of text_parse_mode
 */
@Serializable
data class InputPollOption(
    val text: String,
    val textParseMode: String? = null,
    val textEntities: List<MessageEntity>? = null,
) {
    override fun toString() = DebugStringBuilder("InputPollOption").prop("text", text).prop("textParseMode", textParseMode).prop("textEntities", textEntities).toString()
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
 * @param questionEntities Optional. Special entities that appear in the question. Currently, only custom emoji entities are allowed in poll questions
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
    val questionEntities: List<MessageEntity>? = null,
    val correctOptionId: Long? = null,
    val explanation: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Seconds? = null,
    val closeDate: UnixTimestamp? = null,
) {
    override fun toString() = DebugStringBuilder("Poll").prop("id", id).prop("question", question).prop("options", options).prop("totalVoterCount", totalVoterCount).prop("isClosed", isClosed).prop("isAnonymous", isAnonymous).prop("type", type).prop("allowsMultipleAnswers", allowsMultipleAnswers).prop("questionEntities", questionEntities).prop("correctOptionId", correctOptionId).prop("explanation", explanation).prop("explanationEntities", explanationEntities).prop("openPeriod", openPeriod).prop("closeDate", closeDate).toString()
}

/**
 * This object represents a point on the map.
 * @param latitude Latitude as defined by the sender
 * @param longitude Longitude as defined by the sender
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Time relative to the message sending date, during which the location can be updated; in seconds. For active live locations only.
 * @param heading Optional. The direction in which user is moving, in degrees; 1-360. For active live locations only.
 * @param proximityAlertRadius Optional. The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only.
 */
@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Location").prop("latitude", latitude).prop("longitude", longitude).prop("horizontalAccuracy", horizontalAccuracy).prop("livePeriod", livePeriod).prop("heading", heading).prop("proximityAlertRadius", proximityAlertRadius).toString()
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
 * This object represents a service message about a user boosting a chat.
 * @param boostCount Number of boosts added by the user
 */
@Serializable
data class ChatBoostAdded(
    val boostCount: Long,
) {
    override fun toString() = DebugStringBuilder("ChatBoostAdded").prop("boostCount", boostCount).toString()
}

/**
 * This object describes the way a background is filled based on the selected colors. Currently, it can be one of
 * - [BackgroundFillSolid]
 * - [BackgroundFillGradient]
 * - [BackgroundFillFreeformGradient]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface BackgroundFill

/**
 * The background is filled using the selected color.
 * @param color The color of the background fill in the RGB24 format
 */
@Serializable
@SerialName("solid")
data class BackgroundFillSolid(
    val color: Long,
) : BackgroundFill {
    override fun toString() = DebugStringBuilder("BackgroundFillSolid").prop("color", color).toString()
}

/**
 * The background is a gradient fill.
 * @param topColor Top color of the gradient in the RGB24 format
 * @param bottomColor Bottom color of the gradient in the RGB24 format
 * @param rotationAngle Clockwise rotation angle of the background fill in degrees; 0-359
 */
@Serializable
@SerialName("gradient")
data class BackgroundFillGradient(
    val topColor: Long,
    val bottomColor: Long,
    val rotationAngle: Long,
) : BackgroundFill {
    override fun toString() = DebugStringBuilder("BackgroundFillGradient").prop("topColor", topColor).prop("bottomColor", bottomColor).prop("rotationAngle", rotationAngle).toString()
}

/**
 * The background is a freeform gradient that rotates after every message in the chat.
 * @param colors A list of the 3 or 4 base colors that are used to generate the freeform gradient in the RGB24 format
 */
@Serializable
@SerialName("freeform_gradient")
data class BackgroundFillFreeformGradient(
    val colors: List<Long>,
) : BackgroundFill {
    override fun toString() = DebugStringBuilder("BackgroundFillFreeformGradient").prop("colors", colors).toString()
}

/**
 * This object describes the type of a background. Currently, it can be one of
 * - [BackgroundTypeFill]
 * - [BackgroundTypeWallpaper]
 * - [BackgroundTypePattern]
 * - [BackgroundTypeChatTheme]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface BackgroundType

/**
 * The background is automatically filled based on the selected colors.
 * @param fill The background fill
 * @param darkThemeDimming Dimming of the background in dark themes, as a percentage; 0-100
 */
@Serializable
@SerialName("fill")
data class BackgroundTypeFill(
    val fill: BackgroundFill,
    val darkThemeDimming: Long,
) : BackgroundType {
    override fun toString() = DebugStringBuilder("BackgroundTypeFill").prop("fill", fill).prop("darkThemeDimming", darkThemeDimming).toString()
}

/**
 * The background is a wallpaper in the JPEG format.
 * @param document Document with the wallpaper
 * @param darkThemeDimming Dimming of the background in dark themes, as a percentage; 0-100
 * @param isBlurred Optional. True, if the wallpaper is downscaled to fit in a 450x450 square and then box-blurred with radius 12
 * @param isMoving Optional. True, if the background moves slightly when the device is tilted
 */
@Serializable
@SerialName("wallpaper")
data class BackgroundTypeWallpaper(
    val document: Document,
    val darkThemeDimming: Long,
    val isBlurred: Boolean? = null,
    val isMoving: Boolean? = null,
) : BackgroundType {
    override fun toString() = DebugStringBuilder("BackgroundTypeWallpaper").prop("document", document).prop("darkThemeDimming", darkThemeDimming).prop("isBlurred", isBlurred).prop("isMoving", isMoving).toString()
}

/**
 * The background is a .PNG or .TGV (gzipped subset of SVG with MIME type “application/x-tgwallpattern”) pattern to be combined with the background fill chosen by the user.
 * @param document Document with the pattern
 * @param fill The background fill that is combined with the pattern
 * @param intensity Intensity of the pattern when it is shown above the filled background; 0-100
 * @param isInverted Optional. True, if the background fill must be applied only to the pattern itself. All other pixels are black in this case. For dark themes only
 * @param isMoving Optional. True, if the background moves slightly when the device is tilted
 */
@Serializable
@SerialName("pattern")
data class BackgroundTypePattern(
    val document: Document,
    val fill: BackgroundFill,
    val intensity: Long,
    val isInverted: Boolean? = null,
    val isMoving: Boolean? = null,
) : BackgroundType {
    override fun toString() = DebugStringBuilder("BackgroundTypePattern").prop("document", document).prop("fill", fill).prop("intensity", intensity).prop("isInverted", isInverted).prop("isMoving", isMoving).toString()
}

/**
 * The background is taken directly from a built-in chat theme.
 * @param themeName Name of the chat theme, which is usually an emoji
 */
@Serializable
@SerialName("chat_theme")
data class BackgroundTypeChatTheme(
    val themeName: String,
) : BackgroundType {
    override fun toString() = DebugStringBuilder("BackgroundTypeChatTheme").prop("themeName", themeName).toString()
}

/**
 * This object represents a chat background.
 * @param type Type of the background
 */
@Serializable
data class ChatBackground(
    val type: BackgroundType,
) {
    override fun toString() = DebugStringBuilder("ChatBackground").prop("type", type).toString()
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
 * This object contains information about a user that was shared with the bot using a KeyboardButtonRequestUsers button.
 * @param userId Identifier of the shared user. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so 64-bit integers or double-precision float types are safe for storing these identifiers. The bot may not have access to the user and could be unable to use this identifier, unless the user is already known to the bot by some other means.
 * @param firstName Optional. First name of the user, if the name was requested by the bot
 * @param lastName Optional. Last name of the user, if the name was requested by the bot
 * @param username Optional. Username of the user, if the username was requested by the bot
 * @param photo Optional. Available sizes of the chat photo, if the photo was requested by the bot
 */
@Serializable
data class SharedUser(
    val userId: UserId,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val photo: List<PhotoSize>? = null,
) {
    override fun toString() = DebugStringBuilder("SharedUser").prop("userId", userId).prop("firstName", firstName).prop("lastName", lastName).prop("username", username).prop("photo", photo).toString()
}

/**
 * This object contains information about the users whose identifiers were shared with the bot using a KeyboardButtonRequestUsers button.
 * @param requestId Identifier of the request
 * @param users Information about users shared with the bot.
 */
@Serializable
data class UsersShared(
    val requestId: Long,
    val users: List<SharedUser>,
) {
    override fun toString() = DebugStringBuilder("UsersShared").prop("requestId", requestId).prop("users", users).toString()
}

/**
 * This object contains information about a chat that was shared with the bot using a KeyboardButtonRequestChat button.
 * @param requestId Identifier of the request
 * @param chatId Identifier of the shared chat. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot may not have access to the chat and could be unable to use this identifier, unless the chat is already known to the bot by some other means.
 * @param title Optional. Title of the chat, if the title was requested by the bot.
 * @param username Optional. Username of the chat, if the username was requested by the bot and available.
 * @param photo Optional. Available sizes of the chat photo, if the photo was requested by the bot
 */
@Serializable
data class ChatShared(
    val requestId: Long,
    val chatId: ChatId,
    val title: String? = null,
    val username: String? = null,
    val photo: List<PhotoSize>? = null,
) {
    override fun toString() = DebugStringBuilder("ChatShared").prop("requestId", requestId).prop("chatId", chatId).prop("title", title).prop("username", username).prop("photo", photo).toString()
}

/**
 * This object represents a service message about a user allowing a bot to write messages after adding it to the attachment menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess.
 * @param fromRequest Optional. True, if the access was granted after the user accepted an explicit request from a Web App sent by the method requestWriteAccess
 * @param webAppName Optional. Name of the Web App, if the access was granted when the Web App was launched from a link
 * @param fromAttachmentMenu Optional. True, if the access was granted when the bot was added to the attachment or side menu
 */
@Serializable
data class WriteAccessAllowed(
    val fromRequest: Boolean? = null,
    val webAppName: String? = null,
    val fromAttachmentMenu: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("WriteAccessAllowed").prop("fromRequest", fromRequest).prop("webAppName", webAppName).prop("fromAttachmentMenu", fromAttachmentMenu).toString()
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
 * Describes a service message about a change in the price of paid messages within a chat.
 * @param paidMessageStarCount The new number of Telegram Stars that must be paid by non-administrator users of the supergroup chat for each sent message
 */
@Serializable
data class PaidMessagePriceChanged(
    val paidMessageStarCount: Long,
) {
    override fun toString() = DebugStringBuilder("PaidMessagePriceChanged").prop("paidMessageStarCount", paidMessageStarCount).toString()
}

/**
 * This object represents a service message about the creation of a scheduled giveaway.
 * @param prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 */
@Serializable
data class GiveawayCreated(
    val prizeStarCount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("GiveawayCreated").prop("prizeStarCount", prizeStarCount).toString()
}

/**
 * This object represents a message about a scheduled giveaway.
 * @param chats The list of chats which the user must join to participate in the giveaway
 * @param winnersSelectionDate Point in time (Unix timestamp) when winners of the giveaway will be selected
 * @param winnerCount The number of users which are supposed to be selected as winners of the giveaway
 * @param onlyNewMembers Optional. True, if only users who join the chats after the giveaway started should be eligible to win
 * @param hasPublicWinners Optional. True, if the list of giveaway winners will be visible to everyone
 * @param prizeDescription Optional. Description of additional giveaway prize
 * @param countryCodes Optional. A list of two-letter ISO 3166-1 alpha-2 country codes indicating the countries from which eligible users for the giveaway must come. If empty, then all users can participate in the giveaway. Users with a phone number that was bought on Fragment can always participate in giveaways.
 * @param premiumSubscriptionMonthCount Optional. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
 * @param prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 */
@Serializable
data class Giveaway(
    val chats: List<Chat>,
    val winnersSelectionDate: Long,
    val winnerCount: Long,
    val onlyNewMembers: Boolean? = null,
    val hasPublicWinners: Boolean? = null,
    val prizeDescription: String? = null,
    val countryCodes: List<String>? = null,
    val premiumSubscriptionMonthCount: Long? = null,
    val prizeStarCount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Giveaway").prop("chats", chats).prop("winnersSelectionDate", winnersSelectionDate).prop("winnerCount", winnerCount).prop("onlyNewMembers", onlyNewMembers).prop("hasPublicWinners", hasPublicWinners).prop("prizeDescription", prizeDescription).prop("countryCodes", countryCodes).prop("premiumSubscriptionMonthCount", premiumSubscriptionMonthCount).prop("prizeStarCount", prizeStarCount).toString()
}

/**
 * This object represents a message about the completion of a giveaway with public winners.
 * @param chat The chat that created the giveaway
 * @param giveawayMessageId Identifier of the message with the giveaway in the chat
 * @param winnersSelectionDate Point in time (Unix timestamp) when winners of the giveaway were selected
 * @param winnerCount Total number of winners in the giveaway
 * @param winners List of up to 100 winners of the giveaway
 * @param additionalChatCount Optional. The number of other chats the user had to join in order to be eligible for the giveaway
 * @param premiumSubscriptionMonthCount Optional. The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram Premium giveaways only
 * @param unclaimedPrizeCount Optional. Number of undistributed prizes
 * @param onlyNewMembers Optional. True, if only users who had joined the chats after the giveaway started were eligible to win
 * @param wasRefunded Optional. True, if the giveaway was canceled because the payment for it was refunded
 * @param prizeDescription Optional. Description of additional giveaway prize
 * @param prizeStarCount Optional. The number of Telegram Stars that were split between giveaway winners; for Telegram Star giveaways only
 */
@Serializable
data class GiveawayWinners(
    val chat: Chat,
    val giveawayMessageId: MessageId,
    val winnersSelectionDate: Long,
    val winnerCount: Long,
    val winners: List<User>,
    val additionalChatCount: Long? = null,
    val premiumSubscriptionMonthCount: Long? = null,
    val unclaimedPrizeCount: Long? = null,
    val onlyNewMembers: Boolean? = null,
    val wasRefunded: Boolean? = null,
    val prizeDescription: String? = null,
    val prizeStarCount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("GiveawayWinners").prop("chat", chat).prop("giveawayMessageId", giveawayMessageId).prop("winnersSelectionDate", winnersSelectionDate).prop("winnerCount", winnerCount).prop("winners", winners).prop("additionalChatCount", additionalChatCount).prop("premiumSubscriptionMonthCount", premiumSubscriptionMonthCount).prop("unclaimedPrizeCount", unclaimedPrizeCount).prop("onlyNewMembers", onlyNewMembers).prop("wasRefunded", wasRefunded).prop("prizeDescription", prizeDescription).prop("prizeStarCount", prizeStarCount).toString()
}

/**
 * This object represents a service message about the completion of a giveaway without public winners.
 * @param winnerCount Number of winners in the giveaway
 * @param unclaimedPrizeCount Optional. Number of undistributed prizes
 * @param giveawayMessage Optional. Message with the giveaway that was completed, if it wasn't deleted
 * @param isStarGiveaway Optional. True, if the giveaway is a Telegram Star giveaway. Otherwise, currently, the giveaway is a Telegram Premium giveaway.
 */
@Serializable
data class GiveawayCompleted(
    val winnerCount: Long,
    val unclaimedPrizeCount: Long? = null,
    val giveawayMessage: Message? = null,
    val isStarGiveaway: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("GiveawayCompleted").prop("winnerCount", winnerCount).prop("unclaimedPrizeCount", unclaimedPrizeCount).prop("giveawayMessage", giveawayMessage).prop("isStarGiveaway", isStarGiveaway).toString()
}

/**
 * Describes the options used for link preview generation.
 * @param isDisabled Optional. True, if the link preview is disabled
 * @param url Optional. URL to use for the link preview. If empty, then the first URL found in the message text will be used
 * @param preferSmallMedia Optional. True, if the media in the link preview is supposed to be shrunk; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
 * @param preferLargeMedia Optional. True, if the media in the link preview is supposed to be enlarged; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
 * @param showAboveText Optional. True, if the link preview must be shown above the message text; otherwise, the link preview will be shown below the message text
 */
@Serializable
data class LinkPreviewOptions(
    val isDisabled: Boolean? = null,
    val url: String? = null,
    val preferSmallMedia: Boolean? = null,
    val preferLargeMedia: Boolean? = null,
    val showAboveText: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("LinkPreviewOptions").prop("isDisabled", isDisabled).prop("url", url).prop("preferSmallMedia", preferSmallMedia).prop("preferLargeMedia", preferLargeMedia).prop("showAboveText", showAboveText).toString()
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
 * This object represents a custom keyboard with reply options (see Introduction to bots for details and examples). Not supported in channels and for messages sent on behalf of a Telegram Business account.
 * @param keyboard Array of button rows, each represented by an Array of KeyboardButton objects
 * @param isPersistent Optional. Requests clients to always show the keyboard when the regular keyboard is hidden. Defaults to false, in which case the custom keyboard can be hidden and opened with a keyboard icon.
 * @param resizeKeyboard Optional. Requests clients to resize the keyboard vertically for optimal fit (e.g., make the keyboard smaller if there are just two rows of buttons). Defaults to false, in which case the custom keyboard is always of the same height as the app's standard keyboard.
 * @param oneTimeKeyboard Optional. Requests clients to hide the keyboard as soon as it's been used. The keyboard will still be available, but clients will automatically display the usual letter-keyboard in the chat - the user can press a special button in the input field to see the custom keyboard again. Defaults to false.
 * @param inputFieldPlaceholder Optional. The placeholder to be shown in the input field when the keyboard is active; 1-64 characters
 * @param selective Optional. Use this parameter if you want to show the keyboard to specific users only. Targets: 1) users that are @mentioned in the text of the Message object; 2) if the bot's message is a reply to a message in the same chat and forum topic, sender of the original message. Example: A user requests to change the bot's language, bot replies to the request with a keyboard to select the new language. Other users in the group don't see the keyboard.
 */
@Serializable
data class ReplyKeyboardMarkup(
    val keyboard: List<List<KeyboardButton>>,
    val isPersistent: Boolean? = null,
    val resizeKeyboard: Boolean? = null,
    val oneTimeKeyboard: Boolean? = null,
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean? = null,
) : ReplyMarkup {
    override fun toString() = DebugStringBuilder("ReplyKeyboardMarkup").prop("keyboard", keyboard).prop("isPersistent", isPersistent).prop("resizeKeyboard", resizeKeyboard).prop("oneTimeKeyboard", oneTimeKeyboard).prop("inputFieldPlaceholder", inputFieldPlaceholder).prop("selective", selective).toString()
}

/**
 * This object represents one button of the reply keyboard. At most one of the optional fields must be used to specify type of the button. For simple text buttons, String can be used instead of this object to specify the button text.
 * @param text Text of the button. If none of the optional fields are used, it will be sent as a message when the button is pressed
 * @param requestUsers Optional. If specified, pressing the button will open a list of suitable users. Identifiers of selected users will be sent to the bot in a “users_shared” service message. Available in private chats only.
 * @param requestChat Optional. If specified, pressing the button will open a list of suitable chats. Tapping on a chat will send its identifier to the bot in a “chat_shared” service message. Available in private chats only.
 * @param requestContact Optional. If True, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only.
 * @param requestLocation Optional. If True, the user's current location will be sent when the button is pressed. Available in private chats only.
 * @param requestPoll Optional. If specified, the user will be asked to create a poll and send it to the bot when the button is pressed. Available in private chats only.
 * @param webApp Optional. If specified, the described Web App will be launched when the button is pressed. The Web App will be able to send a “web_app_data” service message. Available in private chats only.
 */
@Serializable
data class KeyboardButton(
    val text: String,
    val requestUsers: KeyboardButtonRequestUsers? = null,
    val requestChat: KeyboardButtonRequestChat? = null,
    val requestContact: Boolean? = null,
    val requestLocation: Boolean? = null,
    val requestPoll: KeyboardButtonPollType? = null,
    val webApp: WebAppInfo? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButton").prop("text", text).prop("requestUsers", requestUsers).prop("requestChat", requestChat).prop("requestContact", requestContact).prop("requestLocation", requestLocation).prop("requestPoll", requestPoll).prop("webApp", webApp).toString()
}

/**
 * This object defines the criteria used to request suitable users. Information about the selected users will be shared with the bot when the corresponding button is pressed. More about requesting users »
 * @param requestId Signed 32-bit identifier of the request that will be received back in the UsersShared object. Must be unique within the message
 * @param userIsBot Optional. Pass True to request bots, pass False to request regular users. If not specified, no additional restrictions are applied.
 * @param userIsPremium Optional. Pass True to request premium users, pass False to request non-premium users. If not specified, no additional restrictions are applied.
 * @param maxQuantity Optional. The maximum number of users to be selected; 1-10. Defaults to 1.
 * @param requestName Optional. Pass True to request the users' first and last names
 * @param requestUsername Optional. Pass True to request the users' usernames
 * @param requestPhoto Optional. Pass True to request the users' photos
 */
@Serializable
data class KeyboardButtonRequestUsers(
    val requestId: Long,
    val userIsBot: Boolean? = null,
    val userIsPremium: Boolean? = null,
    val maxQuantity: Long? = null,
    val requestName: Boolean? = null,
    val requestUsername: Boolean? = null,
    val requestPhoto: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButtonRequestUsers").prop("requestId", requestId).prop("userIsBot", userIsBot).prop("userIsPremium", userIsPremium).prop("maxQuantity", maxQuantity).prop("requestName", requestName).prop("requestUsername", requestUsername).prop("requestPhoto", requestPhoto).toString()
}

/**
 * This object defines the criteria used to request a suitable chat. Information about the selected chat will be shared with the bot when the corresponding button is pressed. The bot will be granted requested rights in the chat if appropriate. More about requesting chats ».
 * @param requestId Signed 32-bit identifier of the request, which will be received back in the ChatShared object. Must be unique within the message
 * @param chatIsChannel Pass True to request a channel chat, pass False to request a group or a supergroup chat.
 * @param chatIsForum Optional. Pass True to request a forum supergroup, pass False to request a non-forum chat. If not specified, no additional restrictions are applied.
 * @param chatHasUsername Optional. Pass True to request a supergroup or a channel with a username, pass False to request a chat without a username. If not specified, no additional restrictions are applied.
 * @param chatIsCreated Optional. Pass True to request a chat owned by the user. Otherwise, no additional restrictions are applied.
 * @param userAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the user in the chat. The rights must be a superset of bot_administrator_rights. If not specified, no additional restrictions are applied.
 * @param botAdministratorRights Optional. A JSON-serialized object listing the required administrator rights of the bot in the chat. The rights must be a subset of user_administrator_rights. If not specified, no additional restrictions are applied.
 * @param botIsMember Optional. Pass True to request a chat with the bot as a member. Otherwise, no additional restrictions are applied.
 * @param requestTitle Optional. Pass True to request the chat's title
 * @param requestUsername Optional. Pass True to request the chat's username
 * @param requestPhoto Optional. Pass True to request the chat's photo
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
    val requestTitle: Boolean? = null,
    val requestUsername: Boolean? = null,
    val requestPhoto: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("KeyboardButtonRequestChat").prop("requestId", requestId).prop("chatIsChannel", chatIsChannel).prop("chatIsForum", chatIsForum).prop("chatHasUsername", chatHasUsername).prop("chatIsCreated", chatIsCreated).prop("userAdministratorRights", userAdministratorRights).prop("botAdministratorRights", botAdministratorRights).prop("botIsMember", botIsMember).prop("requestTitle", requestTitle).prop("requestUsername", requestUsername).prop("requestPhoto", requestPhoto).toString()
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
 * Upon receiving a message with this object, Telegram clients will remove the current custom keyboard and display the default letter-keyboard. By default, custom keyboards are displayed until a new keyboard is sent by a bot. An exception is made for one-time keyboards that are hidden immediately after the user presses a button (see ReplyKeyboardMarkup). Not supported in channels and for messages sent on behalf of a Telegram Business account.
 * @param removeKeyboard Requests clients to remove the custom keyboard (user will not be able to summon this keyboard; if you want to hide the keyboard from sight but keep it accessible, use one_time_keyboard in ReplyKeyboardMarkup)
 * @param selective Optional. Use this parameter if you want to remove the keyboard for specific users only. Targets: 1) users that are @mentioned in the text of the Message object; 2) if the bot's message is a reply to a message in the same chat and forum topic, sender of the original message. Example: A user votes in a poll, bot returns confirmation message in reply to the vote and removes the keyboard for that user, while still showing the keyboard with poll options to users who haven't voted yet.
 */
@Serializable
data class ReplyKeyboardRemove(
    val removeKeyboard: Boolean,
    val selective: Boolean? = null,
) : ReplyMarkup {
    override fun toString() = DebugStringBuilder("ReplyKeyboardRemove").prop("removeKeyboard", removeKeyboard).prop("selective", selective).toString()
}

/**
 * This object represents an inline keyboard that appears right next to the message it belongs to.
 * @param inlineKeyboard Array of button rows, each represented by an Array of InlineKeyboardButton objects
 */
@Serializable
data class InlineKeyboardMarkup(
    val inlineKeyboard: List<List<InlineKeyboardButton>>,
) : ReplyMarkup {
    override fun toString() = DebugStringBuilder("InlineKeyboardMarkup").prop("inlineKeyboard", inlineKeyboard).toString()
}

/**
 * This object represents one button of an inline keyboard. Exactly one of the optional fields must be used to specify type of the button.
 * @param text Label text on the button
 * @param url Optional. HTTP or tg:// URL to be opened when the button is pressed. Links tg://user?id=<user_id> can be used to mention a user by their identifier without using a username, if this is allowed by their privacy settings.
 * @param callbackData Optional. Data to be sent in a callback query to the bot when the button is pressed, 1-64 bytes
 * @param webApp Optional. Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery. Available only in private chats between a user and the bot. Not supported for messages sent on behalf of a Telegram Business account.
 * @param loginUrl Optional. An HTTPS URL used to automatically authorize the user. Can be used as a replacement for the Telegram Login Widget.
 * @param switchInlineQuery Optional. If set, pressing the button will prompt the user to select one of their chats, open that chat and insert the bot's username and the specified inline query in the input field. May be empty, in which case just the bot's username will be inserted. Not supported for messages sent on behalf of a Telegram Business account.
 * @param switchInlineQueryCurrentChat Optional. If set, pressing the button will insert the bot's username and the specified inline query in the current chat's input field. May be empty, in which case only the bot's username will be inserted. This offers a quick way for the user to open your bot in inline mode in the same chat - good for selecting something from multiple options. Not supported in channels and for messages sent on behalf of a Telegram Business account.
 * @param switchInlineQueryChosenChat Optional. If set, pressing the button will prompt the user to select one of their chats of the specified type, open that chat and insert the bot's username and the specified inline query in the input field. Not supported for messages sent on behalf of a Telegram Business account.
 * @param callbackGame Optional. Description of the game that will be launched when the user presses the button. NOTE: This type of button must always be the first button in the first row.
 * @param pay Optional. Specify True, to send a Pay button. Substrings “” and “XTR” in the buttons's text will be replaced with a Telegram Star icon. NOTE: This type of button must always be the first button in the first row and can only be used in invoice messages.
 * @param copyText Optional. Description of the button that copies the specified text to the clipboard.
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
    val copyText: CopyTextButton? = null,
) {
    override fun toString() = DebugStringBuilder("InlineKeyboardButton").prop("text", text).prop("url", url).prop("callbackData", callbackData).prop("webApp", webApp).prop("loginUrl", loginUrl).prop("switchInlineQuery", switchInlineQuery).prop("switchInlineQueryCurrentChat", switchInlineQueryCurrentChat).prop("switchInlineQueryChosenChat", switchInlineQueryChosenChat).prop("callbackGame", callbackGame).prop("pay", pay).prop("copyText", copyText).toString()
}

/**
 * This object represents a parameter of the inline keyboard button used to automatically authorize a user. Serves as a great replacement for the Telegram Login Widget when the user is coming from Telegram. All the user needs to do is tap/click a button and confirm that they want to log in:
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
 * This object represents an inline keyboard button that copies specified text to the clipboard.
 * @param text The text to be copied to the clipboard; 1-256 characters
 */
@Serializable
data class CopyTextButton(
    val text: String,
) {
    override fun toString() = DebugStringBuilder("CopyTextButton").prop("text", text).toString()
}

/**
 * This object represents an incoming callback query from a callback button in an inline keyboard. If the button that originated the query was attached to a message sent by the bot, the field message will be present. If the button was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present. Exactly one of the fields data or game_short_name will be present.
 * @param id Unique identifier for this query
 * @param from Sender
 * @param chatInstance Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent. Useful for high scores in games.
 * @param message Optional. Message sent by the bot with the callback button that originated the query
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
 * Upon receiving a message with this object, Telegram clients will display a reply interface to the user (act as if the user has selected the bot's message and tapped 'Reply'). This can be extremely useful if you want to create user-friendly step-by-step interfaces without having to sacrifice privacy mode. Not supported in channels and for messages sent on behalf of a Telegram Business account.
 * @param forceReply Shows reply interface to the user, as if they manually selected the bot's message and tapped 'Reply'
 * @param inputFieldPlaceholder Optional. The placeholder to be shown in the input field when the reply is active; 1-64 characters
 * @param selective Optional. Use this parameter if you want to force reply from specific users only. Targets: 1) users that are @mentioned in the text of the Message object; 2) if the bot's message is a reply to a message in the same chat and forum topic, sender of the original message.
 */
@Serializable
data class ForceReply(
    val forceReply: Boolean,
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean? = null,
) : ReplyMarkup {
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
 * @param subscriptionPeriod Optional. The number of seconds the subscription will be active for before the next payment
 * @param subscriptionPrice Optional. The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a member of the chat using the link
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
    val subscriptionPeriod: Seconds? = null,
    val subscriptionPrice: Long? = null,
) {
    override fun toString() = DebugStringBuilder("ChatInviteLink").prop("inviteLink", inviteLink).prop("creator", creator).prop("createsJoinRequest", createsJoinRequest).prop("isPrimary", isPrimary).prop("isRevoked", isRevoked).prop("name", name).prop("expireDate", expireDate).prop("memberLimit", memberLimit).prop("pendingJoinRequestCount", pendingJoinRequestCount).prop("subscriptionPeriod", subscriptionPeriod).prop("subscriptionPrice", subscriptionPrice).toString()
}

/**
 * Represents the rights of an administrator in a chat.
 * @param isAnonymous True, if the user's presence in the chat is hidden
 * @param canManageChat True, if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
 * @param canDeleteMessages True, if the administrator can delete messages of other users
 * @param canManageVideoChats True, if the administrator can manage video chats
 * @param canRestrictMembers True, if the administrator can restrict, ban or unban chat members, or access supergroup statistics
 * @param canPromoteMembers True, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
 * @param canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @param canInviteUsers True, if the user is allowed to invite new users to the chat
 * @param canPostStories True, if the administrator can post stories to the chat
 * @param canEditStories True, if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
 * @param canDeleteStories True, if the administrator can delete stories posted by other users
 * @param canPostMessages Optional. True, if the administrator can post messages in the channel, or access channel statistics; for channels only
 * @param canEditMessages Optional. True, if the administrator can edit messages of other users and can pin messages; for channels only
 * @param canPinMessages Optional. True, if the user is allowed to pin messages; for groups and supergroups only
 * @param canManageTopics Optional. True, if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
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
    val canPostStories: Boolean,
    val canEditStories: Boolean,
    val canDeleteStories: Boolean,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ChatAdministratorRights").prop("isAnonymous", isAnonymous).prop("canManageChat", canManageChat).prop("canDeleteMessages", canDeleteMessages).prop("canManageVideoChats", canManageVideoChats).prop("canRestrictMembers", canRestrictMembers).prop("canPromoteMembers", canPromoteMembers).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPostStories", canPostStories).prop("canEditStories", canEditStories).prop("canDeleteStories", canDeleteStories).prop("canPostMessages", canPostMessages).prop("canEditMessages", canEditMessages).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).toString()
}

/**
 * This object represents changes in the status of a chat member.
 * @param chat Chat the user belongs to
 * @param from Performer of the action, which resulted in the change
 * @param date Date the change was done in Unix time
 * @param oldChatMember Previous information about the chat member
 * @param newChatMember New information about the chat member
 * @param inviteLink Optional. Chat invite link, which was used by the user to join the chat; for joining by invite link events only.
 * @param viaJoinRequest Optional. True, if the user joined the chat after sending a direct join request without using an invite link and being approved by an administrator
 * @param viaChatFolderInviteLink Optional. True, if the user joined the chat via a chat folder invite link
 */
@Serializable
data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: UnixTimestamp,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    val viaJoinRequest: Boolean? = null,
    val viaChatFolderInviteLink: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("ChatMemberUpdated").prop("chat", chat).prop("from", from).prop("date", date).prop("oldChatMember", oldChatMember).prop("newChatMember", newChatMember).prop("inviteLink", inviteLink).prop("viaJoinRequest", viaJoinRequest).prop("viaChatFolderInviteLink", viaChatFolderInviteLink).toString()
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
sealed interface ChatMember

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
) : ChatMember {
    override fun toString() = DebugStringBuilder("ChatMemberOwner").prop("user", user).prop("isAnonymous", isAnonymous).prop("customTitle", customTitle).toString()
}

/**
 * Represents a chat member that has some additional privileges.
 * @param user Information about the user
 * @param canBeEdited True, if the bot is allowed to edit administrator privileges of that user
 * @param isAnonymous True, if the user's presence in the chat is hidden
 * @param canManageChat True, if the administrator can access the chat event log, get boost list, see hidden supergroup and channel members, report spam messages and ignore slow mode. Implied by any other administrator privilege.
 * @param canDeleteMessages True, if the administrator can delete messages of other users
 * @param canManageVideoChats True, if the administrator can manage video chats
 * @param canRestrictMembers True, if the administrator can restrict, ban or unban chat members, or access supergroup statistics
 * @param canPromoteMembers True, if the administrator can add new administrators with a subset of their own privileges or demote administrators that they have promoted, directly or indirectly (promoted by administrators that were appointed by the user)
 * @param canChangeInfo True, if the user is allowed to change the chat title, photo and other settings
 * @param canInviteUsers True, if the user is allowed to invite new users to the chat
 * @param canPostStories True, if the administrator can post stories to the chat
 * @param canEditStories True, if the administrator can edit stories posted by other users, post stories to the chat page, pin chat stories, and access the chat's story archive
 * @param canDeleteStories True, if the administrator can delete stories posted by other users
 * @param canPostMessages Optional. True, if the administrator can post messages in the channel, or access channel statistics; for channels only
 * @param canEditMessages Optional. True, if the administrator can edit messages of other users and can pin messages; for channels only
 * @param canPinMessages Optional. True, if the user is allowed to pin messages; for groups and supergroups only
 * @param canManageTopics Optional. True, if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only
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
    val canPostStories: Boolean,
    val canEditStories: Boolean,
    val canDeleteStories: Boolean,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canManageTopics: Boolean? = null,
    val customTitle: String? = null,
) : ChatMember {
    override fun toString() = DebugStringBuilder("ChatMemberAdministrator").prop("user", user).prop("canBeEdited", canBeEdited).prop("isAnonymous", isAnonymous).prop("canManageChat", canManageChat).prop("canDeleteMessages", canDeleteMessages).prop("canManageVideoChats", canManageVideoChats).prop("canRestrictMembers", canRestrictMembers).prop("canPromoteMembers", canPromoteMembers).prop("canChangeInfo", canChangeInfo).prop("canInviteUsers", canInviteUsers).prop("canPostStories", canPostStories).prop("canEditStories", canEditStories).prop("canDeleteStories", canDeleteStories).prop("canPostMessages", canPostMessages).prop("canEditMessages", canEditMessages).prop("canPinMessages", canPinMessages).prop("canManageTopics", canManageTopics).prop("customTitle", customTitle).toString()
}

/**
 * Represents a chat member that has no additional privileges or restrictions.
 * @param user Information about the user
 * @param untilDate Optional. Date when the user's subscription will expire; Unix time
 */
@Serializable
@SerialName("member")
data class ChatMemberMember(
    val user: User,
    val untilDate: UnixTimestamp? = null,
) : ChatMember {
    override fun toString() = DebugStringBuilder("ChatMemberMember").prop("user", user).prop("untilDate", untilDate).toString()
}

/**
 * Represents a chat member that is under certain restrictions in the chat. Supergroups only.
 * @param user Information about the user
 * @param isMember True, if the user is a member of the chat at the moment of the request
 * @param canSendMessages True, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
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
 * @param untilDate Date when restrictions will be lifted for this user; Unix time. If 0, then the user is restricted forever
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
) : ChatMember {
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
) : ChatMember {
    override fun toString() = DebugStringBuilder("ChatMemberLeft").prop("user", user).toString()
}

/**
 * Represents a chat member that was banned in the chat and can't return to the chat or view chat messages.
 * @param user Information about the user
 * @param untilDate Date when restrictions will be lifted for this user; Unix time. If 0, then the user is banned forever
 */
@Serializable
@SerialName("kicked")
data class ChatMemberBanned(
    val user: User,
    val untilDate: UnixTimestamp,
) : ChatMember {
    override fun toString() = DebugStringBuilder("ChatMemberBanned").prop("user", user).prop("untilDate", untilDate).toString()
}

/**
 * Represents a join request sent to a chat.
 * @param chat Chat to which the request was sent
 * @param from User that sent the join request
 * @param userChatId Identifier of a private chat with the user who sent the join request. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier. The bot can use this identifier for 5 minutes to send messages until the join request is processed, assuming no other administrator contacted the user.
 * @param date Date the request was sent in Unix time
 * @param bio Optional. Bio of the user.
 * @param inviteLink Optional. Chat invite link that was used by the user to send the join request
 */
@Serializable
data class ChatJoinRequest(
    val chat: Chat,
    val from: User,
    val userChatId: ChatId,
    val date: UnixTimestamp,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
) {
    override fun toString() = DebugStringBuilder("ChatJoinRequest").prop("chat", chat).prop("from", from).prop("userChatId", userChatId).prop("date", date).prop("bio", bio).prop("inviteLink", inviteLink).toString()
}

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 * @param canSendMessages Optional. True, if the user is allowed to send text messages, contacts, giveaways, giveaway winners, invoices, locations and venues
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
 * Describes the birthdate of a user.
 * @param day Day of the user's birth; 1-31
 * @param month Month of the user's birth; 1-12
 * @param year Optional. Year of the user's birth
 */
@Serializable
data class Birthdate(
    val day: Long,
    val month: Long,
    val year: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Birthdate").prop("day", day).prop("month", month).prop("year", year).toString()
}

/**
 * Contains information about the start page settings of a Telegram Business account.
 * @param title Optional. Title text of the business intro
 * @param message Optional. Message text of the business intro
 * @param sticker Optional. Sticker of the business intro
 */
@Serializable
data class BusinessIntro(
    val title: String? = null,
    val message: String? = null,
    val sticker: Sticker? = null,
) {
    override fun toString() = DebugStringBuilder("BusinessIntro").prop("title", title).prop("message", message).prop("sticker", sticker).toString()
}

/**
 * Contains information about the location of a Telegram Business account.
 * @param address Address of the business
 * @param location Optional. Location of the business
 */
@Serializable
data class BusinessLocation(
    val address: String,
    val location: Location? = null,
) {
    override fun toString() = DebugStringBuilder("BusinessLocation").prop("address", address).prop("location", location).toString()
}

/**
 * Describes an interval of time during which a business is open.
 * @param openingMinute The minute's sequence number in a week, starting on Monday, marking the start of the time interval during which the business is open; 0 - 7 * 24 * 60
 * @param closingMinute The minute's sequence number in a week, starting on Monday, marking the end of the time interval during which the business is open; 0 - 8 * 24 * 60
 */
@Serializable
data class BusinessOpeningHoursInterval(
    val openingMinute: Long,
    val closingMinute: Long,
) {
    override fun toString() = DebugStringBuilder("BusinessOpeningHoursInterval").prop("openingMinute", openingMinute).prop("closingMinute", closingMinute).toString()
}

/**
 * Describes the opening hours of a business.
 * @param timeZoneName Unique name of the time zone for which the opening hours are defined
 * @param openingHours List of time intervals describing business opening hours
 */
@Serializable
data class BusinessOpeningHours(
    val timeZoneName: String,
    val openingHours: List<BusinessOpeningHoursInterval>,
) {
    override fun toString() = DebugStringBuilder("BusinessOpeningHours").prop("timeZoneName", timeZoneName).prop("openingHours", openingHours).toString()
}

/**
 * Describes the position of a clickable area within a story.
 * @param xPercentage The abscissa of the area's center, as a percentage of the media width
 * @param yPercentage The ordinate of the area's center, as a percentage of the media height
 * @param widthPercentage The width of the area's rectangle, as a percentage of the media width
 * @param heightPercentage The height of the area's rectangle, as a percentage of the media height
 * @param rotationAngle The clockwise rotation angle of the rectangle, in degrees; 0-360
 * @param cornerRadiusPercentage The radius of the rectangle corner rounding, as a percentage of the media width
 */
@Serializable
data class StoryAreaPosition(
    val xPercentage: Double,
    val yPercentage: Double,
    val widthPercentage: Double,
    val heightPercentage: Double,
    val rotationAngle: Double,
    val cornerRadiusPercentage: Double,
) {
    override fun toString() = DebugStringBuilder("StoryAreaPosition").prop("xPercentage", xPercentage).prop("yPercentage", yPercentage).prop("widthPercentage", widthPercentage).prop("heightPercentage", heightPercentage).prop("rotationAngle", rotationAngle).prop("cornerRadiusPercentage", cornerRadiusPercentage).toString()
}

/**
 * Describes the physical address of a location.
 * @param countryCode The two-letter ISO 3166-1 alpha-2 country code of the country where the location is located
 * @param state Optional. State of the location
 * @param city Optional. City of the location
 * @param street Optional. Street address of the location
 */
@Serializable
data class LocationAddress(
    val countryCode: String,
    val state: String? = null,
    val city: String? = null,
    val street: String? = null,
) {
    override fun toString() = DebugStringBuilder("LocationAddress").prop("countryCode", countryCode).prop("state", state).prop("city", city).prop("street", street).toString()
}

/**
 * Describes the type of a clickable area on a story. Currently, it can be one of
 * - [StoryAreaTypeLocation]
 * - [StoryAreaTypeSuggestedReaction]
 * - [StoryAreaTypeLink]
 * - [StoryAreaTypeWeather]
 * - [StoryAreaTypeUniqueGift]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface StoryAreaType

/**
 * Describes a story area pointing to a location. Currently, a story can have up to 10 location areas.
 * @param latitude Location latitude in degrees
 * @param longitude Location longitude in degrees
 * @param address Optional. Address of the location
 */
@Serializable
@SerialName("location")
data class StoryAreaTypeLocation(
    val latitude: Double,
    val longitude: Double,
    val address: LocationAddress? = null,
) : StoryAreaType {
    override fun toString() = DebugStringBuilder("StoryAreaTypeLocation").prop("latitude", latitude).prop("longitude", longitude).prop("address", address).toString()
}

/**
 * Describes a story area pointing to a suggested reaction. Currently, a story can have up to 5 suggested reaction areas.
 * @param reactionType Type of the reaction
 * @param isDark Optional. Pass True if the reaction area has a dark background
 * @param isFlipped Optional. Pass True if reaction area corner is flipped
 */
@Serializable
@SerialName("suggested_reaction")
data class StoryAreaTypeSuggestedReaction(
    val reactionType: ReactionType,
    val isDark: Boolean? = null,
    val isFlipped: Boolean? = null,
) : StoryAreaType {
    override fun toString() = DebugStringBuilder("StoryAreaTypeSuggestedReaction").prop("reactionType", reactionType).prop("isDark", isDark).prop("isFlipped", isFlipped).toString()
}

/**
 * Describes a story area pointing to an HTTP or tg:// link. Currently, a story can have up to 3 link areas.
 * @param url HTTP or tg:// URL to be opened when the area is clicked
 */
@Serializable
@SerialName("link")
data class StoryAreaTypeLink(
    val url: String,
) : StoryAreaType {
    override fun toString() = DebugStringBuilder("StoryAreaTypeLink").prop("url", url).toString()
}

/**
 * Describes a story area containing weather information. Currently, a story can have up to 3 weather areas.
 * @param temperature Temperature, in degree Celsius
 * @param emoji Emoji representing the weather
 * @param backgroundColor A color of the area background in the ARGB format
 */
@Serializable
@SerialName("weather")
data class StoryAreaTypeWeather(
    val temperature: Double,
    val emoji: String,
    val backgroundColor: Long,
) : StoryAreaType {
    override fun toString() = DebugStringBuilder("StoryAreaTypeWeather").prop("temperature", temperature).prop("emoji", emoji).prop("backgroundColor", backgroundColor).toString()
}

/**
 * Describes a story area pointing to a unique gift. Currently, a story can have at most 1 unique gift area.
 * @param name Unique name of the gift
 */
@Serializable
@SerialName("unique_gift")
data class StoryAreaTypeUniqueGift(
    val name: String,
) : StoryAreaType {
    override fun toString() = DebugStringBuilder("StoryAreaTypeUniqueGift").prop("name", name).toString()
}

/**
 * Describes a clickable area on a story media.
 * @param position Position of the area
 * @param type Type of the area
 */
@Serializable
data class StoryArea(
    val position: StoryAreaPosition,
    val type: StoryAreaType,
) {
    override fun toString() = DebugStringBuilder("StoryArea").prop("position", position).prop("type", type).toString()
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
 * This object describes the type of a reaction. Currently, it can be one of
 * - [ReactionTypeEmoji]
 * - [ReactionTypeCustomEmoji]
 * - [ReactionTypePaid]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface ReactionType

/**
 * The reaction is based on an emoji.
 * @param emoji Reaction emoji. Currently, it can be one of "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
 */
@Serializable
@SerialName("emoji")
data class ReactionTypeEmoji(
    val emoji: String,
) : ReactionType {
    override fun toString() = DebugStringBuilder("ReactionTypeEmoji").prop("emoji", emoji).toString()
}

/**
 * The reaction is based on a custom emoji.
 * @param customEmojiId Custom emoji identifier
 */
@Serializable
@SerialName("custom_emoji")
data class ReactionTypeCustomEmoji(
    val customEmojiId: CustomEmojiId,
) : ReactionType {
    override fun toString() = DebugStringBuilder("ReactionTypeCustomEmoji").prop("customEmojiId", customEmojiId).toString()
}

/**
 * The reaction is paid.
 */
@Serializable
@SerialName("paid")
data object ReactionTypePaid : ReactionType

/**
 * Represents a reaction added to a message along with the number of times it was added.
 * @param type Type of the reaction
 * @param totalCount Number of times the reaction was added
 */
@Serializable
data class ReactionCount(
    val type: ReactionType,
    val totalCount: Long,
) {
    override fun toString() = DebugStringBuilder("ReactionCount").prop("type", type).prop("totalCount", totalCount).toString()
}

/**
 * This object represents a change of a reaction on a message performed by a user.
 * @param chat The chat containing the message the user reacted to
 * @param messageId Unique identifier of the message inside the chat
 * @param date Date of the change in Unix time
 * @param oldReaction Previous list of reaction types that were set by the user
 * @param newReaction New list of reaction types that have been set by the user
 * @param user Optional. The user that changed the reaction, if the user isn't anonymous
 * @param actorChat Optional. The chat on behalf of which the reaction was changed, if the user is anonymous
 */
@Serializable
data class MessageReactionUpdated(
    val chat: Chat,
    val messageId: MessageId,
    val date: UnixTimestamp,
    val oldReaction: List<ReactionType>,
    val newReaction: List<ReactionType>,
    val user: User? = null,
    val actorChat: Chat? = null,
) {
    override fun toString() = DebugStringBuilder("MessageReactionUpdated").prop("chat", chat).prop("messageId", messageId).prop("date", date).prop("oldReaction", oldReaction).prop("newReaction", newReaction).prop("user", user).prop("actorChat", actorChat).toString()
}

/**
 * This object represents reaction changes on a message with anonymous reactions.
 * @param chat The chat containing the message
 * @param messageId Unique message identifier inside the chat
 * @param date Date of the change in Unix time
 * @param reactions List of reactions that are present on the message
 */
@Serializable
data class MessageReactionCountUpdated(
    val chat: Chat,
    val messageId: MessageId,
    val date: UnixTimestamp,
    val reactions: List<ReactionCount>,
) {
    override fun toString() = DebugStringBuilder("MessageReactionCountUpdated").prop("chat", chat).prop("messageId", messageId).prop("date", date).prop("reactions", reactions).toString()
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
 * This object represents a gift that can be sent by the bot.
 * @param id Unique identifier of the gift
 * @param sticker The sticker that represents the gift
 * @param starCount The number of Telegram Stars that must be paid to send the sticker
 * @param upgradeStarCount Optional. The number of Telegram Stars that must be paid to upgrade the gift to a unique one
 * @param totalCount Optional. The total number of the gifts of this type that can be sent; for limited gifts only
 * @param remainingCount Optional. The number of remaining gifts of this type that can be sent; for limited gifts only
 */
@Serializable
data class Gift(
    val id: String,
    val sticker: Sticker,
    val starCount: Long,
    val upgradeStarCount: Long? = null,
    val totalCount: Long? = null,
    val remainingCount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("Gift").prop("id", id).prop("sticker", sticker).prop("starCount", starCount).prop("upgradeStarCount", upgradeStarCount).prop("totalCount", totalCount).prop("remainingCount", remainingCount).toString()
}

/**
 * This object represent a list of gifts.
 * @param gifts The list of gifts
 */
@Serializable
data class Gifts(
    val gifts: List<Gift>,
) {
    override fun toString() = DebugStringBuilder("Gifts").prop("gifts", gifts).toString()
}

/**
 * This object describes the model of a unique gift.
 * @param name Name of the model
 * @param sticker The sticker that represents the unique gift
 * @param rarityPerMille The number of unique gifts that receive this model for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftModel(
    val name: String,
    val sticker: Sticker,
    val rarityPerMille: Long,
) {
    override fun toString() = DebugStringBuilder("UniqueGiftModel").prop("name", name).prop("sticker", sticker).prop("rarityPerMille", rarityPerMille).toString()
}

/**
 * This object describes the symbol shown on the pattern of a unique gift.
 * @param name Name of the symbol
 * @param sticker The sticker that represents the unique gift
 * @param rarityPerMille The number of unique gifts that receive this model for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftSymbol(
    val name: String,
    val sticker: Sticker,
    val rarityPerMille: Long,
) {
    override fun toString() = DebugStringBuilder("UniqueGiftSymbol").prop("name", name).prop("sticker", sticker).prop("rarityPerMille", rarityPerMille).toString()
}

/**
 * This object describes the colors of the backdrop of a unique gift.
 * @param centerColor The color in the center of the backdrop in RGB format
 * @param edgeColor The color on the edges of the backdrop in RGB format
 * @param symbolColor The color to be applied to the symbol in RGB format
 * @param textColor The color for the text on the backdrop in RGB format
 */
@Serializable
data class UniqueGiftBackdropColors(
    val centerColor: Long,
    val edgeColor: Long,
    val symbolColor: Long,
    val textColor: Long,
) {
    override fun toString() = DebugStringBuilder("UniqueGiftBackdropColors").prop("centerColor", centerColor).prop("edgeColor", edgeColor).prop("symbolColor", symbolColor).prop("textColor", textColor).toString()
}

/**
 * This object describes the backdrop of a unique gift.
 * @param name Name of the backdrop
 * @param colors Colors of the backdrop
 * @param rarityPerMille The number of unique gifts that receive this backdrop for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftBackdrop(
    val name: String,
    val colors: UniqueGiftBackdropColors,
    val rarityPerMille: Long,
) {
    override fun toString() = DebugStringBuilder("UniqueGiftBackdrop").prop("name", name).prop("colors", colors).prop("rarityPerMille", rarityPerMille).toString()
}

/**
 * This object describes a unique gift that was upgraded from a regular gift.
 * @param baseName Human-readable name of the regular gift from which this unique gift was upgraded
 * @param name Unique name of the gift. This name can be used in https://t.me/nft/... links and story areas
 * @param number Unique number of the upgraded gift among gifts upgraded from the same regular gift
 * @param model Model of the gift
 * @param symbol Symbol of the gift
 * @param backdrop Backdrop of the gift
 */
@Serializable
data class UniqueGift(
    val baseName: String,
    val name: String,
    val number: Long,
    val model: UniqueGiftModel,
    val symbol: UniqueGiftSymbol,
    val backdrop: UniqueGiftBackdrop,
) {
    override fun toString() = DebugStringBuilder("UniqueGift").prop("baseName", baseName).prop("name", name).prop("number", number).prop("model", model).prop("symbol", symbol).prop("backdrop", backdrop).toString()
}

/**
 * Describes a service message about a regular gift that was sent or received.
 * @param gift Information about the gift
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @param convertStarCount Optional. Number of Telegram Stars that can be claimed by the receiver by converting the gift; omitted if conversion to Telegram Stars is impossible
 * @param prepaidUpgradeStarCount Optional. Number of Telegram Stars that were prepaid by the sender for the ability to upgrade the gift
 * @param canBeUpgraded Optional. True, if the gift can be upgraded to a unique gift
 * @param text Optional. Text of the message that was added to the gift
 * @param entities Optional. Special entities that appear in the text
 * @param isPrivate Optional. True, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
 */
@Serializable
data class GiftInfo(
    val gift: Gift,
    val ownedGiftId: String? = null,
    val convertStarCount: Long? = null,
    val prepaidUpgradeStarCount: Long? = null,
    val canBeUpgraded: Boolean? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val isPrivate: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("GiftInfo").prop("gift", gift).prop("ownedGiftId", ownedGiftId).prop("convertStarCount", convertStarCount).prop("prepaidUpgradeStarCount", prepaidUpgradeStarCount).prop("canBeUpgraded", canBeUpgraded).prop("text", text).prop("entities", entities).prop("isPrivate", isPrivate).toString()
}

/**
 * Describes a service message about a unique gift that was sent or received.
 * @param gift Information about the gift
 * @param origin Origin of the gift. Currently, either “upgrade” or “transfer”
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @param transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 */
@Serializable
data class UniqueGiftInfo(
    val gift: UniqueGift,
    val origin: String,
    val ownedGiftId: String? = null,
    val transferStarCount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("UniqueGiftInfo").prop("gift", gift).prop("origin", origin).prop("ownedGiftId", ownedGiftId).prop("transferStarCount", transferStarCount).toString()
}

/**
 * This object describes a gift received and owned by a user or a chat. Currently, it can be one of
 * - [OwnedGiftRegular]
 * - [OwnedGiftUnique]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface OwnedGift

/**
 * Describes a regular gift owned by a user or a chat.
 * @param gift Information about the regular gift
 * @param sendDate Date the gift was sent in Unix time
 * @param ownedGiftId Optional. Unique identifier of the gift for the bot; for gifts received on behalf of business accounts only
 * @param senderUser Optional. Sender of the gift if it is a known user
 * @param text Optional. Text of the message that was added to the gift
 * @param entities Optional. Special entities that appear in the text
 * @param isPrivate Optional. True, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
 * @param isSaved Optional. True, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
 * @param canBeUpgraded Optional. True, if the gift can be upgraded to a unique gift; for gifts received on behalf of business accounts only
 * @param wasRefunded Optional. True, if the gift was refunded and isn't available anymore
 * @param convertStarCount Optional. Number of Telegram Stars that can be claimed by the receiver instead of the gift; omitted if the gift cannot be converted to Telegram Stars
 * @param prepaidUpgradeStarCount Optional. Number of Telegram Stars that were paid by the sender for the ability to upgrade the gift
 */
@Serializable
@SerialName("regular")
data class OwnedGiftRegular(
    val gift: Gift,
    val sendDate: Long,
    val ownedGiftId: String? = null,
    val senderUser: User? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val isPrivate: Boolean? = null,
    val isSaved: Boolean? = null,
    val canBeUpgraded: Boolean? = null,
    val wasRefunded: Boolean? = null,
    val convertStarCount: Long? = null,
    val prepaidUpgradeStarCount: Long? = null,
) : OwnedGift {
    override fun toString() = DebugStringBuilder("OwnedGiftRegular").prop("gift", gift).prop("sendDate", sendDate).prop("ownedGiftId", ownedGiftId).prop("senderUser", senderUser).prop("text", text).prop("entities", entities).prop("isPrivate", isPrivate).prop("isSaved", isSaved).prop("canBeUpgraded", canBeUpgraded).prop("wasRefunded", wasRefunded).prop("convertStarCount", convertStarCount).prop("prepaidUpgradeStarCount", prepaidUpgradeStarCount).toString()
}

/**
 * Describes a unique gift received and owned by a user or a chat.
 * @param gift Information about the unique gift
 * @param sendDate Date the gift was sent in Unix time
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; for gifts received on behalf of business accounts only
 * @param senderUser Optional. Sender of the gift if it is a known user
 * @param isSaved Optional. True, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
 * @param canBeTransferred Optional. True, if the gift can be transferred to another owner; for gifts received on behalf of business accounts only
 * @param transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 */
@Serializable
@SerialName("unique")
data class OwnedGiftUnique(
    val gift: UniqueGift,
    val sendDate: Long,
    val ownedGiftId: String? = null,
    val senderUser: User? = null,
    val isSaved: Boolean? = null,
    val canBeTransferred: Boolean? = null,
    val transferStarCount: Long? = null,
) : OwnedGift {
    override fun toString() = DebugStringBuilder("OwnedGiftUnique").prop("gift", gift).prop("sendDate", sendDate).prop("ownedGiftId", ownedGiftId).prop("senderUser", senderUser).prop("isSaved", isSaved).prop("canBeTransferred", canBeTransferred).prop("transferStarCount", transferStarCount).toString()
}

/**
 * Contains the list of gifts received and owned by a user or a chat.
 * @param totalCount The total number of gifts owned by the user or the chat
 * @param gifts The list of gifts
 * @param nextOffset Optional. Offset for the next request. If empty, then there are no more results
 */
@Serializable
data class OwnedGifts(
    val totalCount: Long,
    val gifts: List<OwnedGift>,
    val nextOffset: String? = null,
) {
    override fun toString() = DebugStringBuilder("OwnedGifts").prop("totalCount", totalCount).prop("gifts", gifts).prop("nextOffset", nextOffset).toString()
}

/**
 * This object describes the types of gifts that can be gifted to a user or a chat.
 * @param unlimitedGifts True, if unlimited regular gifts are accepted
 * @param limitedGifts True, if limited regular gifts are accepted
 * @param uniqueGifts True, if unique gifts or gifts that can be upgraded to unique for free are accepted
 * @param premiumSubscription True, if a Telegram Premium subscription is accepted
 */
@Serializable
data class AcceptedGiftTypes(
    val unlimitedGifts: Boolean,
    val limitedGifts: Boolean,
    val uniqueGifts: Boolean,
    val premiumSubscription: Boolean,
) {
    override fun toString() = DebugStringBuilder("AcceptedGiftTypes").prop("unlimitedGifts", unlimitedGifts).prop("limitedGifts", limitedGifts).prop("uniqueGifts", uniqueGifts).prop("premiumSubscription", premiumSubscription).toString()
}

/**
 * Describes an amount of Telegram Stars.
 * @param amount Integer amount of Telegram Stars, rounded to 0; can be negative
 * @param nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars; from -999999999 to 999999999; can be negative if and only if amount is non-positive
 */
@Serializable
data class StarAmount(
    val amount: Long,
    val nanostarAmount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("StarAmount").prop("amount", amount).prop("nanostarAmount", nanostarAmount).toString()
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
sealed interface BotCommandScope

/**
 * Represents the default scope of bot commands. Default commands are used if no commands with a narrower scope are specified for the user.
 */
@Serializable
@SerialName("default")
data object BotCommandScopeDefault : BotCommandScope

/**
 * Represents the scope of bot commands, covering all private chats.
 */
@Serializable
@SerialName("all_private_chats")
data object BotCommandScopeAllPrivateChats : BotCommandScope

/**
 * Represents the scope of bot commands, covering all group and supergroup chats.
 */
@Serializable
@SerialName("all_group_chats")
data object BotCommandScopeAllGroupChats : BotCommandScope

/**
 * Represents the scope of bot commands, covering all group and supergroup chat administrators.
 */
@Serializable
@SerialName("all_chat_administrators")
data object BotCommandScopeAllChatAdministrators : BotCommandScope

/**
 * Represents the scope of bot commands, covering a specific chat.
 * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
 */
@Serializable
@SerialName("chat")
data class BotCommandScopeChat(
    val chatId: ChatId,
) : BotCommandScope {
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
) : BotCommandScope {
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
) : BotCommandScope {
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
sealed interface MenuButton

/**
 * Represents a menu button, which opens the bot's list of commands.
 */
@Serializable
@SerialName("commands")
data object MenuButtonCommands : MenuButton

/**
 * Represents a menu button, which launches a Web App.
 * @param text Text on the button
 * @param webApp Description of the Web App that will be launched when the user presses the button. The Web App will be able to send an arbitrary message on behalf of the user using the method answerWebAppQuery. Alternatively, a t.me link to a Web App of the bot can be specified in the object instead of the Web App's URL, in which case the Web App will be opened as if the user pressed the link.
 */
@Serializable
@SerialName("web_app")
data class MenuButtonWebApp(
    val text: String,
    val webApp: WebAppInfo,
) : MenuButton {
    override fun toString() = DebugStringBuilder("MenuButtonWebApp").prop("text", text).prop("webApp", webApp).toString()
}

/**
 * Describes that no specific value for the menu button was set.
 */
@Serializable
@SerialName("default")
data object MenuButtonDefault : MenuButton

/**
 * This object describes the source of a chat boost. It can be one of
 * - [ChatBoostSourcePremium]
 * - [ChatBoostSourceGiftCode]
 * - [ChatBoostSourceGiveaway]
 */
@Serializable
@JsonClassDiscriminator("source")
sealed interface ChatBoostSource

/**
 * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to another user.
 * @param user User that boosted the chat
 */
@Serializable
@SerialName("premium")
data class ChatBoostSourcePremium(
    val user: User,
) : ChatBoostSource {
    override fun toString() = DebugStringBuilder("ChatBoostSourcePremium").prop("user", user).toString()
}

/**
 * The boost was obtained by the creation of Telegram Premium gift codes to boost a chat. Each such code boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
 * @param user User for which the gift code was created
 */
@Serializable
@SerialName("gift_code")
data class ChatBoostSourceGiftCode(
    val user: User,
) : ChatBoostSource {
    override fun toString() = DebugStringBuilder("ChatBoostSourceGiftCode").prop("user", user).toString()
}

/**
 * The boost was obtained by the creation of a Telegram Premium or a Telegram Star giveaway. This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription for Telegram Premium giveaways and prize_star_count / 500 times for one year for Telegram Star giveaways.
 * @param giveawayMessageId Identifier of a message in the chat with the giveaway; the message could have been deleted already. May be 0 if the message isn't sent yet.
 * @param user Optional. User that won the prize in the giveaway if any; for Telegram Premium giveaways only
 * @param isUnclaimed Optional. True, if the giveaway was completed, but there was no user to win the prize
 * @param prizeStarCount Optional. The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only
 */
@Serializable
@SerialName("giveaway")
data class ChatBoostSourceGiveaway(
    val giveawayMessageId: MessageId,
    val user: User? = null,
    val isUnclaimed: Boolean? = null,
    val prizeStarCount: Long? = null,
) : ChatBoostSource {
    override fun toString() = DebugStringBuilder("ChatBoostSourceGiveaway").prop("giveawayMessageId", giveawayMessageId).prop("user", user).prop("isUnclaimed", isUnclaimed).prop("prizeStarCount", prizeStarCount).toString()
}

/**
 * This object contains information about a chat boost.
 * @param boostId Unique identifier of the boost
 * @param addDate Point in time (Unix timestamp) when the chat was boosted
 * @param expirationDate Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged
 * @param source Source of the added boost
 */
@Serializable
data class ChatBoost(
    val boostId: String,
    val addDate: Long,
    val expirationDate: Long,
    val source: ChatBoostSource,
) {
    override fun toString() = DebugStringBuilder("ChatBoost").prop("boostId", boostId).prop("addDate", addDate).prop("expirationDate", expirationDate).prop("source", source).toString()
}

/**
 * This object represents a boost added to a chat or changed.
 * @param chat Chat which was boosted
 * @param boost Information about the chat boost
 */
@Serializable
data class ChatBoostUpdated(
    val chat: Chat,
    val boost: ChatBoost,
) {
    override fun toString() = DebugStringBuilder("ChatBoostUpdated").prop("chat", chat).prop("boost", boost).toString()
}

/**
 * This object represents a boost removed from a chat.
 * @param chat Chat which was boosted
 * @param boostId Unique identifier of the boost
 * @param removeDate Point in time (Unix timestamp) when the boost was removed
 * @param source Source of the removed boost
 */
@Serializable
data class ChatBoostRemoved(
    val chat: Chat,
    val boostId: String,
    val removeDate: Long,
    val source: ChatBoostSource,
) {
    override fun toString() = DebugStringBuilder("ChatBoostRemoved").prop("chat", chat).prop("boostId", boostId).prop("removeDate", removeDate).prop("source", source).toString()
}

/**
 * This object represents a list of boosts added to a chat by a user.
 * @param boosts The list of boosts added to the chat by the user
 */
@Serializable
data class UserChatBoosts(
    val boosts: List<ChatBoost>,
) {
    override fun toString() = DebugStringBuilder("UserChatBoosts").prop("boosts", boosts).toString()
}

/**
 * Represents the rights of a business bot.
 * @param canReply Optional. True, if the bot can send and edit messages in the private chats that had incoming messages in the last 24 hours
 * @param canReadMessages Optional. True, if the bot can mark incoming private messages as read
 * @param canDeleteSentMessages Optional. True, if the bot can delete messages sent by the bot
 * @param canDeleteAllMessages Optional. True, if the bot can delete all private messages in managed chats
 * @param canEditName Optional. True, if the bot can edit the first and last name of the business account
 * @param canEditBio Optional. True, if the bot can edit the bio of the business account
 * @param canEditProfilePhoto Optional. True, if the bot can edit the profile photo of the business account
 * @param canEditUsername Optional. True, if the bot can edit the username of the business account
 * @param canChangeGiftSettings Optional. True, if the bot can change the privacy settings pertaining to gifts for the business account
 * @param canViewGiftsAndStars Optional. True, if the bot can view gifts and the amount of Telegram Stars owned by the business account
 * @param canConvertGiftsToStars Optional. True, if the bot can convert regular gifts owned by the business account to Telegram Stars
 * @param canTransferAndUpgradeGifts Optional. True, if the bot can transfer and upgrade gifts owned by the business account
 * @param canTransferStars Optional. True, if the bot can transfer Telegram Stars received by the business account to its own account, or use them to upgrade and transfer gifts
 * @param canManageStories Optional. True, if the bot can post, edit and delete stories on behalf of the business account
 */
@Serializable
data class BusinessBotRights(
    val canReply: Boolean? = null,
    val canReadMessages: Boolean? = null,
    val canDeleteSentMessages: Boolean? = null,
    val canDeleteAllMessages: Boolean? = null,
    val canEditName: Boolean? = null,
    val canEditBio: Boolean? = null,
    val canEditProfilePhoto: Boolean? = null,
    val canEditUsername: Boolean? = null,
    val canChangeGiftSettings: Boolean? = null,
    val canViewGiftsAndStars: Boolean? = null,
    val canConvertGiftsToStars: Boolean? = null,
    val canTransferAndUpgradeGifts: Boolean? = null,
    val canTransferStars: Boolean? = null,
    val canManageStories: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("BusinessBotRights").prop("canReply", canReply).prop("canReadMessages", canReadMessages).prop("canDeleteSentMessages", canDeleteSentMessages).prop("canDeleteAllMessages", canDeleteAllMessages).prop("canEditName", canEditName).prop("canEditBio", canEditBio).prop("canEditProfilePhoto", canEditProfilePhoto).prop("canEditUsername", canEditUsername).prop("canChangeGiftSettings", canChangeGiftSettings).prop("canViewGiftsAndStars", canViewGiftsAndStars).prop("canConvertGiftsToStars", canConvertGiftsToStars).prop("canTransferAndUpgradeGifts", canTransferAndUpgradeGifts).prop("canTransferStars", canTransferStars).prop("canManageStories", canManageStories).toString()
}

/**
 * Describes the connection of the bot with a business account.
 * @param id Unique identifier of the business connection
 * @param user Business account user that created the business connection
 * @param userChatId Identifier of a private chat with the user who created the business connection. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param date Date the connection was established in Unix time
 * @param isEnabled True, if the connection is active
 * @param rights Optional. Rights of the business bot
 */
@Serializable
data class BusinessConnection(
    val id: BusinessConnectionId,
    val user: User,
    val userChatId: ChatId,
    val date: UnixTimestamp,
    val isEnabled: Boolean,
    val rights: BusinessBotRights? = null,
) {
    override fun toString() = DebugStringBuilder("BusinessConnection").prop("id", id).prop("user", user).prop("userChatId", userChatId).prop("date", date).prop("isEnabled", isEnabled).prop("rights", rights).toString()
}

/**
 * This object is received when messages are deleted from a connected business account.
 * @param businessConnectionId Unique identifier of the business connection
 * @param chat Information about a chat in the business account. The bot may not have access to the chat or the corresponding user.
 * @param messageIds The list of identifiers of deleted messages in the chat of the business account
 */
@Serializable
data class BusinessMessagesDeleted(
    val businessConnectionId: BusinessConnectionId,
    val chat: Chat,
    val messageIds: List<Long>,
) {
    override fun toString() = DebugStringBuilder("BusinessMessagesDeleted").prop("businessConnectionId", businessConnectionId).prop("chat", chat).prop("messageIds", messageIds).toString()
}

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
sealed interface InputMedia

/**
 * Represents a photo to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param caption Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
 * @param hasSpoiler Optional. Pass True if the photo needs to be covered with a spoiler animation
 */
@Serializable
@SerialName("photo")
data class InputMediaPhoto(
    val media: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val showCaptionAboveMedia: Boolean? = null,
    val hasSpoiler: Boolean? = null,
) : InputMedia {
    override fun toString() = DebugStringBuilder("InputMediaPhoto").prop("media", media).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("hasSpoiler", hasSpoiler).toString()
}

/**
 * Represents a video to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Optional. Caption of the video to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the video caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
 * @param width Optional. Video width
 * @param height Optional. Video height
 * @param duration Optional. Video duration in seconds
 * @param supportsStreaming Optional. Pass True if the uploaded video is suitable for streaming
 * @param hasSpoiler Optional. Pass True if the video needs to be covered with a spoiler animation
 * @param cover Optional. Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param startTimestamp Optional. Start timestamp for the video in the message
 */
@Serializable
@SerialName("video")
data class InputMediaVideo(
    val media: String,
    val thumbnail: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val showCaptionAboveMedia: Boolean? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Seconds? = null,
    val supportsStreaming: Boolean? = null,
    val hasSpoiler: Boolean? = null,
    val cover: String? = null,
    val startTimestamp: Long? = null,
) : InputMedia {
    override fun toString() = DebugStringBuilder("InputMediaVideo").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("width", width).prop("height", height).prop("duration", duration).prop("supportsStreaming", supportsStreaming).prop("hasSpoiler", hasSpoiler).prop("cover", cover).prop("startTimestamp", startTimestamp).toString()
}

/**
 * Represents an animation file (GIF or H.264/MPEG-4 AVC video without sound) to be sent.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param caption Optional. Caption of the animation to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the animation caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Seconds? = null,
    val hasSpoiler: Boolean? = null,
) : InputMedia {
    override fun toString() = DebugStringBuilder("InputMediaAnimation").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("width", width).prop("height", height).prop("duration", duration).prop("hasSpoiler", hasSpoiler).toString()
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
) : InputMedia {
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
) : InputMedia {
    override fun toString() = DebugStringBuilder("InputMediaDocument").prop("media", media).prop("thumbnail", thumbnail).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("disableContentTypeDetection", disableContentTypeDetection).toString()
}

/**
 * This object describes the paid media to be sent. Currently, it can be one of
 * - [InputPaidMediaPhoto]
 * - [InputPaidMediaVideo]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface InputPaidMedia

/**
 * The paid media to send is a photo.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 */
@Serializable
@SerialName("photo")
data class InputPaidMediaPhoto(
    val media: String,
) : InputPaidMedia {
    override fun toString() = DebugStringBuilder("InputPaidMediaPhoto").prop("media", media).toString()
}

/**
 * The paid media to send is a video.
 * @param media File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param thumbnail Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param width Optional. Video width
 * @param height Optional. Video height
 * @param duration Optional. Video duration in seconds
 * @param supportsStreaming Optional. Pass True if the uploaded video is suitable for streaming
 * @param cover Optional. Cover for the video in the message. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new one using multipart/form-data under <file_attach_name> name. More information on Sending Files »
 * @param startTimestamp Optional. Start timestamp for the video in the message
 */
@Serializable
@SerialName("video")
data class InputPaidMediaVideo(
    val media: String,
    val thumbnail: String? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Seconds? = null,
    val supportsStreaming: Boolean? = null,
    val cover: String? = null,
    val startTimestamp: Long? = null,
) : InputPaidMedia {
    override fun toString() = DebugStringBuilder("InputPaidMediaVideo").prop("media", media).prop("thumbnail", thumbnail).prop("width", width).prop("height", height).prop("duration", duration).prop("supportsStreaming", supportsStreaming).prop("cover", cover).prop("startTimestamp", startTimestamp).toString()
}

/**
 * This object describes a profile photo to set. Currently, it can be one of
 * - [InputProfilePhotoStatic]
 * - [InputProfilePhotoAnimated]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface InputProfilePhoto

/**
 * A static profile photo in the .JPG format.
 * @param photo The static profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the photo was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 */
@Serializable
@SerialName("static")
data class InputProfilePhotoStatic(
    val photo: String,
) : InputProfilePhoto {
    override fun toString() = DebugStringBuilder("InputProfilePhotoStatic").prop("photo", photo).toString()
}

/**
 * An animated profile photo in the MPEG4 format.
 * @param animation The animated profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the photo was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param mainFrameTimestamp Optional. Timestamp in seconds of the frame that will be used as the static profile photo. Defaults to 0.0.
 */
@Serializable
@SerialName("animated")
data class InputProfilePhotoAnimated(
    val animation: String,
    val mainFrameTimestamp: Double? = null,
) : InputProfilePhoto {
    override fun toString() = DebugStringBuilder("InputProfilePhotoAnimated").prop("animation", animation).prop("mainFrameTimestamp", mainFrameTimestamp).toString()
}

/**
 * This object describes the content of a story to post. Currently, it can be one of
 * - [InputStoryContentPhoto]
 * - [InputStoryContentVideo]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface InputStoryContent

/**
 * Describes a photo to post as a story.
 * @param photo The photo to post as a story. The photo must be of the size 1080x1920 and must not exceed 10 MB. The photo can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the photo was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 */
@Serializable
@SerialName("photo")
data class InputStoryContentPhoto(
    val photo: String,
) : InputStoryContent {
    override fun toString() = DebugStringBuilder("InputStoryContentPhoto").prop("photo", photo).toString()
}

/**
 * Describes a video to post as a story.
 * @param video The video to post as a story. The video must be of the size 720x1280, streamable, encoded with H.265 codec, with key frames added each second in the MPEG4 format, and must not exceed 30 MB. The video can't be reused and can only be uploaded as a new file, so you can pass “attach://<file_attach_name>” if the video was uploaded using multipart/form-data under <file_attach_name>. More information on Sending Files »
 * @param duration Optional. Precise duration of the video in seconds; 0-60
 * @param coverFrameTimestamp Optional. Timestamp in seconds of the frame that will be used as the static cover for the story. Defaults to 0.0.
 * @param isAnimation Optional. Pass True if the video has no sound
 */
@Serializable
@SerialName("video")
data class InputStoryContentVideo(
    val video: String,
    val duration: Double? = null,
    val coverFrameTimestamp: Double? = null,
    val isAnimation: Boolean? = null,
) : InputStoryContent {
    override fun toString() = DebugStringBuilder("InputStoryContentVideo").prop("video", video).prop("duration", duration).prop("coverFrameTimestamp", coverFrameTimestamp).prop("isAnimation", isAnimation).toString()
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
 * @param stickers List of all set stickers
 * @param thumbnail Optional. Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format
 */
@Serializable
data class StickerSet(
    val name: String,
    val title: String,
    val stickerType: String,
    val stickers: List<Sticker>,
    val thumbnail: PhotoSize? = null,
) {
    override fun toString() = DebugStringBuilder("StickerSet").prop("name", name).prop("title", title).prop("stickerType", stickerType).prop("stickers", stickers).prop("thumbnail", thumbnail).toString()
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
 * @param sticker The added sticker. Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or pass “attach://<file_attach_name>” to upload a new file using multipart/form-data under <file_attach_name> name. Animated and video stickers can't be uploaded via HTTP URL. More information on Sending Files »
 * @param format Format of the added sticker, must be one of “static” for a .WEBP or .PNG image, “animated” for a .TGS animation, “video” for a .WEBM video
 * @param emojiList List of 1-20 emoji associated with the sticker
 * @param maskPosition Optional. Position where the mask should be placed on faces. For “mask” stickers only.
 * @param keywords Optional. List of 0-20 search keywords for the sticker with total length of up to 64 characters. For “regular” and “custom_emoji” stickers only.
 */
@Serializable
data class InputSticker(
    val sticker: String,
    val format: String,
    val emojiList: List<String>,
    val maskPosition: MaskPosition? = null,
    val keywords: List<String>? = null,
) {
    override fun toString() = DebugStringBuilder("InputSticker").prop("sticker", sticker).prop("format", format).prop("emojiList", emojiList).prop("maskPosition", maskPosition).prop("keywords", keywords).toString()
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
sealed interface InlineQueryResult

/**
 * Represents a link to an article or web page.
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param title Title of the result
 * @param inputMessageContent Content of the message to be sent
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param url Optional. URL of the result
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
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Long? = null,
    val thumbnailHeight: Long? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultArticle").prop("id", id).prop("title", title).prop("inputMessageContent", inputMessageContent).prop("replyMarkup", replyMarkup).prop("url", url).prop("description", description).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
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
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultPhoto").prop("id", id).prop("photoUrl", photoUrl).prop("thumbnailUrl", thumbnailUrl).prop("photoWidth", photoWidth).prop("photoHeight", photoHeight).prop("title", title).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gifUrl A valid URL for the GIF file
 * @param thumbnailUrl URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
 * @param gifWidth Optional. Width of the GIF
 * @param gifHeight Optional. Height of the GIF
 * @param gifDuration Optional. Duration of the GIF in seconds
 * @param thumbnailMimeType Optional. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultGif").prop("id", id).prop("gifUrl", gifUrl).prop("thumbnailUrl", thumbnailUrl).prop("gifWidth", gifWidth).prop("gifHeight", gifHeight).prop("gifDuration", gifDuration).prop("thumbnailMimeType", thumbnailMimeType).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param mpeg4Url A valid URL for the MPEG4 file
 * @param thumbnailUrl URL of the static (JPEG or GIF) or animated (MPEG4) thumbnail for the result
 * @param mpeg4Width Optional. Video width
 * @param mpeg4Height Optional. Video height
 * @param mpeg4Duration Optional. Video duration in seconds
 * @param thumbnailMimeType Optional. MIME type of the thumbnail, must be one of “image/jpeg”, “image/gif”, or “video/mp4”. Defaults to “image/jpeg”
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultMpeg4Gif").prop("id", id).prop("mpeg4Url", mpeg4Url).prop("thumbnailUrl", thumbnailUrl).prop("mpeg4Width", mpeg4Width).prop("mpeg4Height", mpeg4Height).prop("mpeg4Duration", mpeg4Duration).prop("thumbnailMimeType", thumbnailMimeType).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
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
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val videoWidth: Long? = null,
    val videoHeight: Long? = null,
    val videoDuration: Long? = null,
    val description: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultVideo").prop("id", id).prop("videoUrl", videoUrl).prop("mimeType", mimeType).prop("thumbnailUrl", thumbnailUrl).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("videoWidth", videoWidth).prop("videoHeight", videoHeight).prop("videoDuration", videoDuration).prop("description", description).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
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
) : InlineQueryResult {
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
) : InlineQueryResult {
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
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultDocument").prop("id", id).prop("title", title).prop("documentUrl", documentUrl).prop("mimeType", mimeType).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("description", description).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).prop("thumbnailUrl", thumbnailUrl).prop("thumbnailWidth", thumbnailWidth).prop("thumbnailHeight", thumbnailHeight).toString()
}

/**
 * Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the location.
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param latitude Location latitude in degrees
 * @param longitude Location longitude in degrees
 * @param title Location title
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
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
) : InlineQueryResult {
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
) : InlineQueryResult {
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
) : InlineQueryResult {
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
) : InlineQueryResult {
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
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedPhoto").prop("id", id).prop("photoFileId", photoFileId).prop("title", title).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gifFileId A valid file identifier for the GIF file
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedGif").prop("id", id).prop("gifFileId", gifFileId).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
}

/**
 * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound) stored on the Telegram servers. By default, this animated MPEG-4 file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
 * @param id Unique identifier for this result, 1-64 bytes
 * @param mpeg4FileId A valid file identifier for the MPEG4 file
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the MPEG-4 file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedMpeg4Gif").prop("id", id).prop("mpeg4FileId", mpeg4FileId).prop("title", title).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
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
) : InlineQueryResult {
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
) : InlineQueryResult {
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
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
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
    val showCaptionAboveMedia: Boolean? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override fun toString() = DebugStringBuilder("InlineQueryResultCachedVideo").prop("id", id).prop("videoFileId", videoFileId).prop("title", title).prop("description", description).prop("caption", caption).prop("parseMode", parseMode).prop("captionEntities", captionEntities).prop("showCaptionAboveMedia", showCaptionAboveMedia).prop("replyMarkup", replyMarkup).prop("inputMessageContent", inputMessageContent).toString()
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
) : InlineQueryResult {
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
) : InlineQueryResult {
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
sealed interface InputMessageContent

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
 * @param linkPreviewOptions Optional. Link preview generation options for the message
 */
@Serializable
data class InputTextMessageContent(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
) : InputMessageContent {
    override fun toString() = DebugStringBuilder("InputTextMessageContent").prop("messageText", messageText).prop("parseMode", parseMode).prop("entities", entities).prop("linkPreviewOptions", linkPreviewOptions).toString()
}

/**
 * Represents the content of a location message to be sent as the result of an inline query.
 * @param latitude Latitude of the location in degrees
 * @param longitude Longitude of the location in degrees
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
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
) : InputMessageContent {
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
) : InputMessageContent {
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
) : InputMessageContent {
    override fun toString() = DebugStringBuilder("InputContactMessageContent").prop("phoneNumber", phoneNumber).prop("firstName", firstName).prop("lastName", lastName).prop("vcard", vcard).toString()
}

/**
 * Represents the content of an invoice message to be sent as the result of an inline query.
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use it for your internal processes.
 * @param currency Three-letter ISO 4217 currency code, see more on currencies. Pass “XTR” for payments in Telegram Stars.
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.). Must contain exactly one item for payments in Telegram Stars.
 * @param providerToken Optional. Payment provider token, obtained via @BotFather. Pass an empty string for payments in Telegram Stars.
 * @param maxTipAmount Optional. The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0. Not supported for payments in Telegram Stars.
 * @param suggestedTipAmounts Optional. A JSON-serialized array of suggested amounts of tip in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param providerData Optional. A JSON-serialized object for data about the invoice, which will be shared with the payment provider. A detailed description of the required fields should be provided by the payment provider.
 * @param photoUrl Optional. URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @param photoSize Optional. Photo size in bytes
 * @param photoWidth Optional. Photo width
 * @param photoHeight Optional. Photo height
 * @param needName Optional. Pass True if you require the user's full name to complete the order. Ignored for payments in Telegram Stars.
 * @param needPhoneNumber Optional. Pass True if you require the user's phone number to complete the order. Ignored for payments in Telegram Stars.
 * @param needEmail Optional. Pass True if you require the user's email address to complete the order. Ignored for payments in Telegram Stars.
 * @param needShippingAddress Optional. Pass True if you require the user's shipping address to complete the order. Ignored for payments in Telegram Stars.
 * @param sendPhoneNumberToProvider Optional. Pass True if the user's phone number should be sent to the provider. Ignored for payments in Telegram Stars.
 * @param sendEmailToProvider Optional. Pass True if the user's email address should be sent to the provider. Ignored for payments in Telegram Stars.
 * @param isFlexible Optional. Pass True if the final price depends on the shipping method. Ignored for payments in Telegram Stars.
 */
@Serializable
data class InputInvoiceMessageContent(
    val title: String,
    val description: String,
    val payload: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    val providerToken: String? = null,
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
) : InputMessageContent {
    override fun toString() = DebugStringBuilder("InputInvoiceMessageContent").prop("title", title).prop("description", description).prop("payload", payload).prop("currency", currency).prop("prices", prices).prop("providerToken", providerToken).prop("maxTipAmount", maxTipAmount).prop("suggestedTipAmounts", suggestedTipAmounts).prop("providerData", providerData).prop("photoUrl", photoUrl).prop("photoSize", photoSize).prop("photoWidth", photoWidth).prop("photoHeight", photoHeight).prop("needName", needName).prop("needPhoneNumber", needPhoneNumber).prop("needEmail", needEmail).prop("needShippingAddress", needShippingAddress).prop("sendPhoneNumberToProvider", sendPhoneNumberToProvider).prop("sendEmailToProvider", sendEmailToProvider).prop("isFlexible", isFlexible).toString()
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
 * Describes an inline message to be sent by a user of a Mini App.
 * @param id Unique identifier of the prepared message
 * @param expirationDate Expiration date of the prepared message, in Unix time. Expired prepared messages can no longer be used
 */
@Serializable
data class PreparedInlineMessage(
    val id: String,
    val expirationDate: Long,
) {
    override fun toString() = DebugStringBuilder("PreparedInlineMessage").prop("id", id).prop("expirationDate", expirationDate).toString()
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
 * @param currency Three-letter ISO 4217 currency code, or “XTR” for payments in Telegram Stars
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
 * This object contains basic information about a successful payment. Note that if the buyer initiates a chargeback with the relevant payment provider following this transaction, the funds may be debited from your balance. This is outside of Telegram's control.
 * @param currency Three-letter ISO 4217 currency code, or “XTR” for payments in Telegram Stars
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot-specified invoice payload
 * @param telegramPaymentChargeId Telegram payment identifier
 * @param providerPaymentChargeId Provider payment identifier
 * @param shippingOptionId Optional. Identifier of the shipping option chosen by the user
 * @param orderInfo Optional. Order information provided by the user
 * @param subscriptionExpirationDate Optional. Expiration date of the subscription, in Unix time; for recurring payments only
 * @param isRecurring Optional. True, if the payment is a recurring payment for a subscription
 * @param isFirstRecurring Optional. True, if the payment is the first payment for a subscription
 */
@Serializable
data class SuccessfulPayment(
    val currency: String,
    val totalAmount: Long,
    val invoicePayload: String,
    val telegramPaymentChargeId: TelegramPaymentChargeId,
    val providerPaymentChargeId: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null,
    val subscriptionExpirationDate: Long? = null,
    val isRecurring: Boolean? = null,
    val isFirstRecurring: Boolean? = null,
) {
    override fun toString() = DebugStringBuilder("SuccessfulPayment").prop("currency", currency).prop("totalAmount", totalAmount).prop("invoicePayload", invoicePayload).prop("telegramPaymentChargeId", telegramPaymentChargeId).prop("providerPaymentChargeId", providerPaymentChargeId).prop("shippingOptionId", shippingOptionId).prop("orderInfo", orderInfo).prop("subscriptionExpirationDate", subscriptionExpirationDate).prop("isRecurring", isRecurring).prop("isFirstRecurring", isFirstRecurring).toString()
}

/**
 * This object contains basic information about a refunded payment.
 * @param currency Three-letter ISO 4217 currency code, or “XTR” for payments in Telegram Stars. Currently, always “XTR”
 * @param totalAmount Total refunded price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45, total_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot-specified invoice payload
 * @param telegramPaymentChargeId Telegram payment identifier
 * @param providerPaymentChargeId Optional. Provider payment identifier
 */
@Serializable
data class RefundedPayment(
    val currency: String,
    val totalAmount: Long,
    val invoicePayload: String,
    val telegramPaymentChargeId: TelegramPaymentChargeId,
    val providerPaymentChargeId: String? = null,
) {
    override fun toString() = DebugStringBuilder("RefundedPayment").prop("currency", currency).prop("totalAmount", totalAmount).prop("invoicePayload", invoicePayload).prop("telegramPaymentChargeId", telegramPaymentChargeId).prop("providerPaymentChargeId", providerPaymentChargeId).toString()
}

/**
 * This object contains information about an incoming shipping query.
 * @param id Unique query identifier
 * @param from User who sent the query
 * @param invoicePayload Bot-specified invoice payload
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
 * @param currency Three-letter ISO 4217 currency code, or “XTR” for payments in Telegram Stars
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot-specified invoice payload
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
 * This object contains information about a paid media purchase.
 * @param from User who purchased the media
 * @param paidMediaPayload Bot-specified paid media payload
 */
@Serializable
data class PaidMediaPurchased(
    val from: User,
    val paidMediaPayload: String,
) {
    override fun toString() = DebugStringBuilder("PaidMediaPurchased").prop("from", from).prop("paidMediaPayload", paidMediaPayload).toString()
}

/**
 * This object describes the state of a revenue withdrawal operation. Currently, it can be one of
 * - [RevenueWithdrawalStatePending]
 * - [RevenueWithdrawalStateSucceeded]
 * - [RevenueWithdrawalStateFailed]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface RevenueWithdrawalState

/**
 * The withdrawal is in progress.
 */
@Serializable
@SerialName("pending")
data object RevenueWithdrawalStatePending : RevenueWithdrawalState

/**
 * The withdrawal succeeded.
 * @param date Date the withdrawal was completed in Unix time
 * @param url An HTTPS URL that can be used to see transaction details
 */
@Serializable
@SerialName("succeeded")
data class RevenueWithdrawalStateSucceeded(
    val date: UnixTimestamp,
    val url: String,
) : RevenueWithdrawalState {
    override fun toString() = DebugStringBuilder("RevenueWithdrawalStateSucceeded").prop("date", date).prop("url", url).toString()
}

/**
 * The withdrawal failed and the transaction was refunded.
 */
@Serializable
@SerialName("failed")
data object RevenueWithdrawalStateFailed : RevenueWithdrawalState

/**
 * Contains information about the affiliate that received a commission via this transaction.
 * @param commissionPerMille The number of Telegram Stars received by the affiliate for each 1000 Telegram Stars received by the bot from referred users
 * @param amount Integer amount of Telegram Stars received by the affiliate from the transaction, rounded to 0; can be negative for refunds
 * @param affiliateUser Optional. The bot or the user that received an affiliate commission if it was received by a bot or a user
 * @param affiliateChat Optional. The chat that received an affiliate commission if it was received by a chat
 * @param nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars received by the affiliate; from -999999999 to 999999999; can be negative for refunds
 */
@Serializable
data class AffiliateInfo(
    val commissionPerMille: Long,
    val amount: Long,
    val affiliateUser: User? = null,
    val affiliateChat: Chat? = null,
    val nanostarAmount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("AffiliateInfo").prop("commissionPerMille", commissionPerMille).prop("amount", amount).prop("affiliateUser", affiliateUser).prop("affiliateChat", affiliateChat).prop("nanostarAmount", nanostarAmount).toString()
}

/**
 * This object describes the source of a transaction, or its recipient for outgoing transactions. Currently, it can be one of
 * - [TransactionPartnerUser]
 * - [TransactionPartnerChat]
 * - [TransactionPartnerAffiliateProgram]
 * - [TransactionPartnerFragment]
 * - [TransactionPartnerTelegramAds]
 * - [TransactionPartnerTelegramApi]
 * - [TransactionPartnerOther]
 */
@Serializable
@JsonClassDiscriminator("type")
sealed interface TransactionPartner

/**
 * Describes a transaction with a user.
 * @param user Information about the user
 * @param invoicePayload Optional. Bot-specified invoice payload. Can be available only for “invoice_payment” transactions.
 * @param paidMedia Optional. Information about the paid media bought by the user; for “paid_media_payment” transactions only
 * @param transactionType Type of the transaction, currently one of “invoice_payment” for payments via invoices, “paid_media_payment” for payments for paid media, “gift_purchase” for gifts sent by the bot, “premium_purchase” for Telegram Premium subscriptions gifted by the bot, “business_account_transfer” for direct transfers from managed business accounts
 * @param affiliate Optional. Information about the affiliate that received a commission via this transaction. Can be available only for “invoice_payment” and “paid_media_payment” transactions.
 * @param subscriptionPeriod Optional. The duration of the paid subscription. Can be available only for “invoice_payment” transactions.
 * @param paidMediaPayload Optional. Bot-specified paid media payload. Can be available only for “paid_media_payment” transactions.
 * @param gift Optional. The gift sent to the user by the bot; for “gift_purchase” transactions only
 * @param premiumSubscriptionDuration Optional. Number of months the gifted Telegram Premium subscription will be active for; for “premium_purchase” transactions only
 */
@Serializable
@SerialName("user")
data class TransactionPartnerUser(
    val user: User,
    val invoicePayload: String? = null,
    val paidMedia: List<PaidMedia>? = null,
    val transactionType: String,
    val affiliate: AffiliateInfo? = null,
    val subscriptionPeriod: Seconds? = null,
    val paidMediaPayload: String? = null,
    val gift: Gift? = null,
    val premiumSubscriptionDuration: Long? = null,
) : TransactionPartner {
    override fun toString() = DebugStringBuilder("TransactionPartnerUser").prop("user", user).prop("invoicePayload", invoicePayload).prop("paidMedia", paidMedia).prop("transactionType", transactionType).prop("affiliate", affiliate).prop("subscriptionPeriod", subscriptionPeriod).prop("paidMediaPayload", paidMediaPayload).prop("gift", gift).prop("premiumSubscriptionDuration", premiumSubscriptionDuration).toString()
}

/**
 * Describes a transaction with a chat.
 * @param chat Information about the chat
 * @param gift Optional. The gift sent to the chat by the bot
 */
@Serializable
@SerialName("chat")
data class TransactionPartnerChat(
    val chat: Chat,
    val gift: Gift? = null,
) : TransactionPartner {
    override fun toString() = DebugStringBuilder("TransactionPartnerChat").prop("chat", chat).prop("gift", gift).toString()
}

/**
 * Describes the affiliate program that issued the affiliate commission received via this transaction.
 * @param commissionPerMille The number of Telegram Stars received by the bot for each 1000 Telegram Stars received by the affiliate program sponsor from referred users
 * @param sponsorUser Optional. Information about the bot that sponsored the affiliate program
 */
@Serializable
@SerialName("affiliate_program")
data class TransactionPartnerAffiliateProgram(
    val commissionPerMille: Long,
    val sponsorUser: User? = null,
) : TransactionPartner {
    override fun toString() = DebugStringBuilder("TransactionPartnerAffiliateProgram").prop("commissionPerMille", commissionPerMille).prop("sponsorUser", sponsorUser).toString()
}

/**
 * Describes a withdrawal transaction with Fragment.
 * @param withdrawalState Optional. State of the transaction if the transaction is outgoing
 */
@Serializable
@SerialName("fragment")
data class TransactionPartnerFragment(
    val withdrawalState: RevenueWithdrawalState? = null,
) : TransactionPartner {
    override fun toString() = DebugStringBuilder("TransactionPartnerFragment").prop("withdrawalState", withdrawalState).toString()
}

/**
 * Describes a withdrawal transaction to the Telegram Ads platform.
 */
@Serializable
@SerialName("telegram_ads")
data object TransactionPartnerTelegramAds : TransactionPartner

/**
 * Describes a transaction with payment for paid broadcasting.
 * @param requestCount The number of successful requests that exceeded regular limits and were therefore billed
 */
@Serializable
@SerialName("telegram_api")
data class TransactionPartnerTelegramApi(
    val requestCount: Long,
) : TransactionPartner {
    override fun toString() = DebugStringBuilder("TransactionPartnerTelegramApi").prop("requestCount", requestCount).toString()
}

/**
 * Describes a transaction with an unknown source or recipient.
 */
@Serializable
@SerialName("other")
data object TransactionPartnerOther : TransactionPartner

/**
 * Describes a Telegram Star transaction. Note that if the buyer initiates a chargeback with the payment provider from whom they acquired Stars (e.g., Apple, Google) following this transaction, the refunded Stars will be deducted from the bot's balance. This is outside of Telegram's control.
 * @param id Unique identifier of the transaction. Coincides with the identifier of the original transaction for refund transactions. Coincides with SuccessfulPayment.telegram_payment_charge_id for successful incoming payments from users.
 * @param amount Integer amount of Telegram Stars transferred by the transaction
 * @param date Date the transaction was created in Unix time
 * @param source Optional. Source of an incoming transaction (e.g., a user purchasing goods or services, Fragment refunding a failed withdrawal). Only for incoming transactions
 * @param receiver Optional. Receiver of an outgoing transaction (e.g., a user for a purchase refund, Fragment for a withdrawal). Only for outgoing transactions
 * @param nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars transferred by the transaction; from 0 to 999999999
 */
@Serializable
data class StarTransaction(
    val id: String,
    val amount: Long,
    val date: UnixTimestamp,
    val source: TransactionPartner? = null,
    val receiver: TransactionPartner? = null,
    val nanostarAmount: Long? = null,
) {
    override fun toString() = DebugStringBuilder("StarTransaction").prop("id", id).prop("amount", amount).prop("date", date).prop("source", source).prop("receiver", receiver).prop("nanostarAmount", nanostarAmount).toString()
}

/**
 * Contains a list of Telegram Star transactions.
 * @param transactions The list of transactions
 */
@Serializable
data class StarTransactions(
    val transactions: List<StarTransaction>,
) {
    override fun toString() = DebugStringBuilder("StarTransactions").prop("transactions", transactions).toString()
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
 * @param data Optional. Base64-encoded encrypted Telegram Passport element data provided by the user; available only for “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport” and “address” types. Can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param phoneNumber Optional. User's verified phone number; available only for “phone_number” type
 * @param email Optional. User's verified email address; available only for “email” type
 * @param files Optional. Array of encrypted files with documents provided by the user; available only for “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param frontSide Optional. Encrypted file with the front side of the document, provided by the user; available only for “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param reverseSide Optional. Encrypted file with the reverse side of the document, provided by the user; available only for “driver_license” and “identity_card”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param selfie Optional. Encrypted file with the selfie of the user holding a document, provided by the user; available if requested for “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param translation Optional. Array of encrypted files with translated versions of documents provided by the user; available if requested for “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
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
@JsonClassDiscriminator("source")
sealed interface PassportElementError

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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
) : PassportElementError {
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
sealed interface ReplyMarkup

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

/**
 * Chat identifier.
 */
@Serializable
@JvmInline
value class ChatId(val value: Long) {
    override fun toString(): String = "ChatId(${quoteWhenWhitespace(value)})"
}

/**
 * User identifier.
 */
@Serializable
@JvmInline
value class UserId(val value: Long) {
    override fun toString(): String = "UserId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque message identifier.
 */
@Serializable
@JvmInline
value class MessageId(val value: Long) {
    override fun toString(): String = "MessageId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque inline message identifier.
 */
@Serializable
@JvmInline
value class InlineMessageId(val value: String) {
    override fun toString(): String = "InlineMessageId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque message thread identifier.
 */
@Serializable
@JvmInline
value class MessageThreadId(val value: Long) {
    override fun toString(): String = "MessageThreadId(${quoteWhenWhitespace(value)})"
}

/**
 * Unique identifier of the message effect to be added to the message; for private chats only
 */
@Serializable
@JvmInline
value class MessageEffectId(val value: String) {
    override fun toString(): String = "MessageEffectId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque [CallbackQuery] identifier.
 */
@Serializable
@JvmInline
value class CallbackQueryId(val value: String) {
    override fun toString(): String = "CallbackQueryId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque [InlineQuery] identifier.
 */
@Serializable
@JvmInline
value class InlineQueryId(val value: String) {
    override fun toString(): String = "InlineQueryId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque [InlineQueryResult] identifier.
 */
@Serializable
@JvmInline
value class InlineQueryResultId(val value: String) {
    override fun toString(): String = "InlineQueryResultId(${quoteWhenWhitespace(value)})"
}

/**
 * Identifier for a file, which can be used to download or reuse the file.
 */
@Serializable
@JvmInline
value class FileId(val value: String) {
    override fun toString(): String = "FileId(${quoteWhenWhitespace(value)})"
}

/**
 * Unique identifier for a file, which is supposed to be the same over time and for different bots.
 *
 * It can't be used to download or reuse the file.
 */
@Serializable
@JvmInline
value class FileUniqueId(val value: String) {
    override fun toString(): String = "FileUniqueId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque [ShippingQuery] identifier.
 */
@Serializable
@JvmInline
value class ShippingQueryId(val value: String) {
    override fun toString(): String = "ShippingQueryId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque web-app query identifier.
 */
@Serializable
@JvmInline
value class WebAppQueryId(val value: String) {
    override fun toString(): String = "WebAppQueryId(${quoteWhenWhitespace(value)})"
}

/**
 * Opaque custom emoji identifier.
 */
@Serializable
@JvmInline
value class CustomEmojiId(val value: String) {
    override fun toString(): String = "CustomEmojiId(${quoteWhenWhitespace(value)})"
}

/**
 * Duration in seconds.
 */
@Serializable
@JvmInline
value class Seconds(val value: Long) {
    override fun toString(): String = "Seconds(${quoteWhenWhitespace(value)})"
}

/**
 * Unix time - **number of seconds** that have elapsed since 00:00:00 UTC on 1 January 1970.
 */
@Serializable
@JvmInline
value class UnixTimestamp(val value: Long) {
    override fun toString(): String = "UnixTimestamp(${quoteWhenWhitespace(value)})"
}

/**
 * Telegram payment identifier
 */
@Serializable
@JvmInline
value class TelegramPaymentChargeId(val value: String) {
    override fun toString(): String = "TelegramPaymentChargeId(${quoteWhenWhitespace(value)})"
}

/**
 * Business connection identifier
 */
@Serializable
@JvmInline
value class BusinessConnectionId(val value: String) {
    override fun toString(): String = "BusinessConnectionId(${quoteWhenWhitespace(value)})"
}

