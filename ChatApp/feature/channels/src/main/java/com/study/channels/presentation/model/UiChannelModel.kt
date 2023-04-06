package com.study.channels.presentation.model

import com.study.components.recycler.shimmer.ShimmerItem

internal sealed interface UiChannelModel

internal data class UiChannel(
    val id: Int,
    val title: String,
    var isCollapsed: Boolean
) : UiChannelModel, Comparable<UiChannel>, ShimmerItem<UiChannel> {
    override fun compareTo(other: UiChannel): Int = this.id - other.id
    override fun content(): UiChannel = this
}

internal object UiChannelShimmer : UiChannelModel, ShimmerItem<UiChannel> {
    override fun content(): UiChannel? = null
    const val DEFAULT_SHIMMER_COUNT = 12
}

internal data class UiChannelTopic(
    val channelId: Int,
    val channelTitle: String,
    val title: String,
    val lastMessageId: Int,
    val messagesCount: Int,
    val backgroundColor: Int
) : UiChannelModel
