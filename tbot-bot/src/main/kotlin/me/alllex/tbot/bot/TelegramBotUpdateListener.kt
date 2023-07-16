package me.alllex.tbot.bot

import me.alllex.tbot.api.client.TelegramBotApiContext
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
    suspend fun onMessage(message: Message) {}

    context(TelegramBotApiContext)
    suspend fun onEditedMessage(message: Message) {}

    context(TelegramBotApiContext)
    suspend fun onChannelPost(message: Message) {}

    context(TelegramBotApiContext)
    suspend fun onEditedChannelPost(message: Message) {}

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
}
