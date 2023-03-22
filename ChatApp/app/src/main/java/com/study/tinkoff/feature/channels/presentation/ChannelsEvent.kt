package com.study.tinkoff.feature.channels.presentation

sealed interface ChannelsEvent {
    object Reload : ChannelsEvent
    class Search(val query: String?) : ChannelsEvent
    class UpdateChannelTopics(val channelId: Int) : ChannelsEvent
}
