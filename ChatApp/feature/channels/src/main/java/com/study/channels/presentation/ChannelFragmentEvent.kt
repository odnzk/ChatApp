package com.study.channels.presentation

internal sealed interface ChannelFragmentEvent {
    object Reload : ChannelFragmentEvent
    class Search(val query: String) : ChannelFragmentEvent
    class UpdateChannelTopics(val channelId: Int) : ChannelFragmentEvent
}
