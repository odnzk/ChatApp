package com.odnzk.auth.presentation.signup.elm

internal data class SignupState(val isLoading: Boolean = false)

internal sealed interface SignupEvent {
    sealed interface Ui : SignupEvent {
        data object Unit : Ui
        class Signup(val email: String, val password: String, val fullName: String) : Ui

        data object UserAlreadyHasAccount : Ui
    }

    sealed interface Internal : SignupEvent {
        class SignupFailure(val error: Throwable) : Internal

        data object SignupSuccess : Internal
    }
}

internal sealed interface SignupEffect {
    class ShowSnackbar(val message: String) : SignupEffect

    data object NavigateToLogin : SignupEffect
}

internal sealed interface SignupCommand {
    class Signup(val email: String, val password: String, val fullName: String) : SignupCommand
}