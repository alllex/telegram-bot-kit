@file:Suppress("CONTEXT_RECEIVERS_DEPRECATED")

package me.alllex.tbot.api.client

import me.alllex.tbot.api.model.*


interface TelegramBotUpdateListener {

    context(TelegramBotApiContext)
    suspend fun onMessage(message: Message) {}

    context(TelegramBotApiContext)
    suspend fun onEditedMessage(editedMessage: Message) {}

    context(TelegramBotApiContext)
    suspend fun onChannelPost(channelPost: Message) {}

    context(TelegramBotApiContext)
    suspend fun onEditedChannelPost(editedChannelPost: Message) {}

    context(TelegramBotApiContext)
    suspend fun onInlineQuery(inlineQuery: InlineQuery) {}

    context(TelegramBotApiContext)
    suspend fun onChosenInlineResult(chosenInlineResult: ChosenInlineResult) {}

    context(TelegramBotApiContext)
    suspend fun onCallbackQuery(callbackQuery: CallbackQuery) {}

    context(TelegramBotApiContext)
    suspend fun onShippingQuery(shippingQuery: ShippingQuery) {}

    context(TelegramBotApiContext)
    suspend fun onPreCheckoutQuery(preCheckoutQuery: PreCheckoutQuery) {}

    context(TelegramBotApiContext)
    suspend fun onPoll(poll: Poll) {}

    context(TelegramBotApiContext)
    suspend fun onPollAnswer(pollAnswer: PollAnswer) {}

    context(TelegramBotApiContext)
    suspend fun onMyChatMember(myChatMember: ChatMemberUpdated) {}

    context(TelegramBotApiContext)
    suspend fun onChatMember(chatMember: ChatMemberUpdated) {}

    context(TelegramBotApiContext)
    suspend fun onChatJoinRequest(chatJoinRequest: ChatJoinRequest) {}

    context(TelegramBotApiContext)
    suspend fun onBusinessConnection(businessConnection: BusinessConnection) {}

    context(TelegramBotApiContext)
    suspend fun onBusinessMessage(businessMessage: Message) {}

    context(TelegramBotApiContext)
    suspend fun onDeletedBusinessMessages(deletedBusinessMessages: BusinessMessagesDeleted) {}

    context(TelegramBotApiContext)
    suspend fun onEditedBusinessMessage(editedBusinessMessage: Message) {}

    context(TelegramBotApiContext)
    suspend fun onMessageReactionCount(messageReactionCount: MessageReactionCountUpdated) {}

    context(TelegramBotApiContext)
    suspend fun onMessageReaction(messageReaction: MessageReactionUpdated) {}

    context(TelegramBotApiContext)
    suspend fun onChatBoost(chatBoost: ChatBoostUpdated) {}

    context(TelegramBotApiContext)
    suspend fun onRemovedChatBoost(removedChatBoost: ChatBoostRemoved) {}

    context(TelegramBotApiContext)
    suspend fun onUpdate(update: Update) {
        when (update) {
            is MessageUpdate -> onMessage(update.message)
            is EditedMessageUpdate -> onEditedMessage(update.editedMessage)
            is ChannelPostUpdate -> onChannelPost(update.channelPost)
            is EditedChannelPostUpdate -> onEditedChannelPost(update.editedChannelPost)
            is InlineQueryUpdate -> onInlineQuery(update.inlineQuery)
            is ChosenInlineResultUpdate -> onChosenInlineResult(update.chosenInlineResult)
            is CallbackQueryUpdate -> onCallbackQuery(update.callbackQuery)
            is ShippingQueryUpdate -> onShippingQuery(update.shippingQuery)
            is PreCheckoutQueryUpdate -> onPreCheckoutQuery(update.preCheckoutQuery)
            is PollUpdate -> onPoll(update.poll)
            is PollAnswerUpdate -> onPollAnswer(update.pollAnswer)
            is MyChatMemberUpdate -> onMyChatMember(update.myChatMember)
            is ChatMemberUpdate -> onChatMember(update.chatMember)
            is ChatJoinRequestUpdate -> onChatJoinRequest(update.chatJoinRequest)
            is BusinessConnectionUpdate -> onBusinessConnection(update.businessConnection)
            is BusinessMessageUpdate -> onBusinessMessage(update.businessMessage)
            is DeletedBusinessMessagesUpdate -> onDeletedBusinessMessages(update.deletedBusinessMessages)
            is EditedBusinessMessageUpdate -> onEditedBusinessMessage(update.editedBusinessMessage)
            is MessageReactionCountUpdate -> onMessageReactionCount(update.messageReactionCount)
            is MessageReactionUpdate -> onMessageReaction(update.messageReaction)
            is ChatBoostUpdate -> onChatBoost(update.chatBoost)
            is RemovedChatBoostUpdate -> onRemovedChatBoost(update.removedChatBoost)
            else -> error("Unknown update type: ${update::class.simpleName}")
        }
    }
}

fun TelegramBotUpdateListener(
    onMessage: TelegramBotUpdateHandler<Message>? = null,
    onEditedMessage: TelegramBotUpdateHandler<Message>? = null,
    onChannelPost: TelegramBotUpdateHandler<Message>? = null,
    onEditedChannelPost: TelegramBotUpdateHandler<Message>? = null,
    onInlineQuery: TelegramBotUpdateHandler<InlineQuery>? = null,
    onChosenInlineResult: TelegramBotUpdateHandler<ChosenInlineResult>? = null,
    onCallbackQuery: TelegramBotUpdateHandler<CallbackQuery>? = null,
    onShippingQuery: TelegramBotUpdateHandler<ShippingQuery>? = null,
    onPreCheckoutQuery: TelegramBotUpdateHandler<PreCheckoutQuery>? = null,
    onPoll: TelegramBotUpdateHandler<Poll>? = null,
    onPollAnswer: TelegramBotUpdateHandler<PollAnswer>? = null,
    onMyChatMember: TelegramBotUpdateHandler<ChatMemberUpdated>? = null,
    onChatMember: TelegramBotUpdateHandler<ChatMemberUpdated>? = null,
    onChatJoinRequest: TelegramBotUpdateHandler<ChatJoinRequest>? = null,
    onBusinessConnection: TelegramBotUpdateHandler<BusinessConnection>? = null,
    onBusinessMessage: TelegramBotUpdateHandler<Message>? = null,
    onDeletedBusinessMessages: TelegramBotUpdateHandler<BusinessMessagesDeleted>? = null,
    onEditedBusinessMessage: TelegramBotUpdateHandler<Message>? = null,
    onMessageReactionCount: TelegramBotUpdateHandler<MessageReactionCountUpdated>? = null,
    onMessageReaction: TelegramBotUpdateHandler<MessageReactionUpdated>? = null,
    onChatBoost: TelegramBotUpdateHandler<ChatBoostUpdated>? = null,
    onRemovedChatBoost: TelegramBotUpdateHandler<ChatBoostRemoved>? = null,
    @Suppress("UNUSED_PARAMETER") noTrailingLambda: Unit = Unit,
): TelegramBotUpdateListener {
    return object : TelegramBotUpdateListener {

        context(TelegramBotApiContext)
        override suspend fun onMessage(message: Message) =
            onMessage?.handle(message) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onEditedMessage(editedMessage: Message) =
            onEditedMessage?.handle(editedMessage) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onChannelPost(channelPost: Message) =
            onChannelPost?.handle(channelPost) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onEditedChannelPost(editedChannelPost: Message) =
            onEditedChannelPost?.handle(editedChannelPost) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onInlineQuery(inlineQuery: InlineQuery) =
            onInlineQuery?.handle(inlineQuery) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onChosenInlineResult(chosenInlineResult: ChosenInlineResult) =
            onChosenInlineResult?.handle(chosenInlineResult) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onCallbackQuery(callbackQuery: CallbackQuery) =
            onCallbackQuery?.handle(callbackQuery) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onShippingQuery(shippingQuery: ShippingQuery) =
            onShippingQuery?.handle(shippingQuery) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onPreCheckoutQuery(preCheckoutQuery: PreCheckoutQuery) =
            onPreCheckoutQuery?.handle(preCheckoutQuery) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onPoll(poll: Poll) =
            onPoll?.handle(poll) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onPollAnswer(pollAnswer: PollAnswer) =
            onPollAnswer?.handle(pollAnswer) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onMyChatMember(myChatMember: ChatMemberUpdated) =
            onMyChatMember?.handle(myChatMember) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onChatMember(chatMember: ChatMemberUpdated) =
            onChatMember?.handle(chatMember) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onChatJoinRequest(chatJoinRequest: ChatJoinRequest) =
            onChatJoinRequest?.handle(chatJoinRequest) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onBusinessConnection(businessConnection: BusinessConnection) =
            onBusinessConnection?.handle(businessConnection) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onBusinessMessage(businessMessage: Message) =
            onBusinessMessage?.handle(businessMessage) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onDeletedBusinessMessages(deletedBusinessMessages: BusinessMessagesDeleted) =
            onDeletedBusinessMessages?.handle(deletedBusinessMessages) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onEditedBusinessMessage(editedBusinessMessage: Message) =
            onEditedBusinessMessage?.handle(editedBusinessMessage) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onMessageReactionCount(messageReactionCount: MessageReactionCountUpdated) =
            onMessageReactionCount?.handle(messageReactionCount) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onMessageReaction(messageReaction: MessageReactionUpdated) =
            onMessageReaction?.handle(messageReaction) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onChatBoost(chatBoost: ChatBoostUpdated) =
            onChatBoost?.handle(chatBoost) ?: Unit

        context(TelegramBotApiContext)
        override suspend fun onRemovedChatBoost(removedChatBoost: ChatBoostRemoved) =
            onRemovedChatBoost?.handle(removedChatBoost) ?: Unit
    }
}
