package com.study.users.presentation.util.model

import com.study.components.model.UserPresenceStatus
import com.study.components.recycler.shimmer.ShimmerItem

internal data class UiUser(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val presence: UserPresenceStatus
) : ShimmerItem<UiUser> {
    override fun content(): UiUser = this
}
