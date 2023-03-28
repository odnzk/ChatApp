package com.study.channels.presentation.model

internal sealed interface UiChannelModel

internal data class UiChannel(
    val id: Int,
    val title: String,
    var isCollapsed: Boolean
) : UiChannelModel, Comparable<UiChannel> {
    override fun compareTo(other: UiChannel): Int = this.id - other.id
}

internal data class UiChannelTopic(
    val channelId: Int,
    val title: String,
    val lastMessageId: Int,
    val messagesCount: Int,
    val backgroundColor: Int
) : UiChannelModel
