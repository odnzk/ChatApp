package com.study.users.presentation.elm

import com.study.users.presentation.util.model.UiUser

internal data class UsersState(
    val isLoading: Boolean = false,
    val users: List<UiUser> = emptyList(),
    val searchQuery: String = ""
)

internal sealed interface UsersCommand {
    object LoadUsers : UsersCommand
    class SearchUsers(val query: String) : UsersCommand
}

internal sealed interface UsersEffect {
    class ShowError(val error: Throwable) : UsersEffect
}

internal sealed interface UsersEvent {
    sealed interface Ui : UsersEvent {
        object Init : Ui
        object Reload : Ui
        class Search(val query: String) : Ui
    }

    sealed interface Internal : UsersEvent {
        class LoadingUsersSuccess(val users: List<UiUser>) : Internal
        class LoadingUsersError(val error: Throwable) : Internal
        class SearchSuccess(val users: List<UiUser>) : Internal
        class SearchError(val error: Throwable) : Internal
    }
}
