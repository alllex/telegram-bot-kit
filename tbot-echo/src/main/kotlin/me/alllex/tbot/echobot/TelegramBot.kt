package me.alllex.tbot.echobot

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.api.client.TelegramBotApiContext
import me.alllex.tbot.api.model.Update
import me.alllex.tbot.bot.util.log.loggerForClass
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TelegramBot(
    private val client: TelegramBotApiClient,
    private val pollingTimeout: Duration = 10.seconds,
) {

    private val started = AtomicBoolean()

    private var state: State? = null

    fun start(process: suspend TelegramBotApiContext.(Update) -> Unit) {
        check(started.compareAndSet(false, true)) { "Already started" }
        flowOf(1)

        val poller = FlowPoller(client, pollingTimeout)
        val state = State(poller)
        this.state = state

        val botApiContext = object : TelegramBotApiContext {
            override val botApiClient = client
        }

        state.coroutineScope.launch {
            poller.use {
                it.updates.collect { update ->
                    try {
                        with(botApiContext) {
                            process(update)
                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (t: Throwable) {
                        println("Error while processing update: $update")
                    }
                }
            }
        }

        println("Starting poller...")
        poller.start()

        println("Bot started")
        log.info("Started")
    }

    fun stop() {
        val state = state ?: run {
            log.warn("Already stopped")
            return
        }

        state.poller.stop()
        state.coroutineScope.cancel()

//        runBlocking {
//            state.coroutineScope.coroutineContext.job.cancelAndJoin()
//        }
        this.state = null

        log.info("Stopped")
    }

    private class State(
        val poller: FlowPoller
    ) {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default.limitedParallelism(1))
    }

    companion object {
        private val log = loggerForClass<TelegramBot>()
    }

}
