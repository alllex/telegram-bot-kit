package me.alllex.tbot.bot.util.log

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext


class LoggingCoroutineExceptionHandler : CoroutineExceptionHandler {

    override val key = CoroutineExceptionHandler.Key

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        log.error("Unhandled coroutine exception in context: $context", exception)
    }

    companion object {
        private val log = loggerForClass<LoggingCoroutineExceptionHandler>()
    }

}
