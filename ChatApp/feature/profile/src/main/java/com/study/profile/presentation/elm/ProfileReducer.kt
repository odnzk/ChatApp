package com.study.profile.presentation.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class ProfileReducer :
    DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {

    override fun Result.reduce(event: ProfileEvent) = when (event) {
        is ProfileEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +ProfileCommand.LoadUser(event.userId) }
        }
        is ProfileEvent.Ui.Reload -> {
            state { copy(isLoading = true) }
            commands { +ProfileCommand.LoadUser(event.userId) }
        }
        is ProfileEvent.Internal.LoadingUserError -> {
            state { copy(isLoading = false) }
            effects { +ProfileEffect.ShowError(event.error) }
        }
        is ProfileEvent.Internal.LoadingUserSuccess -> {
            state { copy(isLoading = false, user = event.user) }
        }
    }
}
