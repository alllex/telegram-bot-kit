package me.alllex.tbot.mycounty.db

import me.alllex.tbot.api.model.UserId
import me.alllex.tbot.mycounty.UserCounter
import me.alllex.tbot.mycounty.UserCounterId

class UserCounterRepository(
    private val counterDao: UserCounterDao,
    private val counterUpdateRepository: UserCounterUpdateRepository
) {

    private val counters = hashMapOf<UserCounterId, UserCounter>()

    suspend fun findCounterByName(userId: UserId, counterName: String): UserCounter? {
        val counterId = counterDao.findCounterByName(userId, counterName) ?: return null
        return getCounter(counterId)
    }

    suspend fun findCountersMatchingName(userId: UserId, counterNamePattern: String): List<UserCounter> {
        val counterIds = counterDao.findCountersMatchingName(userId, counterNamePattern)
        return counterIds.map { getCounter(it) }
    }

    suspend fun getCounters(userId: UserId): List<UserCounter> {
        val counterIds = counterDao.getUserCounters(userId)
        return counterIds.map { getCounter(it) }
    }

    suspend fun getCounter(userCounterId: UserCounterId): UserCounter {
        return getCounterOrNull(userCounterId) ?: error("counter not found with id=$userCounterId")
    }

    suspend fun getCounterOrNull(userCounterId: UserCounterId): UserCounter? {
        counters[userCounterId]?.let { return it }
        val foundCounter = counterDao.findUserCounter(userCounterId) ?: return null
        return foundCounter.also { postCreate(it) }
    }

    suspend fun addCounter(userId: UserId, newCounterName: String): UserCounter {
        val newCounter = counterDao.createUserCounter(newCounterName, userId)
        return newCounter.also { postCreate(it) }
    }

    suspend fun removeCounter(userCounterId: UserCounterId) {
        counterDao.deleteUserCounter(userCounterId)
        counters -= userCounterId
    }

    private suspend fun postCreate(counter: UserCounter) {
        counter.counterDao = counterDao
        counter.counterUpdateRepository = counterUpdateRepository
        val state = counterUpdateRepository.computeCumulativeState(counter.id)
        counter.setCountValue(state)
        counters[counter.id] = counter
    }
}
