package com.study.profile.presentation.elm

import com.study.components.model.UserPresenceStatus
import com.study.profile.domain.usecase.GetUserPresenceUseCase
import com.study.profile.domain.usecase.GetUserUseCase
import com.study.profile.presentation.util.mapper.toUiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class ProfileActor @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserPresenceUseCase: GetUserPresenceUseCase
) : Actor<ProfileCommand, ProfileEvent.Internal> {

    private val switcher = Switcher()
    override fun execute(command: ProfileCommand): Flow<ProfileEvent.Internal> =
        when (command) {
            is ProfileCommand.LoadUser -> switcher.switch {
                flow {
                    val user = getUserUseCase(command.userId)
                    when {
                        user.isActive -> emit(user.toUiUser(getUserPresenceUseCase(user.id)))
                        user.isBot -> emit(user.toUiUser(UserPresenceStatus.BOT))
                        else -> emit(user.toUiUser(UserPresenceStatus.OFFLINE))
                    }
                }.mapEvents(
                    ProfileEvent.Internal::LoadingUserSuccess,
                    ProfileEvent.Internal::ErrorLoadingUser
                )
            }
        }

}
