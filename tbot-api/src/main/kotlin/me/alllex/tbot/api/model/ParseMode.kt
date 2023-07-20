package me.alllex.tbot.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Telegram supports multiple kinds of [ParseMode] for text messages.
 *
 * See more in [formatting options documentation](https://core.telegram.org/bots/api#formatting-options)
 */
@Serializable
enum class ParseMode(val value: String) {

    @SerialName("MarkdownV2")
    MARKDOWN("MarkdownV2"),

    @SerialName("HTML")
    HTML("HTML"),

    @SerialName("Markdown")
    MARKDOWN_LEGACY("Markdown");

    override fun toString() = value
}
