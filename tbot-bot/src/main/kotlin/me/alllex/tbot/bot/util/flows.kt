package me.alllex.tbot.bot.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile


/**
 * Suspends until there is at least one subscriber in the flow.
 */
suspend fun <T> MutableSharedFlow<T>.awaitCollectors(n: Int = 1) {
    subscriptionCount.takeWhile { it < n }.collect()
}
