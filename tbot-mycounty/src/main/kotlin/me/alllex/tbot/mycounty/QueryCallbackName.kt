package me.alllex.tbot.mycounty

enum class QueryCallbackName(val string: String) {
    CREATE("counter-create"),
    SELECT("counters-select"),
    SELECT_SINGLE("counter-select"),
    INC("counter-inc"),
    UNDO("counter-undo"),
    UNDO_CONFIRM("counter-undo-confirm"),
    UNDO_CANCEL("counter-undo-cancel"),
    RENAME("counter-rename"),
    DELETE("counter-delete"),
    DELETE_CONFIRM("counter-delete-confirm"),
    DELETE_CANCEL("counter-delete-cancel"),
    ;

    override fun toString(): String = string

    companion object {
        fun parse(s: String): QueryCallbackName? {
            return entries.find { it.string == s }
        }
    }
}
