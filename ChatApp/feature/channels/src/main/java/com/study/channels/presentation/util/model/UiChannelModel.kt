package com.study.channels.presentation.util.model

import android.os.Parcelable
import com.study.components.recycler.shimmer.ShimmerItem
import kotlinx.parcelize.Parcelize

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
    val lastMessageId: Int,
    val messagesCount: Int,
    val backgroundColor: Int
) : UiChannelModel

@Parcelize
internal enum class ChannelFilter : Parcelable {
    ALL, SUBSCRIBED_ONLY
}
