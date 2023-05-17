package com.study.channels.channels.presentation.elm

import com.study.channels.channels.presentation.model.UiChannelFilter
import com.study.channels.channels.presentation.model.UiChannelModel


internal data class ChannelsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: Throwable? = null,
    val channelFilter: UiChannelFilter = UiChannelFilter.SUBSCRIBED_ONLY,
    val channelsWithTopics: Map<Int, List<UiChannelModel>> = hashMapOf()
)

internal sealed interface ChannelsCommand {
    class GetChannels(val filter: UiChannelFilter) : ChannelsCommand
    class LoadChannels(val filter: UiChannelFilter) : ChannelsCommand
    class LoadChannelTopic(val channelId: Int) : ChannelsCommand
    class ShowChannelTopics(
        val channelId: Int,
        val channelsMap: Map<Int, List<UiChannelModel>>
    ) : ChannelsCommand

    class HideChannelTopics(
        val channelId: Int,
        val channelsMap: Map<Int, List<UiChannelModel>>
    ) : ChannelsCommand

    class SearchChannels(val query: String, val filter: UiChannelFilter) : ChannelsCommand
}

internal sealed interface ChannelsEffect {
    class ShowWarning(val error: Throwable) : ChannelsEffect
    class ShowSynchronizationError(val error: Throwable) : ChannelsEffect
}

internal sealed interface ChannelsEvent {
    sealed interface Ui : ChannelsEvent {
        class Init(val channelFilter: UiChannelFilter) : Ui
        object Reload : Ui
        class Search(val query: String) : Ui
        class ManageChannelTopics(val channelId: Int, val isCollapsed: Boolean) : Ui
    }

    sealed interface Internal : ChannelsEvent {
        class LoadingChannelsWithTopicsSuccess(val channels: Map<Int, List<UiChannelModel>>) :
            Internal

        class LoadingError(val error: Throwable) : Internal
    }
}
