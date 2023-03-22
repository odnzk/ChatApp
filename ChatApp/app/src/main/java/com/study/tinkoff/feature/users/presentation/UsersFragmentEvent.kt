package com.study.tinkoff.feature.users.presentation

sealed interface UsersFragmentEvent {
    object Reload : UsersFragmentEvent
}
