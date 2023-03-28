package com.study.users.presentation

sealed interface UsersFragmentEvent {
    object Reload : UsersFragmentEvent
    class Search(val query: String) : UsersFragmentEvent
}
