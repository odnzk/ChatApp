package com.odnzk.auth.presentation.signup.elm

import com.odnzk.auth.R
import com.study.components.util.ResourcesProvider
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class SignupReducer @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    DslReducer<SignupEvent, SignupState, SignupEffect, SignupCommand>() {
    override fun Result.reduce(event: SignupEvent): Any = when (event) {
        is SignupEvent.Internal.SignupFailure -> {
            state { copy(isLoading = false) }
            effects {
                +SignupEffect.ShowSnackbar(
                    resourcesProvider.getString(
                        R.string.screen_signup_failure,
                        event.error.message.orEmpty()
                    )
                )
            }
        }

        SignupEvent.Internal.SignupSuccess -> {
            state { copy(isLoading = false) }
            effects { +SignupEffect.ShowSnackbar(resourcesProvider.getString(R.string.screen_signup_success)) }
        }

        is SignupEvent.Ui.Signup -> {
            state { copy(isLoading = true) }
            commands {
                +SignupCommand.Signup(
                    email = event.email,
                    password = event.password,
                    fullName = event.fullName
                )
            }
        }

        SignupEvent.Ui.Unit -> Unit
        SignupEvent.Ui.UserAlreadyHasAccount -> {
            effects { SignupEffect.NavigateToLogin }
        }
    }

}