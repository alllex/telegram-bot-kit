package me.alllex.tbot.mycounty

sealed class UserChatState {

    data object Idle : UserChatState()

    data object AwaitNameForNewCounter : UserChatState()

    data class AwaitNewNameForCounter(val counterId: UserCounterId) : UserChatState()

}
