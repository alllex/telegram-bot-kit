package me.alllex.tbot.mycounty

class BadRequestException(
    val userFriendlyMessage: String?,
    cause: Throwable?
) : RuntimeException(userFriendlyMessage, cause)
