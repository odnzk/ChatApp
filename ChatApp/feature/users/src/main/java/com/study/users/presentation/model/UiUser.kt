package com.study.users.presentation.model

import com.study.components.model.UiUserPresenceStatus
import com.study.components.recycler.shimmer.ShimmerItem

internal data class UiUser(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val isBot: Boolean,
    val presence: UiUserPresenceStatus
) : ShimmerItem<UiUser> {
    override fun content(): UiUser = this
}
