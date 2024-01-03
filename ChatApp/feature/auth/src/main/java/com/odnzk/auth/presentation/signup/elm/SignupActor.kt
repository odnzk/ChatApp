package com.odnzk.auth.presentation.signup.elm

import com.odnzk.auth.domain.usecase.SignupUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor

internal class SignupActor(private val signupUseCase: SignupUseCase) :
    Actor<SignupCommand, SignupEvent> {
    override fun execute(command: SignupCommand): Flow<SignupEvent> = when (command) {
        is SignupCommand.Signup -> flow {
            emit(
                signupUseCase(
                    email = command.email,
                    fullName = command.fullName,
                    password = command.password
                )
            )
        }.mapEvents(
            eventMapper = { SignupEvent.Internal.SignupSuccess },
            errorMapper = SignupEvent.Internal::SignupFailure
        )
    }
}