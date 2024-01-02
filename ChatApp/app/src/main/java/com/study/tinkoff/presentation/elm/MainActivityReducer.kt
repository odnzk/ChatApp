package com.study.tinkoff.presentation.elm

import com.study.components.util.ResourcesProvider
import com.study.tinkoff.R
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class MainActivityReducer @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    DslReducer<MainActivityEvent, MainActivityState, MainActivityEffect, MainActivityCommand>() {
    override fun Result.reduce(event: MainActivityEvent) = when (event) {
        MainActivityEvent.Internal.UserNotAuthorized -> {
            effects {
                +MainActivityEffect.Toast(resourcesProvider.getString(R.string.activity_main_user_not_authorized))
                +MainActivityEffect.NavigateToLogin
            }
        }

        MainActivityEvent.Ui.Init -> {
            commands {
                +MainActivityCommand.CheckUserIsAuthorized
            }
        }
    }
}