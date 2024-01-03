package com.odnzk.auth.presentation.login.elm

import com.odnzk.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

internal class LoginActor @Inject constructor(private val loginUseCase: LoginUseCase) :
    Actor<LoginCommand, LoginEvent> {
    override fun execute(command: LoginCommand): Flow<LoginEvent> = when (command) {
        is LoginCommand.Login -> flow {
            emit(loginUseCase(email = command.email, password = command.password))
        }.mapEvents(
            eventMapper = { LoginEvent.Internal.LoginSuccess },
            errorMapper = LoginEvent.Internal::LoginFailure
        )
    }
}