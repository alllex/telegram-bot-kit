package me.alllex.tbot.mycounty

enum class CommandName(
    val string: String
) {

    /**
     * Start is sent when the user initiates the dialog with the bot for the first time.
     */
    START("/start"),

    /**
     * Command to create a new counter
     */
    CREATE("/create"),

    /**
     * Command to create a new counter
     */
    SELECT("/select"),

    /**
     * Command to show the state of all counters
     */
    SUMMARY("/summary"),

    /**
     * Command to cancel an ongoing task
     */
    CANCEL("/cancel"),

    /**
     * Provides help entry
     */
    HELP("/help"),
}
