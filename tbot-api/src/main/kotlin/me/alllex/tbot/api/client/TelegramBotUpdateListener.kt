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

// TODO: this breaks Kotlin compiler: https://youtrack.jetbrains.com/issue/KT-60506

//typealias TelegramBotUpdateHandler<T> = suspend context(TelegramBotApiContext) (T) -> Unit
//
//inline fun TelegramBotUpdateListener(
//    crossinline onMessage: TelegramBotUpdateHandler<Message> = {},
//    crossinline onEditedMessage: TelegramBotUpdateHandler<Message> = {},
//    crossinline onChannelPost: TelegramBotUpdateHandler<Message> = {},
//    crossinline onEditedChannelPost: TelegramBotUpdateHandler<Message> = {},
//    crossinline onInlineQuery: TelegramBotUpdateHandler<InlineQuery> = {},
//    crossinline onChosenInlineResult: TelegramBotUpdateHandler<ChosenInlineResult> = {},
//    crossinline onCallbackQuery: TelegramBotUpdateHandler<CallbackQuery> = {},
//    crossinline onShippingQuery: TelegramBotUpdateHandler<ShippingQuery> = {},
//    crossinline onPreCheckoutQuery: TelegramBotUpdateHandler<PreCheckoutQuery> = {},
//    crossinline onPoll: TelegramBotUpdateHandler<Poll> = {},
//    crossinline onPollAnswer: TelegramBotUpdateHandler<PollAnswer> = {},
//    crossinline onMyChatMember: TelegramBotUpdateHandler<ChatMemberUpdated> = {},
//    crossinline onChatMember: TelegramBotUpdateHandler<ChatMemberUpdated> = {},
//    crossinline onChatJoinRequest: TelegramBotUpdateHandler<ChatJoinRequest> = {},
//    @Suppress("UNUSED_PARAMETER") noTrailingLambda: Unit = Unit,
//): TelegramBotUpdateListener {
//
//    return object : TelegramBotUpdateListener {
//        context(TelegramBotApiContext) override suspend fun onMessage(message: Message) =
//            onMessage(this@TelegramBotApiContext, message)
//
//        context(TelegramBotApiContext) override suspend fun onEditedMessage(message: Message) =
//            onEditedMessage(this@TelegramBotApiContext, message)
//
//        context(TelegramBotApiContext) override suspend fun onChannelPost(message: Message) =
//            onChannelPost(this@TelegramBotApiContext, message)
//
//        context(TelegramBotApiContext) override suspend fun onEditedChannelPost(message: Message) =
//            onEditedChannelPost(this@TelegramBotApiContext, message)
//
//        context(TelegramBotApiContext) override suspend fun onInlineQuery(inlineQuery: InlineQuery) =
//            onInlineQuery(this@TelegramBotApiContext, inlineQuery)
//
//        context(TelegramBotApiContext) override suspend fun onChosenInlineResult(chosenInlineResult: ChosenInlineResult) =
//            onChosenInlineResult(this@TelegramBotApiContext, chosenInlineResult)
//
//        context(TelegramBotApiContext) override suspend fun onCallbackQuery(callbackQuery: CallbackQuery) =
//            onCallbackQuery(this@TelegramBotApiContext, callbackQuery)
//
//        context(TelegramBotApiContext) override suspend fun onShippingQuery(shippingQuery: ShippingQuery) =
//            onShippingQuery(this@TelegramBotApiContext, shippingQuery)
//
//        context(TelegramBotApiContext) override suspend fun onPreCheckoutQuery(preCheckoutQuery: PreCheckoutQuery) =
//            onPreCheckoutQuery(this@TelegramBotApiContext, preCheckoutQuery)
//
//        context(TelegramBotApiContext) override suspend fun onPoll(poll: Poll) =
//            onPoll(this@TelegramBotApiContext, poll)
//
//        context(TelegramBotApiContext) override suspend fun onPollAnswer(pollAnswer: PollAnswer) =
//            onPollAnswer(this@TelegramBotApiContext, pollAnswer)
//
//        context(TelegramBotApiContext) override suspend fun onMyChatMember(chatMemberUpdated: ChatMemberUpdated) =
//            onMyChatMember(this@TelegramBotApiContext, chatMemberUpdated)
//
//        context(TelegramBotApiContext) override suspend fun onChatMember(chatMemberUpdated: ChatMemberUpdated) =
//            onChatMember(this@TelegramBotApiContext, chatMemberUpdated)
//
//        context(TelegramBotApiContext) override suspend fun onChatJoinRequest(chatJoinRequest: ChatJoinRequest) =
//            onChatJoinRequest(this@TelegramBotApiContext, chatJoinRequest)
//    }
//}
