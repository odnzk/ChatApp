package com.odnzk.auth.presentation.elm

import com.odnzk.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class LoginActor @Inject constructor(private val loginUseCase: LoginUseCase) :
    Actor<LoginCommand, LoginEvent> {
    override fun execute(command: LoginCommand): Flow<LoginEvent> = when (command) {
        is LoginCommand.Login -> flow<LoginEvent> {
            loginUseCase(username = command.username, password = command.password)
            emit(LoginEvent.Internal.LoginSuccess)
        }.mapEvents(errorMapper = { LoginEvent.Internal.LoginFailure(it) })
    }
}