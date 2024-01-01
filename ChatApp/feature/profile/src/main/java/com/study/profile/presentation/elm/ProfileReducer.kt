package com.study.profile.presentation.elm

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class ProfileReducer @AssistedInject constructor(@Assisted("userId") private val userId: Int?) :
    DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {

    override fun Result.reduce(event: ProfileEvent) = when (event) {
        is ProfileEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            loadUser()
        }

        is ProfileEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            loadUser()
        }

        is ProfileEvent.Internal.ErrorLoadingUser -> {
            state { copy(isLoading = false, error = event.error) }
        }

        is ProfileEvent.Internal.LoadingUserSuccess -> {
            state { copy(isLoading = false, error = null, user = event.user) }
        }
    }

    private fun Result.loadUser() {
        if (userId == null) {
            commands { +ProfileCommand.LoadCurrentUser }
        } else {
            commands { +ProfileCommand.LoadUser(userId) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("userId") userId: Int?): ProfileReducer
    }
}
