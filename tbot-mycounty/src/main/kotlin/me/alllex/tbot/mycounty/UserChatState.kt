package me.alllex.tbot.mycounty

sealed class UserChatState {

    object Idle : UserChatState()

    object AwaitNameForNewCounter : UserChatState()

    data class AwaitNewNameForCounter(val counterId: UserCounterId) : UserChatState()

}
