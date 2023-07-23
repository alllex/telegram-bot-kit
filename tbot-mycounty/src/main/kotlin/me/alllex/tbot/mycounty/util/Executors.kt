package me.alllex.tbot.mycounty.util

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Executes [action] on receiver catching and logging an error if thrown.
 */
inline fun Executor.execute(
    log: Logger,
    errMsg: String = "Unexpected error:",
    crossinline action: () -> Unit
) {
    execute {
        try {
            action()
        } catch (t: Throwable) {
            log.error(errMsg, t)
            throw t
        }
    }
}

/**
 * Submits [task] to receiver catching and logging an error if thrown.
 */
inline fun <T> ExecutorService.submit(
    log: Logger,
    errMsg: String = "Unexpected error:",
    crossinline task: () -> T
): Future<T> {
    return submit(Callable {
        try {
            task()
        } catch (t: Throwable) {
            log.error(errMsg, t)
            throw t
        }
    })
}

/**
 * Schedules [command] on receiver catching and logging an error if thrown.
 */
inline fun ScheduledExecutorService.scheduleAtFixedRate(
    log: Logger,
    period: Long,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    initialDelay: Long = period,
    errMsg: String = "Unexpected error: ",
    rethrow: Boolean = false,
    crossinline command: () -> Unit
) {
    scheduleAtFixedRate({
        try {
            command()
        } catch (t: Throwable) {
            log.error(errMsg, t)
            if (rethrow) throw t
        }
    }, initialDelay, period, unit)
}

/**
 * Schedules [command] on receiver catching and logging an error if thrown.
 */
inline fun ScheduledExecutorService.scheduleWithFixedDelay(
    log: Logger,
    period: Long,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    initialDelay: Long = period,
    errMsg: String = "Unexpected error: ",
    rethrow: Boolean = false,
    crossinline command: () -> Unit
) {
    scheduleWithFixedDelay({
        try {
            command()
        } catch (t: Throwable) {
            log.error(errMsg, t)
            if (rethrow) throw t
        }
    }, initialDelay, period, unit)
}

/**
 * Creates new single-thread [ScheduledExecutorService] assigning a [name] to it.
 */
fun newSingleThreadScheduledExecutor(
    name: String
): ScheduledExecutorService {
    return Executors.newSingleThreadScheduledExecutor(NamingThreadFactory(name))
}

/**
 * Creates new single-thread [ExecutorService] assigning a [name] to it.
 */
fun newSingleThreadExecutor(
    name: String
): ExecutorService {
    return Executors.newSingleThreadExecutor(NamingThreadFactory(name))
}

/**
 * Creates new fixed-pool [ExecutorService] assigning a [name] to it.
 */
fun newFixedThreadPoolExecutor(
    name: String,
    size: Int
): ExecutorService {
    val threadFactory = NamingThreadFactory(name, enumerate = true)
    return Executors.newFixedThreadPool(size, threadFactory)
}

fun ExecutorService.shutdownAndAwaitTermination(
    timeout: Long = 500L,
    unit: TimeUnit = TimeUnit.MILLISECONDS
) {
    shutdown()
    awaitTermination(timeout, unit)
}


private class NamingThreadFactory(
    private val name: String,
    private val enumerate: Boolean = false,
    private val delegate: ThreadFactory = Executors.defaultThreadFactory()
) : ThreadFactory {

    private val threadNumber = AtomicInteger(0)

    override fun newThread(r: Runnable): Thread {
        val t = delegate.newThread(r)
        t.name = buildThreadName(name, t.id)
        return t
    }

    private fun buildThreadName(name: String, tid: Long) = buildString {
        append(name)
        if (enumerate) {
            append('-')
            append(threadNumber.incrementAndGet())
        }
        append('@')
        append(tid)
    }

}
