package com.study.profile.presentation.elm

import com.study.profile.presentation.model.UiUserDetailed

internal data class ProfileState(
    val isLoading: Boolean = false,
    val user: UiUserDetailed? = null,
    val error: Throwable? = null
)

internal sealed interface ProfileCommand {
    class LoadUser(val userId: Int) : ProfileCommand
    data object LoadCurrentUser : ProfileCommand
}

internal sealed interface ProfileEffect

internal sealed interface ProfileEvent {
    sealed interface Ui : ProfileEvent {
        data object Init : Ui
        data object Reload : Ui
    }

    sealed interface Internal : ProfileEvent {
        class LoadingUserSuccess(val user: UiUserDetailed) : Internal
        class ErrorLoadingUser(val error: Throwable) : Internal
    }
}
