package me.alllex.tbot.mycounty

import me.alllex.tbot.api.model.ChatId
import me.alllex.tbot.api.model.UserId
import java.time.ZoneId

data class BotUser(
    val userId: UserId,
    val chatId: ChatId,
    val timezone: ZoneId,
) {

    // TODO: persist during restarts
    var state: UserChatState = UserChatState.Idle

    override fun toString(): String = "BotUser(${userId.value})"
}

