package me.alllex.tbot.bot.util.log

import org.apache.logging.log4j.LogManager

private typealias Log4JLogger = org.apache.logging.log4j.Logger

class Logger private constructor(
    private val wrappedLogger: Log4JLogger
) {

    val name get() = wrappedLogger.name ?: ""

    val isDebugEnabled get() = wrappedLogger.isDebugEnabled

    inline fun trace(lazyMsg: () -> String) {
        if (isDebugEnabled) nonLazyTrace(lazyMsg())
    }

    inline fun debug(lazyMsg: () -> String) {
        if (isDebugEnabled) nonLazyDebug(lazyMsg())
    }

    fun info(msg: String) = wrappedLogger.info(msg)

    fun warn(msg: String) = wrappedLogger.warn(msg)

    fun error(msg: String, t: Throwable? = null) =
        if (t == null) wrappedLogger.error(msg) else wrappedLogger.error(msg, t)

    fun fatal(msg: String, t: Throwable? = null) =
        if (t == null) wrappedLogger.fatal(msg) else wrappedLogger.fatal(msg, t)

    @PublishedApi
    internal fun nonLazyDebug(msg: String) = wrappedLogger.debug(msg)

    @PublishedApi
    internal fun nonLazyTrace(msg: String) = wrappedLogger.trace(msg)

    companion object {

        inline fun <reified T> getForClass() = getForClass(T::class.java)

        @PublishedApi
        internal fun getForClass(javaClass: Class<*>) =
            Logger(LogManager.getLogger(javaClass))

    }

}

inline fun <reified T> loggerForClass(): Logger = Logger.getForClass<T>()

