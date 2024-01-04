package com.odnzk.auth.presentation.login.elm

import com.odnzk.auth.R
import com.study.components.util.ResourcesProvider
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class LoginReducer @Inject constructor(
    private val resourcesProvider: ResourcesProvider
) :
    DslReducer<LoginEvent, LoginState, LoginEffect, LoginCommand>() {
    override fun Result.reduce(event: LoginEvent) = when (event) {
        is LoginEvent.Internal.LoginFailure -> {
            state { copy(isLoading = false) }
            effects {
                +LoginEffect.ShowSnackbar(
                    resourcesProvider.getString(
                        R.string.screen_login_failure,
                        event.error.message.orEmpty()
                    )
                )
            }
        }

        LoginEvent.Internal.LoginSuccess -> {
            state { copy(isLoading = false) }
            effects { +LoginEffect.ShowSnackbar(resourcesProvider.getString(R.string.screen_login_success)) }
            effects { +LoginEffect.NavigateToMainGraph }
        }

        is LoginEvent.Ui.Login -> {
            state { copy(isLoading = true) }
            commands {
                +LoginCommand.Login(event.email, event.password)
            }
        }

        LoginEvent.Ui.Init -> Unit
        LoginEvent.Ui.UserDoesNotHaveAnAccount -> {
            effects { +LoginEffect.NavigateToSignup }
        }
    }
}