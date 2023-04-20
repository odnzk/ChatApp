package com.study.profile.presentation.elm

import com.study.profile.domain.usecase.GetUserPresenceUseCase
import com.study.profile.domain.usecase.GetUserUseCase
import com.study.profile.presentation.util.mapper.toUiUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class ProfileActor @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserPresenceUseCase: GetUserPresenceUseCase,
    private val dispatcher: CoroutineDispatcher
) : Actor<ProfileCommand, ProfileEvent.Internal> {

    private val switcher = Switcher()

    override fun execute(command: ProfileCommand): Flow<ProfileEvent.Internal> =
        when (command) {
            is ProfileCommand.LoadUser -> switcher.switch {
                flow {
                    val user = getUserUseCase(command.userId)
                    val presence = getUserPresenceUseCase(user.id)
                    emit(user.toUiUser(presence))
                }.flowOn(dispatcher).mapEvents(
                    ProfileEvent.Internal::LoadingUserSuccess,
                    ProfileEvent.Internal::LoadingUserError
                )
            }
        }

}
