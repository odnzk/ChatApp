package com.study.channels.presentation.channels.util.model

import com.study.components.recycler.shimmer.ShimmerItem

internal sealed interface UiChannelModel

internal data class UiChannel(
    val id: Int,
    val title: String,
    val isCollapsed: Boolean
) : UiChannelModel, ShimmerItem<UiChannel> {
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
    val messagesCount: Int,
    val backgroundColor: Int
) : UiChannelModel
