package me.alllex.tbot.api.dsl

import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.client.TelegramBotUpdateListener
import me.alllex.tbot.api.client.startPolling
import me.alllex.tbot.api.model.CallbackQuery
import me.alllex.tbot.api.model.ChatJoinRequest
import me.alllex.tbot.api.model.ChatMemberUpdated
import me.alllex.tbot.api.model.ChosenInlineResult
import me.alllex.tbot.api.model.InlineQuery
import me.alllex.tbot.api.model.Message
import me.alllex.tbot.api.model.Poll
import me.alllex.tbot.api.model.PollAnswer
import me.alllex.tbot.api.model.PreCheckoutQuery
import me.alllex.tbot.api.model.ShippingQuery
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TelegramBotUpdateBuilder {

    private var onMessage: (suspend context(TelegramBotApiContext) (Message) -> Unit)? = null
    private var onEditedMessage: (suspend context(TelegramBotApiContext)  (Message) -> Unit)? = null
    private var onChannelPost: (suspend context(TelegramBotApiContext)  (Message) -> Unit)? = null
    private var onEditedChannelPost: (suspend context(TelegramBotApiContext)  (Message) -> Unit)? = null
    private var onInlineQuery: (suspend context(TelegramBotApiContext)  (InlineQuery) -> Unit)? = null
    private var onChosenInlineResult: (suspend context(TelegramBotApiContext)  (ChosenInlineResult) -> Unit)? = null
    private var onCallbackQuery: (suspend context(TelegramBotApiContext)  (CallbackQuery) -> Unit)? = null
    private var onShippingQuery: (suspend context(TelegramBotApiContext)  (ShippingQuery) -> Unit)? = null
    private var onPreCheckoutQuery: (suspend context(TelegramBotApiContext)  (PreCheckoutQuery) -> Unit)? = null
    private var onPoll: (suspend context(TelegramBotApiContext)  (Poll) -> Unit)? = null
    private var onPollAnswer: (suspend context(TelegramBotApiContext)  (PollAnswer) -> Unit)? = null
    private var onMyChatMember: (suspend context(TelegramBotApiContext)  (ChatMemberUpdated) -> Unit)? = null
    private var onChatMember: (suspend context(TelegramBotApiContext)  (ChatMemberUpdated) -> Unit)? = null
    private var onChatJoinRequest: (suspend context(TelegramBotApiContext)  (ChatJoinRequest) -> Unit)? = null

    fun onMessage(action: suspend context(TelegramBotApiContext)  (Message) -> Unit) {
        onMessage = action
    }

    fun onEditedMessage(action: suspend context(TelegramBotApiContext)  (Message) -> Unit) {
        onEditedMessage = action
    }

    fun onChannelPost(action: suspend context(TelegramBotApiContext)  (Message) -> Unit) {
        onChannelPost = action
    }

    fun onEditedChannelPost(action: suspend context(TelegramBotApiContext)  (Message) -> Unit) {
        onEditedMessage = action
    }

    fun onInlineQuery(action: suspend context(TelegramBotApiContext)  (InlineQuery) -> Unit) {
        onInlineQuery = action
    }

    fun onChosenInlineResult(action: suspend context(TelegramBotApiContext)  (ChosenInlineResult) -> Unit) {
        onChosenInlineResult = action
    }

    fun onCallbackQuery(action: suspend context(TelegramBotApiContext)  (CallbackQuery) -> Unit) {
        onCallbackQuery = action
    }

    fun onShippingQuery(action: suspend context(TelegramBotApiContext)  (ShippingQuery) -> Unit) {
        onShippingQuery = action
    }

    fun onPreCheckoutQuery(action: suspend context(TelegramBotApiContext)  (PreCheckoutQuery) -> Unit) {
        onPreCheckoutQuery = action
    }

    fun onPoll(action: suspend context(TelegramBotApiContext)  (Poll) -> Unit) {
        onPoll = action
    }

    fun onPollAnswer(action: suspend context(TelegramBotApiContext)  (PollAnswer) -> Unit) {
        onPollAnswer = action
    }

    fun onMyChatMember(action: suspend context(TelegramBotApiContext)  (ChatMemberUpdated) -> Unit) {
        onMyChatMember = action
    }

    fun onChatMember(action: suspend context(TelegramBotApiContext)  (ChatMemberUpdated) -> Unit) {
        onChatMember = action
    }

    fun onChatJoinRequest(action: suspend context(TelegramBotApiContext)  (ChatJoinRequest) -> Unit) {
        onChatJoinRequest = action
    }

    fun build() = TelegramBotUpdateListener(
        onMessage = onMessage,
        onEditedMessage = onEditedMessage,
        onChannelPost = onChannelPost,
        onEditedChannelPost = onEditedChannelPost,
        onInlineQuery = onInlineQuery,
        onChosenInlineResult = onChosenInlineResult,
        onCallbackQuery = onCallbackQuery,
        onShippingQuery = onShippingQuery,
        onPreCheckoutQuery = onPreCheckoutQuery,
        onPoll = onPoll,
        onPollAnswer = onPollAnswer,
        onMyChatMember = onMyChatMember,
        onChatMember = onChatMember,
        onChatJoinRequest = onChatJoinRequest
    )
}

suspend fun TelegramBotApiClient.startPolling(
    pollingTimeout: Duration = 0.seconds,
    startingUpdateOffset: Long = 0L,
    logger: Logger = LoggerFactory.getLogger("TelegramBotApiPoller"),
    onUpdateOffset: suspend (Long) -> Unit = {},
    listener: TelegramBotUpdateBuilder.() -> Unit
) = startPolling(
    listener = TelegramBotUpdateBuilder().apply(listener).build(),
    onUpdateOffsetChanged = onUpdateOffset,
    pollingTimeout = pollingTimeout,
    startingUpdateOffset = startingUpdateOffset,
    logger = logger
)
