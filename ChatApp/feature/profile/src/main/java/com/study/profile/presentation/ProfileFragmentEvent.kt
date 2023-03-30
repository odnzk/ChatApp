package com.study.profile.presentation


internal sealed interface ProfileFragmentEvent {
    object Reload : ProfileFragmentEvent
    object Logout : ProfileFragmentEvent
}
