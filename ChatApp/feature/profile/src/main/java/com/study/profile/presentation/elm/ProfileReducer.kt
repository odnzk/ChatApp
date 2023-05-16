package com.study.profile.presentation.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class ProfileReducer @Inject constructor() :
    DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {

    override fun Result.reduce(event: ProfileEvent) = when (event) {
        is ProfileEvent.Ui.Init ->
            if (state.user == null) {
                state { copy(isLoading = true) }
                commands { +ProfileCommand.LoadUser(event.userId) }
            } else Unit
        is ProfileEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +ProfileCommand.LoadUser(event.userId) }
        }
        is ProfileEvent.Internal.ErrorLoadingUser -> {
            state { copy(isLoading = false, error = event.error) }
        }
        is ProfileEvent.Internal.LoadingUserSuccess -> {
            state { copy(isLoading = false, error = null, user = event.user) }
        }
    }
}
