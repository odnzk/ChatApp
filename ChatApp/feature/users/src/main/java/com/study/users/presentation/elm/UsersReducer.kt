package com.study.users.presentation.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class UsersReducer : DslReducer<UsersEvent, UsersState, UsersEffect, UsersCommand>() {

    override fun Result.reduce(event: UsersEvent) = when (event) {
        UsersEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +UsersCommand.LoadUsers }
        }
        is UsersEvent.Internal.LoadingUsersError -> {
            state { copy(isLoading = false) }
            effects { +UsersEffect.ShowError(event.error) }
        }
        is UsersEvent.Internal.LoadingUsersSuccess -> {
            state { copy(isLoading = false, users = event.users) }
        }
        UsersEvent.Ui.Reload -> {
            state { copy(isLoading = true) }
            commands { +UsersCommand.LoadUsers }
        }
        is UsersEvent.Ui.Search -> {
            state { copy(isLoading = true) }
            commands { +UsersCommand.SearchUsers(event.query) }
        }
        is UsersEvent.Internal.SearchError -> {
            state { copy(isLoading = false) }
            effects { +UsersEffect.ShowError(event.error) }
        }
        is UsersEvent.Internal.SearchSuccess -> {
            state { copy(isLoading = false, users = event.users) }
        }
    }
}
