package me.alllex.tbot.apigen

import kotlinx.serialization.Serializable


@Serializable
data class BotApiElementArgs(
    val name: String,
    val args: List<String>,
)

@Serializable
data class BotApiArgLists(
    val types: List<BotApiElementArgs>,
    val methods: List<BotApiElementArgs>,
)
