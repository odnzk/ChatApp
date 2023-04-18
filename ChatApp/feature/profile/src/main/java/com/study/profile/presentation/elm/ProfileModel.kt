package com.study.profile.presentation.elm

import com.study.profile.presentation.util.model.UiUser

internal data class ProfileState(
    val isLoading: Boolean = false,
    val user: UiUser? = null,
    val error: Throwable? = null
)

internal sealed interface ProfileCommand {
    class LoadUser(val userId: Int) : ProfileCommand
}

internal sealed interface ProfileEffect

internal sealed interface ProfileEvent {
    sealed interface Ui : ProfileEvent {
        class Init(val userId: Int) : Ui
        class Reload(val userId: Int) : Ui
    }

    sealed interface Internal : ProfileEvent {
        class LoadingUserSuccess(val user: UiUser) : Internal
        class LoadingUserError(val error: Throwable) : Internal
    }
}
