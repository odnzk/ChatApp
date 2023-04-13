package com.study.users.presentation.elm

import com.study.users.domain.usecase.GetUsersPresenceCase
import com.study.users.domain.usecase.GetUsersUseCase
import com.study.users.domain.usecase.SearchUsersUseCase
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal object UsersStoreFactory {
    fun create(
        getUsersUseCase: GetUsersUseCase,
        getUsersPresenceUseCase: GetUsersPresenceCase,
        searchUsersUseCase: SearchUsersUseCase
    ) = ElmStoreCompat(
        UsersState(),
        UsersReducer(),
        UsersActor(getUsersUseCase, getUsersPresenceUseCase, searchUsersUseCase)
    )
}
