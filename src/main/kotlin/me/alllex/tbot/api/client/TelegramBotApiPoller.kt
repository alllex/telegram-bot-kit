package me.alllex.tbot.api.client

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import me.alllex.tbot.api.model.asSeconds
import me.alllex.tbot.api.model.tryGetUpdates
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


suspend fun TelegramBotApiClient.startPolling(
    listener: TelegramBotUpdateListener,
    onUpdateOffset: suspend (Long) -> Unit = {},
    pollingTimeout: Duration = 10.seconds,
    startingUpdateOffset: Long = 0L,
    logger: Logger = LoggerFactory.getLogger("TelegramBotApiPoller")
) = coroutineScope {

    var currentUpdateOffset = startingUpdateOffset
    val botApiContext = SimpleTelegramBotApiContext(
        botApiClient = this@startPolling,
        logger = logger,
        json = Json(TelegramBotApiClient.JSON) {
            prettyPrint = true
        }
    )

    timerFlow(1.seconds) { tryGetUpdates(offset = currentUpdateOffset, timeout = pollingTimeout.asSeconds) }
        .mapNotNull { it.result }
        .onEach {
            val highestUpdateId = it.maxOfOrNull { it.updateId }
            if (highestUpdateId != null) {
                val newUpdateId = highestUpdateId + 1
                onUpdateOffset(newUpdateId)
                currentUpdateOffset = newUpdateId
            }
        }
        .flatMapMerge { it.asFlow() }
        .onEach {
            launch {
                with(botApiContext) {
                    listener.onUpdate(it)
                }
            }
        }
        .retryWhen { cause, attempt ->
            logger.error("Failed to process update", cause)
            attempt < 10
        }
        .collect()

}

