package com.study.channels.domain.model

data class SubscribedChannel(
    val id: Int,
    val title: String,
    val description: String,
    val isPrivate: Boolean,
    val subscribersId: List<Int>,
    val isMuted: Boolean,
    val isPinned: Boolean,
    val userColor: String,
    val streamPostPolicy: StreamPostPolicy,
    val topics: List<ChannelTopic>
)
