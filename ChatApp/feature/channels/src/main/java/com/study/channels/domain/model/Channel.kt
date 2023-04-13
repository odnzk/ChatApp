package com.study.channels.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Channel(
    val id: Int,
    val title: String,
    val description: String,
    val isPrivate: Boolean
)

@Parcelize
enum class ChannelFilter : Parcelable {
    ALL, SUBSCRIBED_ONLY
}

