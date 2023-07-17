package me.alllex.tbot.mycounty

import kotlinx.coroutines.*
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.bot.TelegramBotUpdateListener
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
            } catch (e: BadRequestException) {
                val text = e.userFriendlyMessage ?: "Something went wrong..."
                message.chat.sendMessage(text)
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
            } catch (e: BadRequestException) {
                val text = e.userFriendlyMessage ?: UserStrings["somethingWentWrong"]
                callbackQuery.answer(text)
                answered = true
            } finally {
                if (!answered) {
                    callbackQuery.answer()
                }
            }
        }
    }

    private suspend fun onChatMessageImpl(user: User, msg: Message) {
        log.debug { "Received message from $user" }

        // cancellation comes before any state semantics
        if (msg.text.orEmpty().trim() == CommandName.CANCEL.string) {
            onCancelCommand(user)
            return
        }

        when (val state = user.state) {
            UserChatState.Idle -> onChatMessageInIdle(user, msg)
            UserChatState.AwaitNameForNewCounter -> onChatMessageInAwaitNameForNewCounter(user, msg)
            is UserChatState.AwaitNewNameForCounter -> onChatMessageInAwaitNewNameForCounter(user, msg, state)
        }
    }

    private suspend fun onChatMessageInIdle(user: User, msg: Message) {
        val leadCommand = msg.findLeadCommand()
        if (leadCommand == null) {
            onUnknownCommand(user, msg)
            return
        }

        val commandArgText = msg.getTextAfter(leadCommand).trim()
        when (msg[leadCommand]) {
            CommandName.START.string -> onStartCommand(user)
            CommandName.HELP.string -> onHelpCommand(user)
            CommandName.CREATE.string -> onCreateCommand(user, commandArgText)
            CommandName.SELECT.string -> onSelectCommand(user, commandArgText)
            CommandName.SUMMARY.string -> onSummaryCommand(user, commandArgText)
            CommandName.CANCEL.string -> onCancelCommand(user)
            else -> onUnknownCommand(user, msg)
        }
    }

    private suspend fun onUnknownCommand(user: User, msg: Message) {
        val msgText = msg.text?.trim() ?: ""
        val counters = counterRepository.findCountersMatchingName(user.userId, msgText)
        if (counters.isEmpty()) {
            msg.reply(UserStrings["unknownCommand"])
            return
        }

        val keyboard = createCountersSelectKeyboard(user, counters)
        api.sendMarkdown(user.chatId, UserStrings["selectCountersMatching"].format(msgText), replyMarkup = keyboard)
    }

    private suspend fun onStartCommand(user: User) {
        api.sendMarkdown(user.chatId, UserStrings["startGreeting"])
    }

    private suspend fun onHelpCommand(user: User) {
        api.sendMarkdown(user.chatId, UserStrings["userHelp"])
    }

    private suspend fun onSelectCommand(user: User, counterName: String) {
        if (counterName.isNotEmpty()) {
            val counter = counterRepository.findCounterByName(user.userId, counterName)
            if (counter != null) {
                doSelectCounter(user, counter)
                return
            }
        }

        val counters = counterRepository.getCounters(user.userId)
        val keyboard = createCountersSelectKeyboard(user, counters)

        api.sendMarkdown(user.chatId, UserStrings["selectCounters"], replyMarkup = keyboard)
    }

    private fun createCountersSelectKeyboard(user: User, counters: List<UserCounter>) = inlineKeyboard {
        rowsChunked(2) {
            for (counter in counters) {
                val data = "${user.userId.value}/${QueryCallbackName.SELECT}/${counter.id}"
                button(text = counter.name, callbackData = data)
            }
        }
    }

    private suspend fun onChatMessageInAwaitNameForNewCounter(user: User, msg: Message) {
        val text = msg.text.orEmpty().trim()
        onCreateCounterWithName(user, text)
    }

    private suspend fun onCreateCounterWithName(user: User, newCounterName: String) {
        if (newCounterName.isEmpty()) {
            api.sendMarkdown(user.chatId, UserStrings["createCounter"])
            user.state = UserChatState.AwaitNameForNewCounter
            return
        } else if (newCounterName.length > 100) {
            api.sendMarkdown(user.chatId, UserStrings["counterNameTooLong"])
            user.state = UserChatState.AwaitNameForNewCounter
            return
        }

        val existingCounter = counterRepository.findCounterByName(user.userId, newCounterName)
        if (existingCounter != null) {
            api.sendMarkdown(user.chatId, UserStrings["counterAlreadyExists"].format(existingCounter.name))
            user.state = UserChatState.AwaitNameForNewCounter
            return
        }

        user.state = UserChatState.Idle

        val counter = counterRepository.addCounter(user.userId, newCounterName)
        val responseText = UserStrings["createdCounter"].format(counter.name)
        api.sendMarkdown(user.chatId, responseText)

        doSelectCounter(user, counter)
    }

    private suspend fun onChatMessageInAwaitNewNameForCounter(
        user: User,
        msg: Message,
        state: UserChatState.AwaitNewNameForCounter
    ) {
        val counterId = state.counterId
        val counter = counterRepository.getCounterOrNull(counterId)
        if (counter == null || counter.userId != user.userId) {
            log.error("Renaming invalid counter: $counterId")
            api.sendMessage(user.chatId, UserStrings["somethingWentWrong"])
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

        user.state = UserChatState.Idle

        counter.setName(text)

        val responseText = UserStrings["renamedCounter"].format(counter.name)
        msg.chat.sendMarkdown(responseText)
    }

    private suspend fun onSummaryCommand(user: User, counterName: String) {
        if (counterName.isNotEmpty()) {
            val counter = counterRepository.findCounterByName(user.userId, counterName)
            if (counter != null) {
                api.sendMarkdown(user.chatId, "`${counter.name}` = `${counter.getCountValue()}`")
                return
            }
        }

        val counters = counterRepository.getCounters(user.userId)
        if (counters.isEmpty()) {
            api.sendMarkdown(user.chatId, UserStrings["summaryNoCounters"])
            return
        }

        val summaryText = counters.joinToString("\n") {
            "`${it.name}` = `${it.getCountValue()}`"
        }
        api.sendMarkdown(user.chatId, "Summary of ${counters.size} counters:\n$summaryText")
    }

    private suspend fun onCancelCommand(user: User) {
        transitionToIdle(user)
    }

    /**
     * Returns true if answered to the callback
     */
    private suspend fun onQueryCallbackImpl(user: User, query: CallbackQuery) {
        val data = query.data ?: return
        val dataParts = data.split("/")
        if (dataParts.size !in 2..3) {
            log.error("Unexpected callback structure: $data")
            return
        }

        val userId = UserId(dataParts[0].toLong())
        if (user.userId != userId) return

        val callbackNameStr = dataParts[1]
        val callbackName = QueryCallbackName.parse(callbackNameStr)
        if (callbackName == null) {
            log.error("Unexpected callback name: $callbackNameStr")
            return
        }

        return when (dataParts.size) {
            2 -> {
                onCommonQueryCallback(user, callbackName)
            }

            3 -> {
                val counterId = dataParts[2].toLongOrNull()
                if (counterId == null) {
                    log.error("Bad counterId in callback: $data")
                    return
                }

                val counter = counterRepository.getCounterOrNull(counterId)
                if (counter == null || counter.userId != user.userId) {
                    log.error("[user@${user.userId.value}] No such counter: $counterId")
                    return
                }

                onCounterQueryCallback(user, query, callbackName, counter)
            }

            else -> error("unexpected size: ${dataParts.size}")
        }
    }

    private suspend fun onCommonQueryCallback(user: User, callbackName: QueryCallbackName) {
        return when (callbackName) {
            QueryCallbackName.CREATE -> onCreateCommand(user)
            else -> error("not a common callback: $callbackName")
        }
    }

    private suspend fun onCounterQueryCallback(
        user: User,
        query: CallbackQuery,
        callbackName: QueryCallbackName,
        counter: UserCounter
    ) {
        return when (callbackName) {
            QueryCallbackName.SELECT -> onSelectCounterCallback(user, query, counter, editQueryMessage = true)
            QueryCallbackName.SELECT_SINGLE -> onSelectCounterCallback(user, query, counter, editQueryMessage = false)
            QueryCallbackName.INC -> onIncCounterCallback(user, query, counter)
            QueryCallbackName.UNDO -> onUndoCounterCallback(user, query, counter)
            QueryCallbackName.UNDO_CONFIRM -> onUndoConfirmCounterCallback(user, query, counter)
            QueryCallbackName.UNDO_CANCEL -> onUndoCancelCounterCallback(user, query, counter)
            QueryCallbackName.RENAME -> onRenameCounterCallback(user, query, counter)
            QueryCallbackName.DELETE -> onDeleteCounterCallback(user, query, counter)
            QueryCallbackName.DELETE_CONFIRM -> onDeleteConfirmCounterCallback(user, query, counter)
            QueryCallbackName.DELETE_CANCEL -> onDeleteCancelCounterCallback(user, query, counter)
            else -> error("not a counter callback: $callbackName")
        }
    }

    private suspend fun onSelectCounterCallback(
        user: User,
        query: CallbackQuery,
        counter: UserCounter,
        editQueryMessage: Boolean
    ) {
        val queryMessage = query.message
        if (queryMessage == null) {
            log.error("Missing query message: $query")
            return
        }

        val keyboard = createSelectedCounterKeyboard(user, counter)
        val replyText = UserStrings["selectedCounter"].format(counter.name)
        if (editQueryMessage) {
            queryMessage.editMarkdown(replyText, replyMarkup = keyboard)
        } else {
            api.sendMarkdown(user.chatId, replyText, replyMarkup = keyboard)
        }
    }

    private suspend fun doSelectCounter(user: User, counter: UserCounter) {
        val keyboard = createSelectedCounterKeyboard(user, counter)
        api.sendMarkdown(user.chatId, UserStrings["selectedCounter"].format(counter.name), replyMarkup = keyboard)
    }

    private fun createSelectedCounterKeyboard(user: User, counter: UserCounter): InlineKeyboardMarkup {
        return inlineKeyboard {
            button("Inc", "${user.userId.value}/${QueryCallbackName.INC}/${counter.id}")
            button("Drop Last", "${user.userId.value}/${QueryCallbackName.UNDO}/${counter.id}")
            row {
                button("Rename", "${user.userId.value}/${QueryCallbackName.RENAME}/${counter.id}")
                button("Delete", "${user.userId.value}/${QueryCallbackName.DELETE}/${counter.id}")
            }
        }
    }

    private suspend fun onIncCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        doIncCounter(user, counter)
        query.message?.deleteLater()
    }

    private suspend fun onUndoCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        val queryMessage = query.message
        if (queryMessage == null) {
            log.error("Missing query message: $query")
            return
        }

        queryMessage.editMarkdown(
            text = UserStrings["askCounterUndoConfirmation"].format(counter.name),
            replyMarkup = inlineKeyboard {
                row {
                    button("Remove", "${user.userId.value}/${QueryCallbackName.UNDO_CONFIRM}/${counter.id}")
                    button("Cancel", "${user.userId.value}/${QueryCallbackName.UNDO_CANCEL}/${counter.id}")
                }
            }
        )
    }

    private suspend fun onUndoConfirmCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        if (counter.userId != user.userId) {
            log.error("Trying to undo invalid counter: ${counter.id}")
            api.sendMessage(user.chatId, UserStrings["somethingWentWrong"])
            return
        }

        val newCount = counter.removeLast()
        api.sendMarkdown(user.chatId, UserStrings["undidLast"].format(counter.name, newCount))

        query.message?.deleteLater()
    }

    private suspend fun onUndoCancelCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        onSelectCounterCallback(user, query, counter, editQueryMessage = true)
    }

    private suspend fun doIncCounter(user: User, counter: UserCounter) {
        val now = Instant.now()
        val time = ZonedDateTime.ofInstant(now, user.timezone)
        val newValue = counter.register(time)

        val replyText = UserStrings["incrementedCounter"].format(counter.name, newValue)
        api.sendMarkdown(user.chatId, replyText, replyMarkup = inlineKeyboard {
            button("Select", "${user.userId.value}/${QueryCallbackName.SELECT_SINGLE}/${counter.id}")
        })

        transitionToIdle(user)
    }

    private suspend fun onRenameCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        user.state = UserChatState.AwaitNewNameForCounter(counter.id)
        api.sendMarkdown(user.chatId, UserStrings["renameCounter"].format(counter.name))
        query.message?.deleteLater()
    }

    private suspend fun onDeleteCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        val queryMessage = query.message
        if (queryMessage == null) {
            log.error("Missing query message: $query")
            return
        }

        queryMessage.editMarkdown(
            text = UserStrings["askDeleteConfirmation"].format(counter.name),
            replyMarkup = inlineKeyboard {
                row {
                    button("Delete", "${user.userId.value}/${QueryCallbackName.DELETE_CONFIRM}/${counter.id}")
                    button("Cancel", "${user.userId.value}/${QueryCallbackName.DELETE_CANCEL}/${counter.id}")
                }
            }
        )
    }

    private suspend fun onDeleteConfirmCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        if (counter.userId != user.userId) {
            log.error("Trying to delete invalid counter: ${counter.id}")
            api.sendMessage(user.chatId, UserStrings["somethingWentWrong"])
            return
        }

        counterRepository.removeCounter(counter.id)
        api.sendMarkdown(user.chatId, UserStrings["deletedCounter"].format(counter.name))

        query.message?.deleteLater()
    }

    private suspend fun onDeleteCancelCounterCallback(user: User, query: CallbackQuery, counter: UserCounter) {
        onSelectCounterCallback(user, query, counter, editQueryMessage = true)
    }

    private suspend fun transitionToIdle(user: User) {
        user.state = UserChatState.Idle
        api.sendMarkdown(user.chatId, UserStrings["readyToCount"])
    }

    private fun Message.deleteLater() {
        launch {
            delete()
        }
    }

    private suspend fun onCreateCommand(user: User, newCounterName: String = "") {
        if (newCounterName.isNotEmpty()) {
            onCreateCounterWithName(user, newCounterName)
            return
        }

        user.state = UserChatState.AwaitNameForNewCounter
        api.sendMarkdown(user.chatId, UserStrings["createCounter"])
    }

    private suspend fun getOrCreateUser(chatId: ChatId): User {
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
