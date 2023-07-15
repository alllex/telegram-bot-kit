package me.alllex.tbot.mycounty.db

import me.alllex.tbot.mycounty.UserCounterId
import me.alllex.tbot.mycounty.UserCounterUpdateId
import java.time.ZonedDateTime

class UserCounterUpdateDao(
    private val db: Db
) {

    private val conn get() = db.connection

    suspend fun create(counterId: UserCounterId, time: ZonedDateTime, valueDiff: Int): UserCounterUpdateId = db.run {
        conn.prepareStatement("insert into counter_updates(timestamp, counter_id, diff) values(?,?,?)").use { stmt ->
            stmt.setString(1, time.toString())
            stmt.setLong(2, counterId)
            stmt.setInt(3, valueDiff)
            stmt.executeUpdate()
            val newId = stmt.generatedKeys.use { rs ->
                if (rs.next()) rs.getLong("last_insert_rowid()") else null
            }
            checkNotNull(newId) { "failed to insert a new row" }
        }
    }

    suspend fun computeCumulativeState(counterId: UserCounterId): Int = db.run {
        conn.prepareStatement("select sum(diff) from counter_updates where counter_id = ?").use { stmt ->
            stmt.setLong(1, counterId)
            stmt.executeQuery().use { rs ->
                if (rs.next()) rs.getInt(1) else 0
            }
        }
    }

    suspend fun removeLast(counterId: UserCounterId) = db.run {
        conn.prepareStatement(
            "delete from counter_updates where id in (select id from counter_updates where counter_id = ? order by timestamp desc limit 1)"
        ).use { stmt ->
            stmt.setLong(1, counterId)
            stmt.executeUpdate()
        }
    }

    companion object
}
