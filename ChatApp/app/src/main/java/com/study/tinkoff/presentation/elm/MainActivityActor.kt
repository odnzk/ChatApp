package com.study.tinkoff.presentation.elm

import com.study.tinkoff.domain.CheckAuthorizationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

class MainActivityActor @Inject constructor(private val checkAuthorizationUseCase: CheckAuthorizationUseCase) :
    Actor<MainActivityCommand, MainActivityEvent> {
    override fun execute(command: MainActivityCommand): Flow<MainActivityEvent> = when (command) {
        MainActivityCommand.CheckUserIsAuthorized -> flow {
            if (!checkAuthorizationUseCase()) {
                emit(MainActivityEvent.Internal.UserNotAuthorized)
            }
        }
    }
}