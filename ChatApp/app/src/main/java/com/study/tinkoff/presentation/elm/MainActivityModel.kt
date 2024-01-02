package com.study.tinkoff.presentation.elm

data object MainActivityState

sealed interface MainActivityEvent {
    sealed interface Ui : MainActivityEvent {
        data object Init : Ui
    }

    sealed interface Internal : MainActivityEvent {
        data object UserNotAuthorized : Internal
    }
}

sealed interface MainActivityEffect {
    class Toast(val message: String) : MainActivityEffect

    data object NavigateToLogin : MainActivityEffect
}

sealed interface MainActivityCommand {
    data object CheckUserIsAuthorized : MainActivityCommand
}