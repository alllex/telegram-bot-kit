package me.alllex.tbot.mycounty.db

import me.alllex.tbot.api.model.UserId
import me.alllex.tbot.mycounty.UserCounter
import me.alllex.tbot.mycounty.UserCounterId
import java.sql.ResultSet

class UserCounterDao(
    private val db: Db
) {

    private val conn get() = db.connection

    suspend fun findUserCounter(counterId: UserCounterId): UserCounter? = db.run {
        conn.prepareStatement("select * from counters where id = ?").use { stmt ->
            stmt.setLong(1, counterId)
            stmt.executeQuery().use { rs ->
                if (rs.next()) rs.readUserCounter() else null
            }
        }
    }

    suspend fun getUserCounter(counterId: UserCounterId): UserCounter = db.run {
        conn.prepareStatement("select * from counters where id = ?").use { stmt ->
            stmt.setLong(1, counterId)
            stmt.executeQuery().use { rs ->
                if (rs.next()) rs.readUserCounter() else error("no such user counter: counterId=$counterId")
            }
        }
    }

    suspend fun getUserCounters(userId: UserId): List<UserCounterId> = db.run {
        conn.prepareStatement("select id from counters where user_id = ?").use { stmt ->
            stmt.setLong(1, userId.value)
            stmt.executeQuery().use { rs ->
                val counterIds = mutableListOf<UserCounterId>()
                while (rs.next()) {
                    counterIds += rs.getLong(1)
                }
                counterIds
            }
        }
    }

    suspend fun findCounterByName(userId: UserId, counterName: String): UserCounterId? = db.run {
        conn.prepareStatement("select id from counters where user_id = ? and upper(name) like upper(?)").use { stmt ->
            stmt.setLong(1, userId.value)
            stmt.setString(2, counterName)
            stmt.executeQuery().use { rs ->
                if (rs.next()) rs.getLong("id") else null
            }
        }
    }

    suspend fun findCountersMatchingName(userId: UserId, counterNamePattern: String): List<UserCounterId> = db.run {
        conn.prepareStatement("select id from counters where user_id = ? and instr(upper(name), upper(?)) > 0").use { stmt ->
            stmt.setLong(1, userId.value)
            stmt.setString(2, counterNamePattern)
            stmt.executeQuery().use { rs ->
                val ids = mutableListOf<UserCounterId>()
                while (rs.next()) {
                    ids += rs.getLong("id")
                }
                ids
            }
        }
    }

    suspend fun createUserCounter(counterName: String, userId: UserId): UserCounter = db.run {
        conn.prepareStatement("insert into counters(name, user_id) values(?,?)").use { stmt ->
            stmt.setString(1, counterName)
            stmt.setLong(2, userId.value)
            stmt.executeUpdate()
            val newUserCounterId = stmt.generatedKeys.use { rs ->
                if (rs.next()) rs.getLong("last_insert_rowid()") else null
            }
            checkNotNull(newUserCounterId) { "failed to insert a new user counter" }
            getUserCounter(newUserCounterId)
        }
    }

    suspend fun deleteUserCounter(counterId: UserCounterId): Unit = db.run {
        conn.prepareStatement("delete from counters where id = ?").use { stmt ->
            stmt.setLong(1, counterId)
            stmt.executeUpdate()
            Unit
        }
    }

    suspend fun updateUserCounterName(counterId: UserCounterId, newName: String): Unit = db.run {
        conn.prepareStatement("update counters set name = ? where id = ?").use { stmt ->
            stmt.setString(1, newName)
            stmt.setLong(2, counterId)
            stmt.executeUpdate()
            Unit
        }
    }

    companion object {
        private fun ResultSet.readUserCounter(): UserCounter {
            val counterId = getLong("id")
            val counterName = getString("name")
            val userId = getLong("user_id")
            return UserCounter(counterId, counterName, UserId(userId))
        }
    }
}
