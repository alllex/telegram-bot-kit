package me.alllex.tbot.mycounty

import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.runBlocking
import me.alllex.tbot.Build
import me.alllex.tbot.api.client.TelegramBotApiClient
import me.alllex.tbot.bot.TelegramBotApiPoller
import me.alllex.tbot.api.client.unwrap
import me.alllex.tbot.api.model.getMe
import me.alllex.tbot.bot.util.getSystemPropertyOrThrow
import me.alllex.tbot.bot.util.log.loggerForClass
import me.alllex.tbot.bot.util.runForever
import me.alllex.tbot.mycounty.db.Db
import me.alllex.tbot.util.parseYaml
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds


class Main(
    private val config: Config
) {

    private val db: Db = Db(config.db.url)
    private val api: TelegramBotApiClient
    private val apiPoller: TelegramBotApiPoller
    private val bot: MyCountyBot

    init {
        api = TelegramBotApiClient(config.api.token, host = config.api.host) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }
        apiPoller = TelegramBotApiPoller(api, pollingTimeout = 10.seconds)
        bot = MyCountyBot(db, api)
    }

    fun start() {
        log.info("Starting `$NAME` v$version with config:\n${config}")

        db.start()
        runBlocking { checkBotToken() }
        bot.start()
        apiPoller.start(bot)

        log.info("Started")
    }

    fun stop() {
        log.info("Stopping")

        apiPoller.stop()
        bot.stop()
        api.close()
        db.stop()

        log.info("Stopped")
    }

    private suspend fun checkBotToken() {
        val me = api.getMe().unwrap()
        check(me.isBot) { "Self-check for being bot has failed" }
        check(me.username == config.api.username) {
            "Expected username to be @${config.api.username}, but it is @${me.username}"
        }
        log.info("Token-check passed successfully")
    }

    companion object {

        private val log = loggerForClass<Main>()

        val version: String by lazy { Build.version ?: Build.UNKNOWN_VERSION }

        private const val NAME = "tbot-mycounty"
        private const val CONFIG_PROP = "$NAME.yml"

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                mainImpl()
            } catch (t: Throwable) {
                System.err.println(t)
                log.error("Failed to start - error in method \"main\"", t)
                exitProcess(1)
            }

            runForever(log)
        }

        private fun mainImpl() {
            val config = Paths.get(getSystemPropertyOrThrow(CONFIG_PROP))
                .parseYaml<Config>()

            val main = Main(config)

            Runtime.getRuntime().addShutdownHook(
                thread(name = "main-shutdown-hook", start = false) {
                    doMainShutdown(main)
                }
            )

            main.start()
        }

        private fun doMainShutdown(main: Main) {
            log.info("Executing main shutdown hook")
            println("Executing main shutdown hook")
            try {
                main.stop()
            } catch (t: Throwable) {
                log.error("Unexpected exception during shutdown hook execution:", t)
                println("Unexpected exception during shutdown hook execution:\n$t")
            } finally {
                if (System.getProperty("log4j.shutdownHookEnabled") == "false") {
                    println("Shutting down LogManager")
                    LogManager.shutdown()
                }
                println("Main shutdown hook has been executed")
            }
        }
    }
}
