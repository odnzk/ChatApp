package com.study.channels.shared.domain.model

private const val MAX_NOT_SYNCH_ID = 59
internal val notYetSynchronizedChannelId = (1..MAX_NOT_SYNCH_ID)

internal data class Channel(
    val id: Int,
    val title: String,
    val color: String?
)
