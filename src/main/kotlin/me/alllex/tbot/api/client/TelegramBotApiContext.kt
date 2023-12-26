package me.alllex.tbot.api.client

import kotlinx.serialization.json.Json
import org.slf4j.Logger

interface TelegramBotApiContext {
    val botApiClient: TelegramBotApiClient
    val logger: Logger
    val json: Json
}

data class SimpleTelegramBotApiContext(
    override val botApiClient: TelegramBotApiClient,
    override val logger: Logger,
    override val json: Json
) : TelegramBotApiContext
