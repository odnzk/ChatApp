package com.study.tinkoff.feature.profile.presentation


sealed interface ProfileFragmentEvent {
    object Reload : ProfileFragmentEvent
    object Logout : ProfileFragmentEvent
}
