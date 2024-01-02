package me.alllex.tbot.api.client

import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal var HttpTimeout.HttpTimeoutCapabilityConfiguration.requestTimeout: Duration?
    get() = requestTimeoutMillis?.milliseconds
    set(value) {
        requestTimeoutMillis = value?.inWholeMilliseconds
    }

internal fun <T> timerFlow(interval: Duration, function: suspend () -> T) = flow {
    while (true) {
        delay(interval)
        emit(function())
    }
}
