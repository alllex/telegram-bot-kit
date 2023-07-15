package me.alllex.tbot

import java.time.Instant
import java.util.*


/**
 * See Gradle task `createBuildProperties`.
 */
object Build {

    const val UNKNOWN_VERSION = "<unknown version>"

    private const val BUILD_PROPS_RESOURCE = "build.properties"

    val version: String? by lazy { getBuildProperty("version") }

    val time: Instant? by lazy {
        try {
            getBuildProperty("time")?.let { Instant.parse(it) }
        } catch (t: Throwable) {
            null
        }
    }

    private fun getBuildProperty(name: String): String? {
        return buildProps?.getProperty(name)
    }

    private val buildProps: Properties? by lazy {
        Build::class.java.classLoader
            .getResourceAsStream(BUILD_PROPS_RESOURCE)
            ?.use { r ->
                Properties().apply { load(r) }
            }
    }
}
