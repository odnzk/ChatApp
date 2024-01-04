package com.study.channels.presentation.channels.elm

import com.study.channels.presentation.channels.model.UiChannelFilter
import com.study.channels.presentation.channels.model.UiChannelModel


internal data class ChannelsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: Throwable? = null,
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

    class Search(val filter: UiChannelFilter, val query: String) : ChannelsCommand
}

internal sealed interface ChannelsEffect {
    class ShowWarning(val error: Throwable) : ChannelsEffect
    class ShowSynchronizationError(val error: Throwable) : ChannelsEffect
}

internal sealed interface ChannelsEvent {
    sealed interface Ui : ChannelsEvent {
        class Init(val filter: UiChannelFilter) : Ui
        class Reload(val filter: UiChannelFilter) : Ui
        class ManageChannelTopics(val channelId: Int, val isCollapsed: Boolean) : Ui

        class Search(val filter: UiChannelFilter, val query: String) : Ui
    }

    sealed interface Internal : ChannelsEvent {
        class ChannelsWereLoaded(val filter: UiChannelFilter) : Internal
        class ReceivedChannelsFromDatabase(val channels: Map<Int, List<UiChannelModel>>) :
            Internal

        class LoadingError(val error: Throwable) : Internal
    }
}
