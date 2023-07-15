package me.alllex.tbot.api.model


// TODO: DSL annotation
class InlineKeyboardMarkupBuilder {

    private val rows = mutableListOf<RowBuilder>()

    fun addRow(row: RowBuilder) {
        rows += row
    }

    fun build(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(rows.map { it.build() })
    }

    class RowBuilder {

        private val buttons = mutableListOf<ButtonBuilder>()

        fun addButton(button: ButtonBuilder) {
            buttons += button
        }

        fun build(): List<InlineKeyboardButton> {
            return buttons.map { it.build() }
        }

    }

    class ButtonBuilder {

        var text: String? = null
        var callbackData: String? = null

        fun build(): InlineKeyboardButton {
            val text = text ?: error("text is not set")
            check(text.isNotEmpty()) { "text is empty" }
            val callbackData = callbackData

            if (callbackData != null) {
                check(callbackData.isNotEmpty()) { "callbackData is empty" }
                return InlineKeyboardButton(text, callbackData = callbackData)
            } else {
                error("At least one of optional fields must be set")
            }

        }

    }

}

fun inlineKeyboard(block: InlineKeyboardMarkupBuilder.() -> Unit): InlineKeyboardMarkup {
    val inlineKeyboardBuilder = InlineKeyboardMarkupBuilder()
    inlineKeyboardBuilder.apply(block)
    return inlineKeyboardBuilder.build()
}

fun InlineKeyboardMarkupBuilder.row(block: InlineKeyboardMarkupBuilder.RowBuilder.() -> Unit) {
    val rowBuilder = InlineKeyboardMarkupBuilder.RowBuilder()
    rowBuilder.apply(block)
    addRow(rowBuilder)
}

fun InlineKeyboardMarkupBuilder.RowBuilder.button(text: String, callbackData: String) {
    val buttonBuilder = InlineKeyboardMarkupBuilder.ButtonBuilder()
    buttonBuilder.text = text
    buttonBuilder.callbackData = callbackData
    addButton(buttonBuilder)
}
