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

fun StringBuilder.appendKdoc(s: String, indentSpaces: Int = 0) {
    if (indentSpaces > 0) append(" ".repeat(indentSpaces))
    appendLine("/**")
    appendKdocLines(s, indentSpaces)
    if (indentSpaces > 0) append(" ".repeat(indentSpaces))
    appendLine(" */")
}

fun StringBuilder.appendKdocLines(s: String, indentSpaces: Int = 0) = appendKdocLines(s.split("\n"), indentSpaces)

fun StringBuilder.appendKdocLines(lines: Iterable<String>, indentSpaces: Int = 0) {
    var prevIsEmpty = false
    for (line in lines) {
        if (indentSpaces > 0) append(" ".repeat(indentSpaces))
        val trimmed = line.trim()
        if (trimmed.isEmpty()) {
            if (prevIsEmpty) continue
            appendLine(" *")
            prevIsEmpty = true
        } else {
            append(" * ").append(trimmed).appendLine()
            prevIsEmpty = false
        }
    }
}

fun String.prependIfNotEmpty(prefix: String) = if (isEmpty()) this else prefix + this
