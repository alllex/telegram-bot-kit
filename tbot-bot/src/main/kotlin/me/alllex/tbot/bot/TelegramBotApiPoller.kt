package me.alllex.tbot.api.client

import kotlinx.coroutines.*
import me.alllex.tbot.api.model.Seconds
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.newSingleThreadExecutor
import me.alllex.tbot.bot.util.shutdownAndAwaitTermination
import me.alllex.tbot.api.model.Update
import me.alllex.tbot.api.model.getUpdates
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class TelegramBotApiPoller(
    private val client: TelegramBotApiClient,
    private val pollingTimeout: Duration = 10.seconds
) : CoroutineScope {

    private val updateOffset = AtomicLong(0)
    private val listeners = CopyOnWriteArrayList<TelegramBotUpdateListener>()

    private val pollerJob = SupervisorJob()
    private val pollerExecutor = newSingleThreadExecutor("api-poll")
    override val coroutineContext = pollerJob +
            pollerExecutor.asCoroutineDispatcher() +
            CoroutineExceptionHandler { _, t -> onCoroutineError(t) }

    fun addListener(listener: TelegramBotUpdateListener) {
        listeners.addIfAbsent(listener)
    }

    fun removeListener(listener: TelegramBotUpdateListener) {
        listeners -= listener
    }

    fun start() {
        launch { pollingForever() }
        log.info("Started")
    }

    fun stop() {
        pollerJob.cancel()
        pollerExecutor.shutdownAndAwaitTermination()
        log.info("Stopped")
    }

    private suspend fun pollingForever() {
        while (true) {
            supervisorScope {
                launch {
                    val rawUpdates = getUpdatesSafely()
                    processReceivedUpdates(rawUpdates)
                }
            }
        }
    }

    private fun processReceivedUpdates(rawUpdates: List<Update>?) {
        if (rawUpdates.isNullOrEmpty()) {
            return // polling timeout
        }

        if (listeners.isEmpty()) {
            return
        }

        for (l in listeners) {
            for (rawUpdate in rawUpdates) {
                notifyOnUpdateSafely(l, rawUpdate)
            }
        }

        val lastUpdateId = rawUpdates.maxByOrNull { it.updateId }?.updateId
        if (lastUpdateId != null) {
            updateOffset.set(lastUpdateId + 1)
        }
    }

    private suspend fun getUpdatesSafely(): List<Update>? {
        val response = client.getUpdates(updateOffset.get(), timeout = Seconds(pollingTimeout.inWholeSeconds))
        if (response.ok) {
            return response.result
        }

        log.error("Failed to get updates: ${response.description}")
        return null
    }

    private fun notifyOnUpdateSafely(l: TelegramBotUpdateListener, rawUpdate: Update) {
        try {
            notifyOnUpdate(l, rawUpdate)
        } catch (e: Exception) {
            log.error("Unexpected exception: ", e)
        }
    }

    private fun notifyOnUpdate(l: TelegramBotUpdateListener, rawUpdate: Update) {
        l.onUpdate(rawUpdate)
    }

    private fun onCoroutineError(t: Throwable) {
        log.error("Unexpected coroutine error: ", t)
    }

    companion object {
        private val log = loggerForClass<TelegramBotApiPoller>()
    }

}
