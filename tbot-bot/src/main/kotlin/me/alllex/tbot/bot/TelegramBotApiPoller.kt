package me.alllex.tbot.bot

import kotlinx.coroutines.*
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.model.Seconds
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.newSingleThreadExecutor
import me.alllex.tbot.bot.util.shutdownAndAwaitTermination
import me.alllex.tbot.api.model.Update
import me.alllex.tbot.api.model.getUpdates
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


class TelegramBotApiPoller(
    private val client: TelegramBotApiClient,
    private val pollingTimeout: Duration = 10.seconds
) : CoroutineScope {

    private val started = AtomicBoolean(false)
    private val updateOffset = AtomicLong(0)

    private val pollerJob = SupervisorJob()
    private val pollerExecutor = newSingleThreadExecutor("tbot-polling")
    override val coroutineContext = pollerJob +
        pollerExecutor.asCoroutineDispatcher() +
        CoroutineExceptionHandler { _, t -> onCoroutineError(t) }

    private val botApiContext = object : TelegramBotApiContext {
        override val botApiClient: TelegramBotApiClient get() = client
    }

    fun start(listener: TelegramBotUpdateListener) {
        check(started.compareAndSet(false, true)) { "Already started" }
        launch { runPollingForever(listener) }
        log.info("Started")
    }

    fun stop() {
        runBlocking {
            withTimeout(500.milliseconds) {
                pollerJob.cancelAndJoin()
                log.info("Polling job stopped successfully")
            }
        }
        pollerExecutor.shutdownAndAwaitTermination()
        log.info("Stopped")
    }

    private suspend fun runPollingForever(listener: TelegramBotUpdateListener) {
        while (isActive) {
            runPollingIteration(listener)
        }
    }

    private suspend fun runPollingIteration(listener: TelegramBotUpdateListener) {
        val updates = fetchUpdatesSafelyRetryingForever() ?: return
        log.debug { "Received ${updates.size} updates" }

        for (update in updates) {
            if (!isActive) break

            try {
                with(botApiContext) {
                    listener.onUpdate(update)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                log.error("Failed to process update $update", e)
            }

            val updateId = update.updateId
            updateOffset.updateAndGet { maxOf(it, updateId + 1) }
        }
    }

    private suspend fun fetchUpdatesSafelyRetryingForever(): List<Update>? {
        // happy path
        fetchUpdatesSafely()?.let { return it }

        // 1, 2, 4, 8, 10, 10, ...
        val retryDelaySeconds = generateSequence(1) { it * 2 }.map { it.coerceAtMost(10) }

        for (delaySeconds in retryDelaySeconds) {
            if (!isActive) break

            fetchUpdatesSafely()?.let { return it }

            log.debug { "Retrying update fetching in $delaySeconds seconds" }
            delay(delaySeconds * 1000L)
        }

        // Should not get here
        return null
    }

    /**
     * Returns fetched updates or null if an error occurred.
     */
    private suspend fun fetchUpdatesSafely(): List<Update>? {
        return try {
            fetchUpdates()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            log.error("Failed to get updates due to unexpected error:", e)
            null
        }
    }

    private suspend fun fetchUpdates(): List<Update>? {
        val updateOffsetValue = updateOffset.get()
        log.debug { "Fetching updates with offset $updateOffsetValue" }
        val response = client.getUpdates(updateOffsetValue, timeout = Seconds(pollingTimeout.inWholeSeconds))
        if (response.ok) {
            return response.result
        }

        log.error("Failed to get updates: $response")

        val retryAfter = response.parameters?.retryAfter
        if (retryAfter != null) {
            log.warn("Too many requests, Bot API asks to retry after $retryAfter seconds. Suspending requests for this time...")
            delay(retryAfter.value * 1000L)
            return null
        }

        return null
    }

    private fun onCoroutineError(t: Throwable) {
        log.error("Unexpected coroutine error: ", t)
    }

    companion object {
        private val log = loggerForClass<TelegramBotApiPoller>()
    }
}
