package com.study.channels.channels.presentation.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import com.study.components.recycler.shimmer.ShimmerItem
import kotlinx.parcelize.Parcelize

internal sealed interface UiChannelModel

@Parcelize
internal enum class UiChannelFilter : Parcelable {
    ALL, SUBSCRIBED_ONLY;

    fun isSubscribed(): Boolean = when (this) {
        ALL -> false
        SUBSCRIBED_ONLY -> true
    }
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
    @ColorInt
    val backgroundColor: Int?
) : UiChannelModel
