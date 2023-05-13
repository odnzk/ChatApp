package com.study.channels.presentation.channels.util.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import com.study.components.recycler.shimmer.ShimmerItem
import kotlinx.parcelize.Parcelize

internal sealed interface UiChannelModel

@Parcelize
internal enum class UiChannelFilter : Parcelable {
    ALL, SUBSCRIBED_ONLY
}

internal data class UiChannel(
    val id: Int,
    val title: String,
    val isCollapsed: Boolean,
    @ColorInt
    val color: Int?
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
    @ColorInt
    val backgroundColor: Int?
) : UiChannelModel