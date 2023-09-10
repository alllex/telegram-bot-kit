package me.alllex.tbot.api.model


/**
 * Creates an [InlineKeyboardMarkup] using the provided [block].
 *
 * Example:
 * ```
 * val markup = inlineKeyboard {
 *    row {
 *      button("Yes", "yes")
 *      button("No", "no")
 *    }
 *    button("Cancel", "maybe")
 * }
 * ```
 *
 * Creates the following markup:
 * ```
 * ____________
 * | Yes | No |
 * ____________
 * |  Cancel  |
 * ------------
 * ```
 */
@InlineKeyboardMarkupDsl
fun inlineKeyboard(block: InlineKeyboardMarkupBuilder.() -> Unit): InlineKeyboardMarkup {
    val inlineKeyboardBuilder = InlineKeyboardMarkupBuilder()
    inlineKeyboardBuilder.apply(block)
    return inlineKeyboardBuilder.build()
}

/**
 * Adds a row to the inline keyboard.
 */
@InlineKeyboardMarkupDsl
fun InlineKeyboardMarkupBuilder.row(block: InlineKeyboardMarkupBuilder.RowBuilder.() -> Unit) {
    val rowBuilder = InlineKeyboardMarkupBuilder.RowBuilder()
    rowBuilder.apply(block)
    addRow(rowBuilder.build())
}

@InlineKeyboardMarkupDsl
fun InlineKeyboardMarkupBuilder.rowsChunked(columns: Int, block: InlineKeyboardMarkupBuilder.RowBuilder.() -> Unit) {
    val rowBuilder = InlineKeyboardMarkupBuilder.RowBuilder()
    rowBuilder.apply(block)
    val buttons = rowBuilder.build()
    buttons.chunked(columns).forEach { addRow(it) }
}

/**
 * Adds a button to the row in the inline keyboard.
 */
@InlineKeyboardMarkupDsl
fun InlineKeyboardMarkupBuilder.RowBuilder.button(text: String, callbackData: String) {
    val buttonBuilder = InlineKeyboardMarkupBuilder.ButtonBuilder()
    buttonBuilder.text = text
    buttonBuilder.callbackData = callbackData
    addButton(buttonBuilder)
}

/**
 * Adds a row with a single button to the inline keyboard.
 */
@InlineKeyboardMarkupDsl
fun InlineKeyboardMarkupBuilder.button(text: String, callbackData: String) {
    row {
        button(text, callbackData)
    }
}

/**
 * Adds a link-button to the row in the inline keyboard.
 */
@InlineKeyboardMarkupDsl
fun InlineKeyboardMarkupBuilder.RowBuilder.buttonLink(text: String, url: String) {
    val buttonBuilder = InlineKeyboardMarkupBuilder.ButtonBuilder()
    buttonBuilder.text = text
    buttonBuilder.url = url
    addButton(buttonBuilder)
}

/**
 * Adds a row with a single link-button to the inline keyboard.
 */
@InlineKeyboardMarkupDsl
fun InlineKeyboardMarkupBuilder.buttonLink(text: String, url: String) {
    row {
        buttonLink(text, url)
    }
}

/**
 * Builder for [InlineKeyboardMarkup]. Best used with DSL functions such as [inlineKeyboard].
 */
@InlineKeyboardMarkupDsl
class InlineKeyboardMarkupBuilder {

    private val rows = mutableListOf<List<InlineKeyboardButton>>()

    fun addRow(row: List<InlineKeyboardButton>) {
        if (row.isNotEmpty()) {
            rows += row
        }
    }

    fun build(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(rows)
    }

    @InlineKeyboardMarkupDsl
    class RowBuilder {

        private val buttons = mutableListOf<ButtonBuilder>()

        fun addButton(button: ButtonBuilder) {
            buttons += button
        }

        fun build(): List<InlineKeyboardButton> {
            return buttons.map { it.build() }
        }

    }

    @InlineKeyboardMarkupDsl
    class ButtonBuilder {

        var text: String? = null
        var url: String? = null
        var callbackData: String? = null

        fun build(): InlineKeyboardButton {
            val text = text ?: error("text is not set")
            check(text.isNotEmpty()) { "text is empty" }

            val url = url
            val callbackData = callbackData

            check(listOfNotNull(url, callbackData).size == 1) {
                "Exactly one of optional fields must be set"
            }
            check(url == null || url.isNotEmpty()) { "url is empty" }
            check(callbackData == null || callbackData.isNotEmpty()) { "callbackData is empty" }

            return InlineKeyboardButton(text, url = url, callbackData = callbackData)
        }

    }

}

/**
 * Marks DSL and makes sure invalid constructs such as `row { row { } }` do not compile.
 */
@DslMarker
annotation class InlineKeyboardMarkupDsl
