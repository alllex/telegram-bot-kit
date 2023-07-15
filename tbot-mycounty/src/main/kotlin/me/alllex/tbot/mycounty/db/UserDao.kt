package me.alllex.tbot.mycounty.db

import me.alllex.tbot.api.model.ChatId
import me.alllex.tbot.api.model.UserId
import me.alllex.tbot.mycounty.User
import java.sql.ResultSet
import java.time.ZoneId

class UserDao(
    private val db: Db
) {

    private val conn get() = db.connection

    suspend fun findUserByChatId(tgChatId: ChatId): UserId? = db.run {
        conn.prepareStatement("select id from users where tg_chat_id = ?").use { stmt ->
            stmt.setLong(1, tgChatId.value)
            stmt.executeQuery().use { rs ->
                if (rs.next()) rs.getLong("id") else null
            }
        }
    }?.let(::UserId)

    suspend fun getUser(userId: UserId): User = db.run {
        conn.prepareStatement("select * from users where id = ?").use { stmt ->
            stmt.setLong(1, userId.value)
            stmt.executeQuery().use { rs ->
                if (rs.next()) rs.readUser() else error("no such user: userId=$userId")
            }
        }
    }

    suspend fun createUser(tgChatId: ChatId, timezone: ZoneId): User = db.run {
        conn.prepareStatement("insert into users(tg_chat_id, timezone) values(?,?)").use { stmt ->
            stmt.setLong(1, tgChatId.value)
            stmt.setString(2, timezone.id)
            stmt.executeUpdate()
            val newUserId = stmt.generatedKeys.use { rs ->
                if (rs.next()) rs.getLong("last_insert_rowid()") else null
            }
            checkNotNull(newUserId) { "failed to insert a new user" }
            getUser(UserId(newUserId))
        }
    }

    companion object {
        private fun ResultSet.readUser(): User {
            val userId = getLong("id")
            val tgChatId = getLong("tg_chat_id")
            val timezoneStr = getString("timezone")
            val timezone = ZoneId.of(timezoneStr)
            return User(UserId(userId), ChatId(tgChatId), timezone)
        }
    }
}
