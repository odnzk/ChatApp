package com.study.channels.presentation.channels.util.mapper

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.presentation.channels.util.model.UiChannel
import com.study.channels.presentation.channels.util.model.UiChannelModel
import com.study.channels.presentation.channels.util.model.UiChannelTopic

private fun Channel.toUiChannel(isCollapsed: Boolean = false): UiChannel =
    UiChannel(id, title, isCollapsed)

private fun ChannelTopic.toUiChannelTopic(
    channelId: Int,
    channelTitle: String,
    color: Int,
    ratio: Float
): UiChannelTopic =
    UiChannelTopic(
        channelId = channelId,
        channelTitle = channelTitle,
        title = title,
        messagesCount = 0, // todo
        backgroundColor = generateMatchingColor(color, ratio)
    )


internal fun List<ChannelTopic>.toUiChannelTopics(
    channelId: Int,
    channelTitle: String,
    @ColorInt color: Int
): List<UiChannelTopic> =
    sortedBy { it.title }.mapIndexed { index, channel ->
        channel.toUiChannelTopic(channelId, channelTitle, color, (index / BLEND_COLOR_DELIMITER))
    }

private const val BLEND_COLOR_DELIMITER = 30f

private fun generateMatchingColor(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 0.8) ratio: Float
): Int = ColorUtils.blendARGB(
    color,
    Color.WHITE,
    ratio
)


internal fun List<Channel>.toChannelsMap(): Map<Int, List<UiChannelModel>> =
    associate { channel -> channel.id to listOf(channel.toUiChannel()) }

internal fun Map<Int, List<UiChannelModel>>.toChannelsList(): List<UiChannelModel> {
    val resList = mutableListOf<UiChannelModel>()
    values.forEach { channelWithTopics ->
        val channel = channelWithTopics.first { it is UiChannel } as UiChannel
        if (channel.isCollapsed) {
            resList.addAll(channelWithTopics)
        } else {
            resList.add(channel)
        }
    }
    return resList
}
