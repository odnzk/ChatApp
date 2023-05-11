package com.study.profile.domain.model

internal data class UserDetailed(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val isBot: Boolean,
    val isActive: Boolean
)
