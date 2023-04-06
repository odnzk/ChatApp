package com.study.users.presentation

internal sealed interface UsersFragmentEvent {
    object Reload : UsersFragmentEvent
    class Search(val query: String) : UsersFragmentEvent
}
