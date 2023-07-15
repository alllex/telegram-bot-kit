package me.alllex.tbot.apigen

import java.util.*


/**
 * Converts snake_case to camelCase.
 */
fun String.snakeToCamelCase() =
    lowercase(Locale.getDefault())
        .split("_")
        .let { parts ->
            parts.first() + parts.drop(1).joinToString("") { it.toTitleCase() }
        }

/**
 * Converts snake_case to SNAKE_CASE.
 */
fun String.snakeToLoudSnakeCase() = uppercase(Locale.getDefault())

/**
 * Converts snake_case to PascalCase.
 */
fun String.snakeToPascalCase() = snakeToCamelCase().toTitleCase()

fun String.toTitleCase() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

/**
 * Runs [block] and appends a new line at the end
 */
inline fun StringBuilder.appendLine(block: StringBuilder.() -> Unit) {
    block()
    appendLine()
}
