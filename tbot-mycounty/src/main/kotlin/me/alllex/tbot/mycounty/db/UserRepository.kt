package me.alllex.tbot.mycounty.db

import me.alllex.tbot.api.model.ChatId
import me.alllex.tbot.mycounty.BotUser
import java.time.ZoneId

/**
 * This class is NOT THREAD-SAFE.
 */
class UserRepository(private val userDao: UserDao) {

    private val usersByChatId = hashMapOf<ChatId, BotUser>()

    suspend fun getOrCreateUser(tgChatId: ChatId, timezone: ZoneId): BotUser {
        usersByChatId[tgChatId]?.let { return it }

        val foundUserId = userDao.findUserByChatId(tgChatId)
        val user = if (foundUserId == null) {
            userDao.createUser(tgChatId, timezone)
        } else {
            userDao.getUser(foundUserId)
        }

        usersByChatId[tgChatId] = user

        return user
    }

}
