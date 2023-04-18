package com.study.channels.domain.model

data class Channel(
    val id: Int,
    val title: String,
    val description: String,
    val isPrivate: Boolean
)

