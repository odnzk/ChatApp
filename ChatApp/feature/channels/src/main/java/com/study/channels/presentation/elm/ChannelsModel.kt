package com.study.channels.presentation.elm

import com.study.channels.domain.model.ChannelFilter
import com.study.channels.presentation.util.model.UiChannelModel


internal data class ChannelsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val channelFilter: ChannelFilter = ChannelFilter.SUBSCRIBED_ONLY,
    val channelsWithTopics: Map<Int, List<UiChannelModel>> = hashMapOf()
)

internal sealed interface ChannelsCommand {
    class LoadChannels(val filter: ChannelFilter) : ChannelsCommand
    class ManageChannelTopics(
        val channelId: Int,
        val currentChannelsMap: Map<Int, List<UiChannelModel>>
    ) : ChannelsCommand

    class SearchChannels(val query: String, val filter: ChannelFilter) : ChannelsCommand
}

internal sealed interface ChannelsEffect {
    class ShowError(val error: Throwable) : ChannelsEffect
    class ShowWarning(val error: Throwable) : ChannelsEffect
}

internal sealed interface ChannelsEvent {
    sealed interface Ui : ChannelsEvent {
        object Init : Ui
        object Reload : Ui
        class Search(val query: String) : Ui
        class ManageChannelTopics(val channelId: Int) : Ui
    }

    sealed interface Internal : ChannelsEvent {
        class LoadingChannelsWithTopicsSuccess(val channels: Map<Int, List<UiChannelModel>>) :
            Internal

        class LoadingError(val error: Throwable) : Internal
    }
}
