package com.odnzk.auth.presentation.elm

import com.odnzk.auth.R
import com.study.components.util.ResourcesProvider
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class AuthReducer @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    DslReducer<AuthEvent, AuthState, AuthEffect, AuthCommand>() {
    override fun Result.reduce(event: AuthEvent) = when (event) {
        AuthEvent.Internal.NotAuthorized -> {
            state { copy(isLoading = false, isUserAuthorized = false) }
            effects {
                +AuthEffect.ShowSnackbar(resourcesProvider.getString(R.string.common_error_user_not_authorized))
            }
        }

        AuthEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands {
                +AuthCommand.CheckUserIsAuthorized
            }
        }

        AuthEvent.Internal.Authorized -> {
            state { copy(isLoading = false, isUserAuthorized = true) }
            effects { +AuthEffect.NavigateToMainFeatures }
        }
    }
}