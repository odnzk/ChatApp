package com.study.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val is_active: Boolean
)
