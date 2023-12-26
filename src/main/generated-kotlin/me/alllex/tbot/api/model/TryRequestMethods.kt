package me.alllex.tbot.api.model

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.alllex.tbot.api.client.*


/**
 * Use this method to receive incoming updates using long polling (wiki). Returns an Array of Update objects.
 */
suspend fun TelegramBotApiClient.tryGetUpdates(requestBody: GetUpdatesRequest): TelegramResponse<List<Update>> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getUpdates")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL, containing a JSON-serialized Update. In case of an unsuccessful request, we will give up after a reasonable amount of attempts. Returns True on success.
 *
 * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter secret_token. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token” with the secret token as content.
 */
suspend fun TelegramBotApiClient.trySetWebhook(requestBody: SetWebhookRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setWebhook")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteWebhook(requestBody: DeleteWebhookRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteWebhook")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get current webhook status. Requires no parameters. On success, returns a WebhookInfo object. If the bot is using getUpdates, will return an object with the url field empty.
 */
suspend fun TelegramBotApiClient.tryGetWebhookInfo(): TelegramResponse<WebhookInfo> =
    httpClient.get {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getWebhookInfo")
        }
    }.body()

/**
 * A simple method for testing your bot's authentication token. Requires no parameters. Returns basic information about the bot in form of a User object.
 */
suspend fun TelegramBotApiClient.tryGetMe(): TelegramResponse<User> =
    httpClient.get {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getMe")
        }
    }.body()

/**
 * Use this method to log out from the cloud Bot API server before launching the bot locally. You must log out the bot before running it locally, otherwise there is no guarantee that the bot will receive updates. After a successful call, you can immediately log in on a local server, but will not be able to log in back to the cloud Bot API server for 10 minutes. Returns True on success. Requires no parameters.
 */
suspend fun TelegramBotApiClient.tryLogOut(): TelegramResponse<Boolean> =
    httpClient.get {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "logOut")
        }
    }.body()

/**
 * Use this method to close the bot instance before moving it from one local server to another. You need to delete the webhook before calling this method to ensure that the bot isn't launched again after server restart. The method will return error 429 in the first 10 minutes after the bot is launched. Returns True on success. Requires no parameters.
 */
suspend fun TelegramBotApiClient.tryClose(): TelegramResponse<Boolean> =
    httpClient.get {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "close")
        }
    }.body()

/**
 * Use this method to send text messages. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendMessage(requestBody: SendMessageRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendMessage")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.tryForwardMessage(requestBody: ForwardMessageRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "forwardMessage")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied. A quiz poll can be copied only if the value of the field correct_option_id is known to the bot. The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message. Returns the MessageId of the sent message on success.
 */
suspend fun TelegramBotApiClient.tryCopyMessage(requestBody: CopyMessageRequest): TelegramResponse<MessageIdResult> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "copyMessage")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send photos. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendPhoto(requestBody: SendPhotoRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendPhoto")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned. Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
 *
 * For sending voice messages, use the sendVoice method instead.
 */
suspend fun TelegramBotApiClient.trySendAudio(requestBody: SendAudioRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendAudio")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send general files. On success, the sent Message is returned. Bots can currently send files of any type of up to 50 MB in size, this limit may be changed in the future.
 */
suspend fun TelegramBotApiClient.trySendDocument(requestBody: SendDocumentRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendDocument")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
 */
suspend fun TelegramBotApiClient.trySendVideo(requestBody: SendVideoRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendVideo")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
 */
suspend fun TelegramBotApiClient.trySendAnimation(requestBody: SendAnimationRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendAnimation")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message. For this to work, your audio must be in an .OGG file encoded with OPUS (other formats may be sent as Audio or Document). On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
 */
suspend fun TelegramBotApiClient.trySendVoice(requestBody: SendVoiceRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendVoice")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * As of v.4.0, Telegram clients support rounded square MPEG4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendVideoNote(requestBody: SendVideoNoteRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendVideoNote")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send a group of photos, videos, documents or audios as an album. Documents and audio files can be only grouped in an album with messages of the same type. On success, an array of Messages that were sent is returned.
 */
suspend fun TelegramBotApiClient.trySendMediaGroup(requestBody: SendMediaGroupRequest): TelegramResponse<List<Message>> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendMediaGroup")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send point on the map. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendLocation(requestBody: SendLocationRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendLocation")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send information about a venue. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendVenue(requestBody: SendVenueRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendVenue")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send phone contacts. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendContact(requestBody: SendContactRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendContact")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send a native poll. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendPoll(requestBody: SendPollRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendPoll")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send an animated emoji that will display a random value. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendDice(requestBody: SendDiceRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendDice")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on success.
 *
 * Example: The ImageBot needs some time to process a request and upload the image. Instead of sending a text message along the lines of “Retrieving image, please wait…”, the bot may use sendChatAction with action = upload_photo. The user will see a “sending photo” status for the bot.
 *
 * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
 */
suspend fun TelegramBotApiClient.trySendChatAction(requestBody: SendChatActionRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendChatAction")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
 */
suspend fun TelegramBotApiClient.tryGetUserProfilePhotos(requestBody: GetUserProfilePhotosRequest): TelegramResponse<UserProfilePhotos> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getUserProfilePhotos")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get basic information about a file and prepare it for downloading. For the moment, bots can download files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is guaranteed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling getFile again.
 */
suspend fun TelegramBotApiClient.tryGetFile(requestBody: GetFileRequest): TelegramResponse<File> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getFile")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the user will not be able to return to the chat on their own using invite links, etc., unless unbanned first. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryBanChatMember(requestBody: BanChatMemberRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "banChatMember")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to unban a previously banned user in a supergroup or channel. The user will not return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be able to join it. So if the user is a member of the chat they will also be removed from the chat. If you don't want this, use the parameter only_if_banned. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnbanChatMember(requestBody: UnbanChatMemberRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unbanChatMember")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass True for all permissions to lift restrictions from a user. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryRestrictChatMember(requestBody: RestrictChatMemberRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "restrictChatMember")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to promote or demote a user in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Pass False for all boolean parameters to demote a user. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryPromoteChatMember(requestBody: PromoteChatMemberRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "promoteChatMember")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatAdministratorCustomTitle(requestBody: SetChatAdministratorCustomTitleRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatAdministratorCustomTitle")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is unbanned, the owner of the banned chat won't be able to send messages on behalf of any of their channels. The bot must be an administrator in the supergroup or channel for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryBanChatSenderChat(requestBody: BanChatSenderChatRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "banChatSenderChat")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to unban a previously banned channel chat in a supergroup or channel. The bot must be an administrator for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnbanChatSenderChat(requestBody: UnbanChatSenderChatRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unbanChatSenderChat")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a supergroup for this to work and must have the can_restrict_members administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatPermissions(requestBody: SetChatPermissionsRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatPermissions")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the new invite link as String on success.
 */
suspend fun TelegramBotApiClient.tryExportChatInviteLink(requestBody: ExportChatInviteLinkRequest): TelegramResponse<String> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "exportChatInviteLink")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. The link can be revoked using the method revokeChatInviteLink. Returns the new invite link as ChatInviteLink object.
 */
suspend fun TelegramBotApiClient.tryCreateChatInviteLink(requestBody: CreateChatInviteLinkRequest): TelegramResponse<ChatInviteLink> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "createChatInviteLink")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit a non-primary invite link created by the bot. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the edited invite link as a ChatInviteLink object.
 */
suspend fun TelegramBotApiClient.tryEditChatInviteLink(requestBody: EditChatInviteLinkRequest): TelegramResponse<ChatInviteLink> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editChatInviteLink")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to revoke an invite link created by the bot. If the primary link is revoked, a new link is automatically generated. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns the revoked invite link as ChatInviteLink object.
 */
suspend fun TelegramBotApiClient.tryRevokeChatInviteLink(requestBody: RevokeChatInviteLinkRequest): TelegramResponse<ChatInviteLink> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "revokeChatInviteLink")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryApproveChatJoinRequest(requestBody: ApproveChatJoinRequestRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "approveChatJoinRequest")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to decline a chat join request. The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeclineChatJoinRequest(requestBody: DeclineChatJoinRequestRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "declineChatJoinRequest")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatPhoto(requestBody: SetChatPhotoRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatPhoto")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteChatPhoto(requestBody: DeleteChatPhotoRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteChatPhoto")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the title of a chat. Titles can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatTitle(requestBody: SetChatTitleRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatTitle")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the description of a group, a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatDescription(requestBody: SetChatDescriptionRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatDescription")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to add a message to the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryPinChatMessage(requestBody: PinChatMessageRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "pinChatMessage")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to remove a message from the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnpinChatMessage(requestBody: UnpinChatMessageRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unpinChatMessage")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to clear the list of pinned messages in a chat. If the chat is not a private chat, the bot must be an administrator in the chat for this to work and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnpinAllChatMessages(requestBody: UnpinAllChatMessagesRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unpinAllChatMessages")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryLeaveChat(requestBody: LeaveChatRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "leaveChat")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get up to date information about the chat (current name of the user for one-on-one conversations, current username of a user, group or channel, etc.). Returns a Chat object on success.
 */
suspend fun TelegramBotApiClient.tryGetChat(requestBody: GetChatRequest): TelegramResponse<Chat> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getChat")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of ChatMember objects.
 */
suspend fun TelegramBotApiClient.tryGetChatAdministrators(requestBody: GetChatAdministratorsRequest): TelegramResponse<List<ChatMember>> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getChatAdministrators")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the number of members in a chat. Returns Int on success.
 */
suspend fun TelegramBotApiClient.tryGetChatMemberCount(requestBody: GetChatMemberCountRequest): TelegramResponse<Int> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getChatMemberCount")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get information about a member of a chat. The method is only guaranteed to work for other users if the bot is an administrator in the chat. Returns a ChatMember object on success.
 */
suspend fun TelegramBotApiClient.tryGetChatMember(requestBody: GetChatMemberRequest): TelegramResponse<ChatMember> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getChatMember")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatStickerSet(requestBody: SetChatStickerSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatStickerSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteChatStickerSet(requestBody: DeleteChatStickerSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteChatStickerSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get custom emoji stickers, which can be used as a forum topic icon by any user. Requires no parameters. Returns an Array of Sticker objects.
 */
suspend fun TelegramBotApiClient.tryGetForumTopicIconStickers(): TelegramResponse<List<Sticker>> =
    httpClient.get {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getForumTopicIconStickers")
        }
    }.body()

/**
 * Use this method to create a topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns information about the created topic as a ForumTopic object.
 */
suspend fun TelegramBotApiClient.tryCreateForumTopic(requestBody: CreateForumTopicRequest): TelegramResponse<ForumTopic> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "createForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit name and icon of a topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryEditForumTopic(requestBody: EditForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to close an open topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryCloseForumTopic(requestBody: CloseForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "closeForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to reopen a closed topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights, unless it is the creator of the topic. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryReopenForumTopic(requestBody: ReopenForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "reopenForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete a forum topic along with all its messages in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_delete_messages administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteForumTopic(requestBody: DeleteForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to clear the list of pinned messages in a forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnpinAllForumTopicMessages(requestBody: UnpinAllForumTopicMessagesRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unpinAllForumTopicMessages")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryEditGeneralForumTopic(requestBody: EditGeneralForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editGeneralForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryCloseGeneralForumTopic(requestBody: CloseGeneralForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "closeGeneralForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to reopen a closed 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically unhidden if it was hidden. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryReopenGeneralForumTopic(requestBody: ReopenGeneralForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "reopenGeneralForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to hide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. The topic will be automatically closed if it was open. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryHideGeneralForumTopic(requestBody: HideGeneralForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "hideGeneralForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to unhide the 'General' topic in a forum supergroup chat. The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnhideGeneralForumTopic(requestBody: UnhideGeneralForumTopicRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unhideGeneralForumTopic")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to clear the list of pinned messages in a General forum topic. The bot must be an administrator in the chat for this to work and must have the can_pin_messages administrator right in the supergroup. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryUnpinAllGeneralForumTopicMessages(requestBody: UnpinAllGeneralForumTopicMessagesRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "unpinAllGeneralForumTopicMessages")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the user as a notification at the top of the chat screen or as an alert. On success, True is returned.
 *
 * Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create a game for your bot via @BotFather and accept the terms. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
 */
suspend fun TelegramBotApiClient.tryAnswerCallbackQuery(requestBody: AnswerCallbackQueryRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "answerCallbackQuery")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the list of the bot's commands. See this manual for more details about bot commands. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetMyCommands(requestBody: SetMyCommandsRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setMyCommands")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion, higher level commands will be shown to affected users. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteMyCommands(requestBody: DeleteMyCommandsRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteMyCommands")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language. Returns an Array of BotCommand objects. If commands aren't set, an empty list is returned.
 */
suspend fun TelegramBotApiClient.tryGetMyCommands(requestBody: GetMyCommandsRequest): TelegramResponse<List<BotCommand>> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getMyCommands")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the bot's name. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetMyName(requestBody: SetMyNameRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setMyName")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the current bot name for the given user language. Returns BotName on success.
 */
suspend fun TelegramBotApiClient.tryGetMyName(requestBody: GetMyNameRequest): TelegramResponse<BotName> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getMyName")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetMyDescription(requestBody: SetMyDescriptionRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setMyDescription")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the current bot description for the given user language. Returns BotDescription on success.
 */
suspend fun TelegramBotApiClient.tryGetMyDescription(requestBody: GetMyDescriptionRequest): TelegramResponse<BotDescription> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getMyDescription")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent together with the link when users share the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetMyShortDescription(requestBody: SetMyShortDescriptionRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setMyShortDescription")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the current bot short description for the given user language. Returns BotShortDescription on success.
 */
suspend fun TelegramBotApiClient.tryGetMyShortDescription(requestBody: GetMyShortDescriptionRequest): TelegramResponse<BotShortDescription> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getMyShortDescription")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetChatMenuButton(requestBody: SetChatMenuButtonRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setChatMenuButton")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns MenuButton on success.
 */
suspend fun TelegramBotApiClient.tryGetChatMenuButton(requestBody: GetChatMenuButtonRequest): TelegramResponse<MenuButton> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getChatMenuButton")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the default administrator rights requested by the bot when it's added as an administrator to groups or channels. These rights will be suggested to users, but they are free to modify the list before adding the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetMyDefaultAdministratorRights(requestBody: SetMyDefaultAdministratorRightsRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setMyDefaultAdministratorRights")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get the current default administrator rights of the bot. Returns ChatAdministratorRights on success.
 */
suspend fun TelegramBotApiClient.tryGetMyDefaultAdministratorRights(requestBody: GetMyDefaultAdministratorRightsRequest): TelegramResponse<ChatAdministratorRights> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getMyDefaultAdministratorRights")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit text and game messages. On success the edited Message is returned.
 */
suspend fun TelegramBotApiClient.tryEditMessageText(requestBody: EditMessageTextRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageText")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit text and game messages. On success True is returned.
 */
suspend fun TelegramBotApiClient.tryEditInlineMessageText(requestBody: EditMessageTextRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageText")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit captions of messages. On success the edited Message is returned.
 */
suspend fun TelegramBotApiClient.tryEditMessageCaption(requestBody: EditMessageCaptionRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageCaption")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit captions of messages. On success True is returned.
 */
suspend fun TelegramBotApiClient.tryEditInlineMessageCaption(requestBody: EditMessageCaptionRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageCaption")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit animation, audio, document, photo, or video messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success the edited Message is returned.
 */
suspend fun TelegramBotApiClient.tryEditMessageMedia(requestBody: EditMessageMediaRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageMedia")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit animation, audio, document, photo, or video messages. If a message is part of a message album, then it can be edited only to an audio for audio albums, only to a document for document albums and to a photo or a video otherwise. When an inline message is edited, a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL. On success True is returned.
 */
suspend fun TelegramBotApiClient.tryEditInlineMessageMedia(requestBody: EditMessageMediaRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageMedia")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit live location messages. A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation. On success the edited Message is returned.
 */
suspend fun TelegramBotApiClient.tryEditMessageLiveLocation(requestBody: EditMessageLiveLocationRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageLiveLocation")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit live location messages. A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation. On success True is returned.
 */
suspend fun TelegramBotApiClient.tryEditInlineMessageLiveLocation(requestBody: EditMessageLiveLocationRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageLiveLocation")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to stop updating a live location message before live_period expires. On success the edited Message is returned.
 */
suspend fun TelegramBotApiClient.tryStopMessageLiveLocation(requestBody: StopMessageLiveLocationRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "stopMessageLiveLocation")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to stop updating a live location message before live_period expires. On success True is returned.
 */
suspend fun TelegramBotApiClient.tryStopInlineMessageLiveLocation(requestBody: StopMessageLiveLocationRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "stopMessageLiveLocation")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit only the reply markup of messages. On success the edited Message is returned.
 */
suspend fun TelegramBotApiClient.tryEditMessageReplyMarkup(requestBody: EditMessageReplyMarkupRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageReplyMarkup")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to edit only the reply markup of messages. On success True is returned.
 */
suspend fun TelegramBotApiClient.tryEditInlineMessageReplyMarkup(requestBody: EditMessageReplyMarkupRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "editMessageReplyMarkup")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to stop a poll which was sent by the bot. On success, the stopped Poll is returned.
 */
suspend fun TelegramBotApiClient.tryStopPoll(requestBody: StopPollRequest): TelegramResponse<Poll> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "stopPoll")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete a message, including service messages, with the following limitations: - A message can only be deleted if it was sent less than 48 hours ago. - Service messages about a supergroup, channel, or forum topic creation can't be deleted. - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago. - Bots can delete outgoing messages in private chats, groups, and supergroups. - Bots can delete incoming messages in private chats. - Bots granted can_post_messages permissions can delete outgoing messages in channels. - If the bot is an administrator of a group, it can delete any message there. - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteMessage(requestBody: DeleteMessageRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteMessage")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendSticker(requestBody: SendStickerRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendSticker")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get a sticker set. On success, a StickerSet object is returned.
 */
suspend fun TelegramBotApiClient.tryGetStickerSet(requestBody: GetStickerSetRequest): TelegramResponse<StickerSet> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getStickerSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of Sticker objects.
 */
suspend fun TelegramBotApiClient.tryGetCustomEmojiStickers(requestBody: GetCustomEmojiStickersRequest): TelegramResponse<List<Sticker>> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getCustomEmojiStickers")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to upload a file with a sticker for later use in the createNewStickerSet and addStickerToSet methods (the file can be used multiple times). Returns the uploaded File on success.
 */
suspend fun TelegramBotApiClient.tryUploadStickerFile(requestBody: UploadStickerFileRequest): TelegramResponse<File> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "uploadStickerFile")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryCreateNewStickerSet(requestBody: CreateNewStickerSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "createNewStickerSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to add a new sticker to a set created by the bot. The format of the added sticker must match the format of the other stickers in the set. Emoji sticker sets can have up to 200 stickers. Animated and video sticker sets can have up to 50 stickers. Static sticker sets can have up to 120 stickers. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryAddStickerToSet(requestBody: AddStickerToSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "addStickerToSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to move a sticker in a set created by the bot to a specific position. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetStickerPositionInSet(requestBody: SetStickerPositionInSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setStickerPositionInSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete a sticker from a set created by the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteStickerFromSet(requestBody: DeleteStickerFromSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteStickerFromSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the list of emoji assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetStickerEmojiList(requestBody: SetStickerEmojiListRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setStickerEmojiList")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change search keywords assigned to a regular or custom emoji sticker. The sticker must belong to a sticker set created by the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetStickerKeywords(requestBody: SetStickerKeywordsRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setStickerKeywords")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to change the mask position of a mask sticker. The sticker must belong to a sticker set that was created by the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetStickerMaskPosition(requestBody: SetStickerMaskPositionRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setStickerMaskPosition")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set the title of a created sticker set. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetStickerSetTitle(requestBody: SetStickerSetTitleRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setStickerSetTitle")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match the format of the stickers in the set. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetStickerSetThumbnail(requestBody: SetStickerSetThumbnailRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setStickerSetThumbnail")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set the thumbnail of a custom emoji sticker set. Returns True on success.
 */
suspend fun TelegramBotApiClient.trySetCustomEmojiStickerSetThumbnail(requestBody: SetCustomEmojiStickerSetThumbnailRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setCustomEmojiStickerSetThumbnail")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to delete a sticker set that was created by the bot. Returns True on success.
 */
suspend fun TelegramBotApiClient.tryDeleteStickerSet(requestBody: DeleteStickerSetRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "deleteStickerSet")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send answers to an inline query. On success, True is returned. No more than 50 results per query are allowed.
 */
suspend fun TelegramBotApiClient.tryAnswerInlineQuery(requestBody: AnswerInlineQueryRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "answerInlineQuery")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of the user to the chat from which the query originated. On success, a SentWebAppMessage object is returned.
 */
suspend fun TelegramBotApiClient.tryAnswerWebAppQuery(requestBody: AnswerWebAppQueryRequest): TelegramResponse<SentWebAppMessage> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "answerWebAppQuery")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send invoices. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendInvoice(requestBody: SendInvoiceRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendInvoice")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to create a link for an invoice. Returns the created invoice link as String on success.
 */
suspend fun TelegramBotApiClient.tryCreateInvoiceLink(requestBody: CreateInvoiceLinkRequest): TelegramResponse<String> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "createInvoiceLink")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries. On success, True is returned.
 */
suspend fun TelegramBotApiClient.tryAnswerShippingQuery(requestBody: AnswerShippingQueryRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "answerShippingQuery")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries. On success, True is returned. Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
 */
suspend fun TelegramBotApiClient.tryAnswerPreCheckoutQuery(requestBody: AnswerPreCheckoutQueryRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "answerPreCheckoutQuery")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success.
 *
 * Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
 */
suspend fun TelegramBotApiClient.trySetPassportDataErrors(requestBody: SetPassportDataErrorsRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setPassportDataErrors")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to send a game. On success, the sent Message is returned.
 */
suspend fun TelegramBotApiClient.trySendGame(requestBody: SendGameRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "sendGame")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set the score of the specified user in a game message. On success the edited Message is returned. Returns an error, if the new score is not greater than the user's current score in the chat and force is False.
 */
suspend fun TelegramBotApiClient.trySetGameScore(requestBody: SetGameScoreRequest): TelegramResponse<Message> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setGameScore")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to set the score of the specified user in a game message. On success True is returned. Returns an error, if the new score is not greater than the user's current score in the chat and force is False.
 */
suspend fun TelegramBotApiClient.trySetInlineGameScore(requestBody: SetGameScoreRequest): TelegramResponse<Boolean> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "setGameScore")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

/**
 * Use this method to get data for high score tables. Will return the score of the specified user and several of their neighbors in a game. Returns an Array of GameHighScore objects.
 *
 * This method will currently return scores for the target user, plus two of their closest neighbors on each side. Will also return the top three users if the user and their neighbors are not among them. Please note that this behavior is subject to change.
 */
suspend fun TelegramBotApiClient.tryGetGameHighScores(requestBody: GetGameHighScoresRequest): TelegramResponse<List<GameHighScore>> =
    httpClient.post {
        url {
            protocol = apiProtocol
            host = apiHost
            port = apiPort
            path("bot$apiToken", "getGameHighScores")
        }
        contentType(ContentType.Application.Json)
        setBody(requestBody)
    }.body()

