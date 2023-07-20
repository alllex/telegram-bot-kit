package me.alllex.tbot.api.client

import me.alllex.tbot.api.model.*


interface TelegramBotUpdateListener {

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
        }
    }

    context(TelegramBotApiContext)
    suspend fun onMessage(message: Message) {
    }

    context(TelegramBotApiContext)
    suspend fun onEditedMessage(message: Message) {
    }

    context(TelegramBotApiContext)
    suspend fun onChannelPost(message: Message) {
    }

    context(TelegramBotApiContext)
    suspend fun onEditedChannelPost(message: Message) {
    }

    context(TelegramBotApiContext)
    suspend fun onInlineQuery(inlineQuery: InlineQuery) {
    }

    context(TelegramBotApiContext)
    suspend fun onChosenInlineResult(chosenInlineResult: ChosenInlineResult) {
    }

    context(TelegramBotApiContext)
    suspend fun onCallbackQuery(callbackQuery: CallbackQuery) {
    }

    context(TelegramBotApiContext)
    suspend fun onShippingQuery(shippingQuery: ShippingQuery) {
    }

    context(TelegramBotApiContext)
    suspend fun onPreCheckoutQuery(preCheckoutQuery: PreCheckoutQuery) {
    }

    context(TelegramBotApiContext)
    suspend fun onPoll(poll: Poll) {
    }

    context(TelegramBotApiContext)
    suspend fun onPollAnswer(pollAnswer: PollAnswer) {
    }

    context(TelegramBotApiContext)
    suspend fun onMyChatMember(chatMemberUpdated: ChatMemberUpdated) {
    }

    context(TelegramBotApiContext)
    suspend fun onChatMember(chatMemberUpdated: ChatMemberUpdated) {
    }

    context(TelegramBotApiContext)
    suspend fun onChatJoinRequest(chatJoinRequest: ChatJoinRequest) {
    }
}

fun interface TelegramBotUpdateHandler<T> {
    context(TelegramBotApiContext)
    suspend fun handle(update: T)
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
    @Suppress("UNUSED_PARAMETER") noTrailingLambda: Unit = Unit,
): TelegramBotUpdateListener {

    return object : TelegramBotUpdateListener {
        context(TelegramBotApiContext) override suspend fun onMessage(message: Message) =
            onMessage?.handle(message) ?: Unit

        context(TelegramBotApiContext) override suspend fun onEditedMessage(message: Message) =
            onEditedMessage?.handle(message) ?: Unit

        context(TelegramBotApiContext) override suspend fun onChannelPost(message: Message) =
            onChannelPost?.handle(message) ?: Unit

        context(TelegramBotApiContext) override suspend fun onEditedChannelPost(message: Message) =
            onEditedChannelPost?.handle(message) ?: Unit

        context(TelegramBotApiContext) override suspend fun onInlineQuery(inlineQuery: InlineQuery) =
            onInlineQuery?.handle(inlineQuery) ?: Unit

        context(TelegramBotApiContext) override suspend fun onChosenInlineResult(chosenInlineResult: ChosenInlineResult) =
            onChosenInlineResult?.handle(chosenInlineResult) ?: Unit

        context(TelegramBotApiContext) override suspend fun onCallbackQuery(callbackQuery: CallbackQuery) =
            onCallbackQuery?.handle(callbackQuery) ?: Unit

        context(TelegramBotApiContext) override suspend fun onShippingQuery(shippingQuery: ShippingQuery) =
            onShippingQuery?.handle(shippingQuery) ?: Unit

        context(TelegramBotApiContext) override suspend fun onPreCheckoutQuery(preCheckoutQuery: PreCheckoutQuery) =
            onPreCheckoutQuery?.handle(preCheckoutQuery) ?: Unit

        context(TelegramBotApiContext) override suspend fun onPoll(poll: Poll) =
            onPoll?.handle(poll) ?: Unit

        context(TelegramBotApiContext) override suspend fun onPollAnswer(pollAnswer: PollAnswer) =
            onPollAnswer?.handle(pollAnswer) ?: Unit

        context(TelegramBotApiContext) override suspend fun onMyChatMember(chatMemberUpdated: ChatMemberUpdated) =
            onMyChatMember?.handle(chatMemberUpdated) ?: Unit

        context(TelegramBotApiContext) override suspend fun onChatMember(chatMemberUpdated: ChatMemberUpdated) =
            onChatMember?.handle(chatMemberUpdated) ?: Unit

        context(TelegramBotApiContext) override suspend fun onChatJoinRequest(chatJoinRequest: ChatJoinRequest) =
            onChatJoinRequest?.handle(chatJoinRequest) ?: Unit
    }
}
