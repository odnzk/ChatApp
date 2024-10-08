package com.study.users.presentation.elm

import com.study.users.presentation.model.UiUser

internal data class UsersState(
    val isLoading: Boolean = false,
    val users: List<UiUser> = emptyList(),
    val error: Throwable? = null,
    val searchQuery: String = ""
)

internal sealed interface UsersCommand {
    data object LoadUsers : UsersCommand
    class SearchUsers(val query: String) : UsersCommand
}

internal sealed interface UsersEffect

internal sealed interface UsersEvent {
    sealed interface Ui : UsersEvent {
        data object Init : Ui
        data object Reload : Ui
        class Search(val query: String) : Ui
    }

    sealed interface Internal : UsersEvent {
        class LoadingUsersSuccess(val users: List<UiUser>) : Internal
        class LoadingUsersError(val error: Throwable) : Internal
        class SearchSuccess(val users: List<UiUser>) : Internal
        class SearchError(val error: Throwable) : Internal
    }
}
