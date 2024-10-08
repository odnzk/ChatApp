package com.study.users.domain.model

internal data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val isActive: Boolean,
    val isBot: Boolean
)
