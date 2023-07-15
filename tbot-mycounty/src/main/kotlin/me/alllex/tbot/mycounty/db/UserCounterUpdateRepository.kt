package me.alllex.tbot.mycounty.db

import me.alllex.tbot.mycounty.UserCounterId
import me.alllex.tbot.mycounty.UserCounterUpdateId
import java.time.ZonedDateTime

class UserCounterUpdateRepository(
    private val counterUpdateDao: UserCounterUpdateDao
) {

    suspend fun add(counterId: UserCounterId, time: ZonedDateTime, valueDiff: Int): UserCounterUpdateId {
        return counterUpdateDao.create(counterId, time, valueDiff)
    }

    /**
     * Returns number of events that were actually removed.
     */
    suspend fun removeLast(counterId: UserCounterId): Int {
        return counterUpdateDao.removeLast(counterId)
    }

    suspend fun computeCumulativeState(counterId: UserCounterId): Int {
        return counterUpdateDao.computeCumulativeState(counterId)
    }
}
