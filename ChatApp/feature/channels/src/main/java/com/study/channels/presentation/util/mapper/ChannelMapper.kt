package com.study.channels.presentation.util.mapper

import android.graphics.Color
import com.study.channels.domain.model.Channel
import com.study.channels.domain.model.ChannelTopic
import com.study.channels.presentation.util.model.UiChannel
import com.study.channels.presentation.util.model.UiChannelModel
import com.study.channels.presentation.util.model.UiChannelTopic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val mapperDefaultDispatcher = Dispatchers.Default
private suspend fun Channel.toUiChannel(
    isCollapsed: Boolean = false,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiChannel =
    withContext(dispatcher) { UiChannel(id, title, isCollapsed) }

private suspend fun ChannelTopic.toUiChannelTopic(
    channelId: Int,
    channelTitle: String,
    prevTopicLastMessageId: Int = 0,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiChannelTopic = withContext(dispatcher) {
    UiChannelTopic(
        channelId = channelId,
        channelTitle = channelTitle,
        title = title,
        lastMessageId = lastMessageId,
        messagesCount = lastMessageId - prevTopicLastMessageId,
        backgroundColor = generateColor()
    )
}

internal suspend fun List<ChannelTopic>.toUiChannelTopics(
    channelId: Int,
    channelTitle: String,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): List<UiChannelTopic> =
    withContext(dispatcher) {
        var prevLastMesId = 0
        val resList = mutableListOf<UiChannelTopic>()
        sortedBy { it.lastMessageId }.forEach { topic ->
            resList.add(
                topic.toUiChannelTopic(
                    channelId = channelId, channelTitle = channelTitle, prevLastMesId
                )
            )
            prevLastMesId = topic.lastMessageId
        }
        resList
    }

private val colorAllowedChars = ('A'..'F') + ('a'..'f') + ('0'..'9')
private fun generateColor(): Int =
    Color.parseColor("#" + (1..6).map { colorAllowedChars.random() }.joinToString(""))

internal suspend fun List<Channel>.toChannelsMap(
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): Map<Int, List<UiChannelModel>> =
    withContext(dispatcher) {
        this@toChannelsMap.associate { channel -> channel.id to listOf(channel.toUiChannel()) }
    }

internal suspend fun Map<Int, List<UiChannelModel>>.toChannelsList(
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): List<UiChannelModel> =
    withContext(dispatcher) {
        val resList = mutableListOf<UiChannelModel>()
        values.forEach { value ->
            val channel = value.first { it is UiChannel } as UiChannel
            resList.add(channel)
            if (channel.isCollapsed) {
                resList.addAll(value.toMutableList().apply { remove(channel) })
            }
        }
        resList
    }
