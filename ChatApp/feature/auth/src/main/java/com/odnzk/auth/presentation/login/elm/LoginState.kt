package com.odnzk.auth.presentation.login.elm

internal data class LoginState(
    val isLoading: Boolean = false
)

internal sealed interface LoginEvent {
    sealed interface Ui : LoginEvent {
        data object Init : Ui
        class Login(val username: String, val password: String) : Ui

        data object UserDoesNotHaveAnAccount : Ui
    }

    sealed interface Internal : LoginEvent {
        data object LoginSuccess : Internal
        class LoginFailure(val error: Throwable) : Internal
    }
}

internal sealed interface LoginEffect {
    class ShowSnackbar(val message: String) : LoginEffect
    data object NavigateToSignup : LoginEffect
    data object NavigateToMainGraph : LoginEffect
}

internal sealed interface LoginCommand {
    class Login(val username: String, val password: String) : LoginCommand
}