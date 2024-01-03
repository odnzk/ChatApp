package com.odnzk.auth.presentation.elm

internal data class AuthState(
    val isLoading: Boolean = false,
    val isUserAuthorized: Boolean? = null
)

internal sealed interface AuthEvent {
    sealed interface Ui : AuthEvent {
        data object Init : Ui
    }

    sealed interface Internal : AuthEvent {
        data object Authorized : Internal
        data object NotAuthorized : Internal
    }
}

internal sealed interface AuthEffect {
    class ShowSnackbar(val message: String) : AuthEffect

    data object NavigateToMainFeatures : AuthEffect
}

internal sealed interface AuthCommand {
    data object CheckUserIsAuthorized : AuthCommand
}