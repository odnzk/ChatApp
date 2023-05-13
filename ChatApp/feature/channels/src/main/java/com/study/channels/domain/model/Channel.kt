package com.study.channels.domain.model

internal const val NOT_YET_SYNCHRONIZED_ID = -1

internal data class Channel(
    val id: Int,
    val title: String,
    val color: String?
)
