package me.alllex.tbot.api.client

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.alllex.tbot.api.model.asSeconds
import me.alllex.tbot.api.model.tryGetUpdates
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


suspend fun TelegramBotApiClient.startPolling(
    listener: TelegramBotUpdateListener,
    onUpdateOffsetChanged: suspend (Long) -> Unit = {},
    pollingTimeout: Duration = 10.seconds,
    startingUpdateOffset: Long = 0L,
    logger: Logger = LoggerFactory.getLogger("TelegramBotApiPoller")
): Unit = supervisorScope {
    var currentUpdateOffset = startingUpdateOffset
    val json = Json(TelegramBotApiClient.JSON) {
        prettyPrint = true
    }
    val botApiContext = TelegramBotApiContext(
        botApiClient = this@startPolling,
        logger = logger,
        json = json
    )
    timerFlow(1.seconds) {
        tryGetUpdates(
            offset = currentUpdateOffset,
            timeout = pollingTimeout.asSeconds
        )
    }
        .mapNotNull { it.result }
        .onEach {
            val highestUpdateId = it.maxOfOrNull { it.updateId }
            if (highestUpdateId != null) {
                val newUpdateId = highestUpdateId + 1
                onUpdateOffsetChanged(newUpdateId)
                currentUpdateOffset = newUpdateId
            }
        }
        .flatMapMerge { it.asFlow() }
        .retry { cause ->
            logger.error("Failed to get updates.", cause)
            true
        }
        .collect { update ->
            val errorHandler = CoroutineExceptionHandler { _, throwable ->
                val message = buildString {
                    appendLine("Error while executing update:")
                    appendLine(json.encodeToString(update))
                }
                logger.error(message, throwable)
            }
            launch(errorHandler) {
                with(botApiContext) {
                    listener.onUpdate(update)
                }
            }
        }

}

