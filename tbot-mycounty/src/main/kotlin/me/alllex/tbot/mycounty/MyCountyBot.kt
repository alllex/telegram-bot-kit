package me.alllex.tbot.mycounty

import kotlinx.coroutines.*
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.client.TelegramBotApiException
import me.alllex.tbot.api.client.TelegramBotUpdateListener
import me.alllex.tbot.api.model.*
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.newSingleThreadExecutor
import me.alllex.tbot.bot.util.shutdownAndAwaitTermination
import me.alllex.tbot.mycounty.db.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class MyCountyBot(
    db: Db,
    private val api: TelegramBotApiClient,
) : TelegramBotUpdateListener, TelegramBotApiContext, CoroutineScope {

    private val job = SupervisorJob()
    private val coroutineExecutor = newSingleThreadExecutor("bot")
    override val coroutineContext = job +
        coroutineExecutor.asCoroutineDispatcher() +
        CoroutineExceptionHandler { _, t -> onCoroutineError(t) }

    private val userDao = UserDao(db)
    private val counterDao = UserCounterDao(db)
    private val counterUpdateDao = UserCounterUpdateDao(db)
    private val counterUpdateRepository = UserCounterUpdateRepository(counterUpdateDao)
    private val counterRepository = UserCounterRepository(counterDao, counterUpdateRepository)
    private val userRepository = UserRepository(userDao)

    override val botApiClient: TelegramBotApiClient get() = api

    fun start() {
    }

    fun stop() {
        coroutineExecutor.shutdownAndAwaitTermination()
        job.cancel()
    }

    context(TelegramBotApiContext)
    override suspend fun onMessage(message: Message) {
        (this as CoroutineScope).launch {
            val user = getOrCreateUser(message.chat.id)
            try {
                onChatMessageImpl(user, message)
            } catch (e: TelegramBotApiException) {
                log.error("Failed to process message from $user", e)
                message.chat.sendMessage(UserStrings["somethingWentWrong"])
            }
        }
    }

    context(TelegramBotApiContext)
    override suspend fun onCallbackQuery(callbackQuery: CallbackQuery) {
        (this as CoroutineScope).launch {
            // treat user id as chat id, because working with private chats
            val chatId = ChatId(callbackQuery.from.id.value)
            val user = getOrCreateUser(chatId)
            var answered = false
            try {
                onQueryCallbackImpl(user, callbackQuery)
                callbackQuery.answer()
                answered = true
            } catch (e: TelegramBotApiException) {
                callbackQuery.answer(UserStrings["somethingWentWrong"])
                answered = true
            } finally {
                if (!answered) {
                    callbackQuery.answer()
                }
            }
        }
    }

    private suspend fun onChatMessageImpl(botUser: BotUser, msg: Message) {
        log.debug { "Received message from $botUser" }

        // cancellation comes before any state semantics
        if (msg.text.orEmpty().trim() == CommandName.CANCEL.string) {
            onCancelCommand(botUser)
            return
        }

        when (val state = botUser.state) {
            UserChatState.Idle -> onChatMessageInIdle(botUser, msg)
            UserChatState.AwaitNameForNewCounter -> onChatMessageInAwaitNameForNewCounter(botUser, msg)
            is UserChatState.AwaitNewNameForCounter -> onChatMessageInAwaitNewNameForCounter(botUser, msg, state)
        }
    }

    private suspend fun onChatMessageInIdle(botUser: BotUser, msg: Message) {
        val leadCommand = msg.findLeadCommand()
        if (leadCommand == null) {
            onUnknownCommand(botUser, msg)
            return
        }

        val commandArgText = msg.getTextAfter(leadCommand).trim()
        when (msg[leadCommand]) {
            CommandName.START.string -> onStartCommand(botUser)
            CommandName.HELP.string -> onHelpCommand(botUser)
            CommandName.CREATE.string -> onCreateCommand(botUser, commandArgText)
            CommandName.SELECT.string -> onSelectCommand(botUser, commandArgText)
            CommandName.SUMMARY.string -> onSummaryCommand(botUser, commandArgText)
            CommandName.CANCEL.string -> onCancelCommand(botUser)
            else -> onUnknownCommand(botUser, msg)
        }
    }

    private suspend fun onUnknownCommand(botUser: BotUser, msg: Message) {
        val msgText = msg.text?.trim() ?: ""
        val counters = counterRepository.findCountersMatchingName(botUser.userId, msgText)
        if (counters.isEmpty()) {
            msg.reply(UserStrings["unknownCommand"])
            return
        }

        val keyboard = createCountersSelectKeyboard(botUser, counters)
        botUser.chatId.sendMarkdown(UserStrings["selectCountersMatching"].format(msgText), replyMarkup = keyboard)
    }

    private suspend fun onStartCommand(botUser: BotUser) {
        botUser.chatId.sendMarkdown(UserStrings["startGreeting"])
    }

    private suspend fun onHelpCommand(botUser: BotUser) {
        botUser.chatId.sendMarkdown(UserStrings["userHelp"])
    }

    private suspend fun onSelectCommand(botUser: BotUser, counterName: String) {
        if (counterName.isNotEmpty()) {
            val counter = counterRepository.findCounterByName(botUser.userId, counterName)
            if (counter != null) {
                doSelectCounter(botUser, counter)
                return
            }
        }

        val counters = counterRepository.getCounters(botUser.userId)
        val keyboard = createCountersSelectKeyboard(botUser, counters)

        botUser.chatId.sendMarkdown(UserStrings["selectCounters"], replyMarkup = keyboard)
    }

    private fun createCountersSelectKeyboard(botUser: BotUser, counters: List<UserCounter>) = inlineKeyboard {
        rowsChunked(2) {
            for (counter in counters) {
                val data = "${botUser.userId.value}/${QueryCallbackName.SELECT}/${counter.id}"
                button(text = counter.name, callbackData = data)
            }
        }
    }

    private suspend fun onChatMessageInAwaitNameForNewCounter(botUser: BotUser, msg: Message) {
        val text = msg.text.orEmpty().trim()
        onCreateCounterWithName(botUser, text)
    }

    private suspend fun onCreateCounterWithName(botUser: BotUser, newCounterName: String) {
        if (newCounterName.isEmpty()) {
            botUser.chatId.sendMarkdown(UserStrings["createCounter"])
            botUser.state = UserChatState.AwaitNameForNewCounter
            return
        } else if (newCounterName.length > 100) {
            botUser.chatId.sendMarkdown(UserStrings["counterNameTooLong"])
            botUser.state = UserChatState.AwaitNameForNewCounter
            return
        }

        val existingCounter = counterRepository.findCounterByName(botUser.userId, newCounterName)
        if (existingCounter != null) {
            botUser.chatId.sendMarkdown(UserStrings["counterAlreadyExists"].format(existingCounter.name))
            botUser.state = UserChatState.AwaitNameForNewCounter
            return
        }

        botUser.state = UserChatState.Idle

        val counter = counterRepository.addCounter(botUser.userId, newCounterName)
        val responseText = UserStrings["createdCounter"].format(counter.name)
        botUser.chatId.sendMarkdown(responseText)

        doSelectCounter(botUser, counter)
    }

    private suspend fun onChatMessageInAwaitNewNameForCounter(
        botUser: BotUser,
        msg: Message,
        state: UserChatState.AwaitNewNameForCounter
    ) {
        val counterId = state.counterId
        val counter = counterRepository.getCounterOrNull(counterId)
        if (counter == null || counter.userId != botUser.userId) {
            log.error("Renaming invalid counter: $counterId")
            api.sendMessage(botUser.chatId, UserStrings["somethingWentWrong"])
            return
        }

        val text = msg.text.orEmpty().trim()
        if (text.isEmpty()) {
            msg.chat.sendMarkdown(UserStrings["renameCounter"])
            return
        } else if (text.length > 100) {
            msg.chat.sendMarkdown(UserStrings["counterNameTooLong"])
            return
        }

        botUser.state = UserChatState.Idle

        counter.setName(text)

        val responseText = UserStrings["renamedCounter"].format(counter.name)
        msg.chat.sendMarkdown(responseText)
    }

    private suspend fun onSummaryCommand(botUser: BotUser, counterName: String) {
        if (counterName.isNotEmpty()) {
            val counter = counterRepository.findCounterByName(botUser.userId, counterName)
            if (counter != null) {
                botUser.chatId.sendMarkdown("`${counter.name}` = `${counter.getCountValue()}`")
                return
            }
        }

        val counters = counterRepository.getCounters(botUser.userId)
        if (counters.isEmpty()) {
            botUser.chatId.sendMarkdown(UserStrings["summaryNoCounters"])
            return
        }

        val summaryText = counters.joinToString("\n") {
            "`${it.name}` = `${it.getCountValue()}`"
        }
        botUser.chatId.sendMarkdown("Summary of ${counters.size} counters:\n$summaryText")
    }

    private suspend fun onCancelCommand(botUser: BotUser) {
        transitionToIdle(botUser)
    }

    /**
     * Returns true if answered to the callback
     */
    private suspend fun onQueryCallbackImpl(botUser: BotUser, query: CallbackQuery) {
        val data = query.data ?: return
        val dataParts = data.split("/")
        if (dataParts.size !in 2..3) {
            log.error("Unexpected callback structure: $data")
            return
        }

        val userId = UserId(dataParts[0].toLong())
        if (botUser.userId != userId) return

        val callbackNameStr = dataParts[1]
        val callbackName = QueryCallbackName.parse(callbackNameStr)
        if (callbackName == null) {
            log.error("Unexpected callback name: $callbackNameStr")
            return
        }

        return when (dataParts.size) {
            2 -> {
                onCommonQueryCallback(botUser, callbackName)
            }

            3 -> {
                val counterId = dataParts[2].toLongOrNull()
                if (counterId == null) {
                    log.error("Bad counterId in callback: $data")
                    return
                }

                val counter = counterRepository.getCounterOrNull(counterId)
                if (counter == null || counter.userId != botUser.userId) {
                    log.error("[user@${botUser.userId.value}] No such counter: $counterId")
                    return
                }

                onCounterQueryCallback(botUser, query, callbackName, counter)
            }

            else -> error("unexpected size: ${dataParts.size}")
        }
    }

    private suspend fun onCommonQueryCallback(botUser: BotUser, callbackName: QueryCallbackName) {
        return when (callbackName) {
            QueryCallbackName.CREATE -> onCreateCommand(botUser)
            else -> error("not a common callback: $callbackName")
        }
    }

    private suspend fun onCounterQueryCallback(
        botUser: BotUser,
        query: CallbackQuery,
        callbackName: QueryCallbackName,
        counter: UserCounter
    ) {
        return when (callbackName) {
            QueryCallbackName.SELECT -> onSelectCounterCallback(botUser, query, counter, editQueryMessage = true)
            QueryCallbackName.SELECT_SINGLE -> onSelectCounterCallback(botUser, query, counter, editQueryMessage = false)
            QueryCallbackName.INC -> onIncCounterCallback(botUser, query, counter)
            QueryCallbackName.UNDO -> onUndoCounterCallback(botUser, query, counter)
            QueryCallbackName.UNDO_CONFIRM -> onUndoConfirmCounterCallback(botUser, query, counter)
            QueryCallbackName.UNDO_CANCEL -> onUndoCancelCounterCallback(botUser, query, counter)
            QueryCallbackName.RENAME -> onRenameCounterCallback(botUser, query, counter)
            QueryCallbackName.DELETE -> onDeleteCounterCallback(botUser, query, counter)
            QueryCallbackName.DELETE_CONFIRM -> onDeleteConfirmCounterCallback(botUser, query, counter)
            QueryCallbackName.DELETE_CANCEL -> onDeleteCancelCounterCallback(botUser, query, counter)
            else -> error("not a counter callback: $callbackName")
        }
    }

    private suspend fun onSelectCounterCallback(
        botUser: BotUser,
        query: CallbackQuery,
        counter: UserCounter,
        editQueryMessage: Boolean
    ) {
        val queryMessage = query.message
        if (queryMessage == null) {
            log.error("Missing query message: $query")
            return
        }

        val keyboard = createSelectedCounterKeyboard(botUser, counter)
        val replyText = UserStrings["selectedCounter"].format(counter.name)
        if (editQueryMessage) {
            queryMessage.editTextMarkdown(replyText, replyMarkup = keyboard)
        } else {
            botUser.chatId.sendMarkdown(replyText, replyMarkup = keyboard)
        }
    }

    private suspend fun doSelectCounter(botUser: BotUser, counter: UserCounter) {
        val keyboard = createSelectedCounterKeyboard(botUser, counter)
        botUser.chatId.sendMarkdown(UserStrings["selectedCounter"].format(counter.name), replyMarkup = keyboard)
    }

    private fun createSelectedCounterKeyboard(botUser: BotUser, counter: UserCounter): InlineKeyboardMarkup {
        return inlineKeyboard {
            button("Inc", "${botUser.userId.value}/${QueryCallbackName.INC}/${counter.id}")
            button("Drop Last", "${botUser.userId.value}/${QueryCallbackName.UNDO}/${counter.id}")
            row {
                button("Rename", "${botUser.userId.value}/${QueryCallbackName.RENAME}/${counter.id}")
                button("Delete", "${botUser.userId.value}/${QueryCallbackName.DELETE}/${counter.id}")
            }
        }
    }

    private suspend fun onIncCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        doIncCounter(botUser, counter)
        query.message?.deleteLater()
    }

    private suspend fun onUndoCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        val queryMessage = query.message
        if (queryMessage == null) {
            log.error("Missing query message: $query")
            return
        }

        queryMessage.editTextMarkdown(
            text = UserStrings["askCounterUndoConfirmation"].format(counter.name),
            replyMarkup = inlineKeyboard {
                row {
                    button("Remove", "${botUser.userId.value}/${QueryCallbackName.UNDO_CONFIRM}/${counter.id}")
                    button("Cancel", "${botUser.userId.value}/${QueryCallbackName.UNDO_CANCEL}/${counter.id}")
                }
            }
        )
    }

    private suspend fun onUndoConfirmCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        if (counter.userId != botUser.userId) {
            log.error("Trying to undo invalid counter: ${counter.id}")
            api.sendMessage(botUser.chatId, UserStrings["somethingWentWrong"])
            return
        }

        val newCount = counter.removeLast()
        botUser.chatId.sendMarkdown(UserStrings["undidLast"].format(counter.name, newCount))

        query.message?.deleteLater()
    }

    private suspend fun onUndoCancelCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        onSelectCounterCallback(botUser, query, counter, editQueryMessage = true)
    }

    private suspend fun doIncCounter(botUser: BotUser, counter: UserCounter) {
        val now = Instant.now()
        val time = ZonedDateTime.ofInstant(now, botUser.timezone)
        val newValue = counter.register(time)

        val replyText = UserStrings["incrementedCounter"].format(counter.name, newValue)
        botUser.chatId.sendMarkdown(replyText, replyMarkup = inlineKeyboard {
            button("Select", "${botUser.userId.value}/${QueryCallbackName.SELECT_SINGLE}/${counter.id}")
        })

        transitionToIdle(botUser)
    }

    private suspend fun onRenameCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        botUser.state = UserChatState.AwaitNewNameForCounter(counter.id)
        botUser.chatId.sendMarkdown(UserStrings["renameCounter"].format(counter.name))
        query.message?.deleteLater()
    }

    private suspend fun onDeleteCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        val queryMessage = query.message
        if (queryMessage == null) {
            log.error("Missing query message: $query")
            return
        }

        queryMessage.editTextMarkdown(
            text = UserStrings["askDeleteConfirmation"].format(counter.name),
            replyMarkup = inlineKeyboard {
                row {
                    button("Delete", "${botUser.userId.value}/${QueryCallbackName.DELETE_CONFIRM}/${counter.id}")
                    button("Cancel", "${botUser.userId.value}/${QueryCallbackName.DELETE_CANCEL}/${counter.id}")
                }
            }
        )
    }

    private suspend fun onDeleteConfirmCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        if (counter.userId != botUser.userId) {
            log.error("Trying to delete invalid counter: ${counter.id}")
            api.sendMessage(botUser.chatId, UserStrings["somethingWentWrong"])
            return
        }

        counterRepository.removeCounter(counter.id)
        botUser.chatId.sendMarkdown(UserStrings["deletedCounter"].format(counter.name))

        query.message?.deleteLater()
    }

    private suspend fun onDeleteCancelCounterCallback(botUser: BotUser, query: CallbackQuery, counter: UserCounter) {
        onSelectCounterCallback(botUser, query, counter, editQueryMessage = true)
    }

    private suspend fun transitionToIdle(botUser: BotUser) {
        botUser.state = UserChatState.Idle
        botUser.chatId.sendMarkdown(UserStrings["readyToCount"])
    }

    private fun Message.deleteLater() {
        launch {
            delete()
        }
    }

    private suspend fun onCreateCommand(botUser: BotUser, newCounterName: String = "") {
        if (newCounterName.isNotEmpty()) {
            onCreateCounterWithName(botUser, newCounterName)
            return
        }

        botUser.state = UserChatState.AwaitNameForNewCounter
        botUser.chatId.sendMarkdown(UserStrings["createCounter"])
    }

    private suspend fun getOrCreateUser(chatId: ChatId): BotUser {
        return userRepository.getOrCreateUser(chatId, DEFAULT_ZONE_ID)
    }

    companion object {

        private val log = loggerForClass<MyCountyBot>()

        private val DEFAULT_ZONE_ID = ZoneId.of("UTC")

        private fun onCoroutineError(t: Throwable) {
            log.error("Unexpected coroutine error: ", t)
        }
    }
}
