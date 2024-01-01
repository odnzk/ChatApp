package com.study.channels.channels.presentation.elm

import com.study.channels.channels.presentation.model.UiChannelModel


internal data class ChannelsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: Throwable? = null,
    val channelsWithTopics: Map<Int, List<UiChannelModel>> = hashMapOf()
)

internal sealed interface ChannelsCommand {
    data object GetChannels : ChannelsCommand
    data object LoadChannels : ChannelsCommand
    class LoadChannelTopic(val channelId: Int) : ChannelsCommand
    class ShowChannelTopics(
        val channelId: Int,
        val channelsMap: Map<Int, List<UiChannelModel>>
    ) : ChannelsCommand

    class HideChannelTopics(
        val channelId: Int,
        val channelsMap: Map<Int, List<UiChannelModel>>
    ) : ChannelsCommand
}

internal sealed interface ChannelsEffect {
    class ShowWarning(val error: Throwable) : ChannelsEffect
    class ShowSynchronizationError(val error: Throwable) : ChannelsEffect
}

internal sealed interface ChannelsEvent {
    sealed interface Ui : ChannelsEvent {
        data object Init : Ui
        data object Reload : Ui
        class ManageChannelTopics(val channelId: Int, val isCollapsed: Boolean) : Ui
    }

    sealed interface Internal : ChannelsEvent {
        data object ChannelsWereLoaded : Internal
        class ReceivedChannelsFromDatabase(val channels: Map<Int, List<UiChannelModel>>) :
            Internal

        class LoadingError(val error: Throwable) : Internal
    }
}
