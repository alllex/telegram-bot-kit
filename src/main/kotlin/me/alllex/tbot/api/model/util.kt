package me.alllex.tbot.api.model

internal fun quoteWhenWhitespace(v: Any?) =
    if (v != null && v is CharSequence) quoteWhenWhitespace(v) else v

internal fun quoteWhenWhitespace(s: CharSequence): String = if (s.any { it.isWhitespace() }) "'$s'" else s.toString()

internal fun joinLines(v: Any?) =
    if (v != null && v is CharSequence) joinLines(v) else v

internal fun joinLines(s: CharSequence): String = s.splitToSequence('\n').joinToString(separator = "\\n")

class DebugStringBuilder(name: String) {

    private val sb = StringBuilder(name).append('(')
    private var first = true

    fun prop(name: String, value: Any?) = apply {
        if (value == null) return@apply

        if (first) {
            first = false
        } else {
            sb.append(", ")
        }
        sb.append(name).append('=').append(quoteWhenWhitespace(joinLines(value)))
    }

    override fun toString(): String {
        return sb.append(')').toString()
    }
}
