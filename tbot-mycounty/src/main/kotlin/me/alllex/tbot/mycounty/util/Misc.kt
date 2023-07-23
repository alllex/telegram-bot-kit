package me.alllex.tbot.mycounty.util

fun getSystemPropertyOrThrow(name: String, def: String? = null) =
    System.getProperty(name, def) ?: error("System property is not found: $name")

fun runForever(log: Logger) {
    log.info("Starting forever loop")
    try {
        val period = (24 * 60 * 60 * 1000).toLong()
        while (!Thread.currentThread().isInterrupted) {
            Thread.sleep(period)
        }
    } catch (e: InterruptedException) {
        log.error("Got interrupted", e)
    }
}
