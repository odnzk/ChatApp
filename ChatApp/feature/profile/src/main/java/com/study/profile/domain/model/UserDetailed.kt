package com.study.profile.domain.model

internal data class UserDetailed(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val isActive: Boolean,
    val isBot: Boolean,
    val isAdmin: Boolean,
    val isBillingAdmin: Boolean,
    val isGuest: Boolean,
    val isOwner: Boolean
)
