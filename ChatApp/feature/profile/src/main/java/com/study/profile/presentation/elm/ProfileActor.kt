package com.study.profile.presentation.elm

import com.study.profile.domain.usecase.GetCurrentUserPresenceUseCase
import com.study.profile.domain.usecase.GetCurrentUserUseCase
import com.study.profile.domain.usecase.GetUserPresenceUseCase
import com.study.profile.domain.usecase.GetUserUseCase
import com.study.profile.presentation.util.mapper.toUiPresenceStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.switcher.Switcher
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.switch
import javax.inject.Inject

internal class ProfileActor @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserPresenceUseCase: GetUserPresenceUseCase,
    private val getCurrentUserPresenceUseCase: GetCurrentUserPresenceUseCase
) : Actor<ProfileCommand, ProfileEvent.Internal> {

    private val switcher = Switcher()
    override fun execute(command: ProfileCommand): Flow<ProfileEvent.Internal> =
        when (command) {
            is ProfileCommand.LoadUser -> switcher.switch {
                flow {
                    emit(
                        getUserUseCase(command.userId)
                            .toUiPresenceStatus(
                                getUserPresenceUseCase(command.userId).toUiPresenceStatus()
                            )
                    )
                }.mapEvents(
                    ProfileEvent.Internal::LoadingUserSuccess,
                    ProfileEvent.Internal::ErrorLoadingUser
                )
            }

            ProfileCommand.LoadCurrentUser -> switcher.switch {
                flow {
                    emit(
                        getCurrentUserUseCase().toUiPresenceStatus(
                            getCurrentUserPresenceUseCase().toUiPresenceStatus()
                        )
                    )
                }.mapEvents(
                    ProfileEvent.Internal::LoadingUserSuccess,
                    ProfileEvent.Internal::ErrorLoadingUser
                )
            }
        }


}
