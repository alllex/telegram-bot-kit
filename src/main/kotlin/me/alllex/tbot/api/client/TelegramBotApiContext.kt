package me.alllex.tbot.api.client

import kotlinx.serialization.json.Json
import org.slf4j.Logger

data class TelegramBotApiContext(
    val botApiClient: TelegramBotApiClient,
    val logger: Logger,
    val json: Json
)
