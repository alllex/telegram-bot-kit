package me.alllex.tbot.mycounty

import me.alllex.tbot.api.model.UserId
import me.alllex.tbot.mycounty.db.UserCounterDao
import me.alllex.tbot.mycounty.db.UserCounterUpdateRepository
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class UserCounter(
    val id: UserCounterId,
    name: String,
    val userId: UserId
) {

    lateinit var counterDao: UserCounterDao
    lateinit var counterUpdateRepository: UserCounterUpdateRepository

    var name = name
        private set

    private var state: Int = 0

    suspend fun setName(newName: String) {
        counterDao.updateUserCounterName(id, newName)
        name = newName
    }

    fun getCountValue(): Int {
        return state
    }

    // TODO: not sound impl
    fun setCountValue(value: Int) {
        state = value
    }

    suspend fun register(time: ZonedDateTime): Int {
        registerEvent(time, +1)
        return state
    }

    /**
     * Returns new state value after removal.
     */
    suspend fun removeLast(): Int {
        counterUpdateRepository.removeLast(id)
        val newCount = counterUpdateRepository.computeCumulativeState(id)
        state = newCount
        return newCount
    }

    private suspend fun registerEvent(time: ZonedDateTime, countDiff: Int) {
        val truncatedTime = time.truncatedTo(ChronoUnit.MINUTES)
        counterUpdateRepository.add(id, truncatedTime, countDiff)
        state += countDiff
    }

    override fun toString(): String {
        return "Counter@$id(name='$name', value=$state)"
    }
}
