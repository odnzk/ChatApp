package com.odnzk.auth.presentation.elm

import com.odnzk.auth.domain.usecase.CheckAuthorizationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class AuthActor @Inject constructor(private val checkAuthorizationUseCase: CheckAuthorizationUseCase) :
    Actor<AuthCommand, AuthEvent> {
    override fun execute(command: AuthCommand): Flow<AuthEvent> = when (command) {
        AuthCommand.CheckUserIsAuthorized -> flow {
            if (checkAuthorizationUseCase()) {
                emit(AuthEvent.Internal.Authorized)
            } else {
                emit(AuthEvent.Internal.NotAuthorized)
            }
        }
    }
}