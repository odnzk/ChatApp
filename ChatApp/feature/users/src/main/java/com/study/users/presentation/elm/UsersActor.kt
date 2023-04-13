package com.study.users.presentation.elm

import com.study.common.extensions.toFlow
import com.study.users.domain.usecase.GetUsersPresenceCase
import com.study.users.domain.usecase.GetUsersUseCase
import com.study.users.domain.usecase.SearchUsersUseCase
import com.study.users.presentation.util.mapper.toUiUsers
import kotlinx.coroutines.flow.Flow
import vivid.money.elmslie.coroutines.Actor

internal class UsersActor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getUsersPresenceUseCase: GetUsersPresenceCase,
    private val searchUsersUseCase: SearchUsersUseCase
) :
    Actor<UsersCommand, UsersEvent.Internal> {

    override fun execute(command: UsersCommand): Flow<UsersEvent.Internal> =
        when (command) {
            is UsersCommand.LoadUsers ->
                toFlow {
                    getUsersUseCase().toUiUsers(getUsersPresenceUseCase())
                }.mapEvents(
                    UsersEvent.Internal::LoadingUsersSuccess,
                    UsersEvent.Internal::LoadingUsersError
                )
            is UsersCommand.SearchUsers -> toFlow {
                searchUsersUseCase(command.query).toUiUsers(getUsersPresenceUseCase())
            }.mapEvents(
                UsersEvent.Internal::LoadingUsersSuccess,
                UsersEvent.Internal::LoadingUsersError
            )
        }
}
