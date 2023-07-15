package me.alllex.tbot.mycounty

import me.alllex.tbot.api.model.ChatId
import me.alllex.tbot.api.model.UserId
import java.time.ZoneId

data class User(
    val userId: UserId,
    val chatId: ChatId,
    val timezone: ZoneId,
) {

    // TODO: persist during restarts
    var state: UserChatState = UserChatState.Idle
}

