package com.study.users.presentation.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class UsersReducer @Inject constructor() :
    DslReducer<UsersEvent, UsersState, UsersEffect, UsersCommand>() {
    override fun Result.reduce(event: UsersEvent) = when (event) {
        UsersEvent.Ui.Init -> if (state.users.isEmpty()) {
            state { copy(isLoading = true) }
            commands { +UsersCommand.LoadUsers }
        } else Unit

        is UsersEvent.Internal.LoadingUsersError -> {
            state { copy(isLoading = false, error = event.error) }
        }

        is UsersEvent.Internal.LoadingUsersSuccess -> {
            state { copy(isLoading = false, users = event.users, error = null) }
        }

        UsersEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +UsersCommand.LoadUsers }
        }

        is UsersEvent.Ui.Search -> {
            state { copy(isLoading = true, error = null) }
            commands { +UsersCommand.SearchUsers(event.query) }
        }

        is UsersEvent.Internal.SearchError -> {
            state { copy(isLoading = false, error = event.error) }
        }

        is UsersEvent.Internal.SearchSuccess -> {
            state { copy(isLoading = false, users = event.users, error = null) }
        }
    }
}
