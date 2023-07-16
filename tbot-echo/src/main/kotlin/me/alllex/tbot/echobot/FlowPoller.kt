package me.alllex.tbot.echobot

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.model.Seconds
import me.alllex.tbot.api.model.Update
import me.alllex.tbot.api.model.getUpdates
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.newSingleThreadExecutor
import me.alllex.tbot.bot.util.shutdownAndAwaitTermination
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class FlowPoller(
    private val client: TelegramBotApiClient,
    private val pollingTimeout: Duration = 10.seconds
) : CoroutineScope, AutoCloseable {

    override fun close() {
        stop()
    }

    private val updateOffset = AtomicLong(0)

    private val pollerJob = SupervisorJob()
    private val pollerExecutor = newSingleThreadExecutor("api-poll")
    override val coroutineContext = pollerJob +
        pollerExecutor.asCoroutineDispatcher() +
        CoroutineExceptionHandler { _, t -> onCoroutineError(t) }

    private val _updates: MutableSharedFlow<Update> = MutableSharedFlow()

    val updates: SharedFlow<Update> = _updates.asSharedFlow()

    fun start() {
        launch {
            _updates.subscriptionCount.takeWhile { it == 0 }.collect()
            pollingForever()
        }
        log.info("Started")
    }

    fun stop() {
        pollerJob.cancel()
        pollerExecutor.shutdownAndAwaitTermination()
        log.info("Stopped")
    }

    private suspend fun pollingForever() {
        while (true) {
            try {
                val rawUpdates = getUpdatesSafely()
                processReceivedUpdates(rawUpdates)
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                onCoroutineError(t)
            }
        }
    }

    private suspend fun processReceivedUpdates(rawUpdates: List<Update>?) {
        if (rawUpdates.isNullOrEmpty()) {
            return // polling timeout
        }
        for (rawUpdate in rawUpdates) {
            _updates.emit(rawUpdate)
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

    private fun onCoroutineError(t: Throwable) {
        log.error("Unexpected coroutine error: ", t)
    }

    companion object {
        private val log = loggerForClass<FlowPoller>()
    }

}
