package com.study.tinkoff.feature.channels.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.components.ScreenState
import com.study.components.map
import com.study.domain.model.Channel
import com.study.tinkoff.di.StubDiContainer
import com.study.tinkoff.feature.channels.domain.StreamRepository
import com.study.tinkoff.feature.channels.presentation.mapper.toUiChannelTopics
import com.study.tinkoff.feature.channels.presentation.mapper.toUiChannels
import com.study.tinkoff.feature.channels.presentation.model.UiChannel
import com.study.tinkoff.feature.channels.presentation.model.UiChannelModel
import com.study.tinkoff.feature.channels.presentation.model.UiChannelTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.TreeMap

class ChannelsViewModel : ViewModel() {
    private val channelRepository: StreamRepository = StubDiContainer.bindsChannelsRepository()
    private var channelSetting: ChannelsFragment.ChannelsSettings =
        ChannelsFragment.ChannelsSettings.ALL

    private val _state: MutableStateFlow<ScreenState<TreeMap<UiChannel, List<UiChannelTopic>>>> =
        MutableStateFlow(ScreenState.Loading)
    val state: Flow<ScreenState<List<UiChannelModel>>> =
        _state.map { state -> state.map { treemap -> treemap.toList() } }

    fun updateChannelSettings(newSettings: ChannelsFragment.ChannelsSettings) {
        channelSetting = newSettings
        loadData()
    }

    fun onEvent(event: ChannelsEvent) = viewModelScope.launch {
        when (event) {
            ChannelsEvent.Reload -> loadData()
            is ChannelsEvent.Search -> TODO()
            is ChannelsEvent.UpdateChannelTopics -> manageTopicsForChannel(event.channelId)
        }
    }

    private fun loadData() = viewModelScope.launch {
        val data =
            if (channelSetting == ChannelsFragment.ChannelsSettings.ALL) channelRepository.getAll()
            else channelRepository.getSubscribedStreams()
        _state.value = ScreenState.Success(data.toTreeMap())
    }

    private fun manageTopicsForChannel(channelId: Int) = viewModelScope.launch {
        (_state.value as? ScreenState.Success)?.data?.let { map ->
            val helperChannel = UiChannel(channelId, "", false)
            if (map[helperChannel]?.isEmpty() == true) {
                val topics = channelRepository.getStreamTopics(channelId)
                map[helperChannel] = topics.toUiChannelTopics(channelId)
            }
            _state.update { ScreenState.Success(map) }
        }
    }

    private fun List<Channel>.toTreeMap(): TreeMap<UiChannel, List<UiChannelTopic>> {
        val map = TreeMap<UiChannel, List<UiChannelTopic>>()
        toUiChannels().forEach { channel -> map[channel] = emptyList() }
        return map
    }

    private fun TreeMap<UiChannel, List<UiChannelTopic>>.toList(): List<UiChannelModel> {
        val resList = mutableListOf<UiChannelModel>()
        for ((channel, topics) in this) {
            resList.add(channel)
            if (channel.isCollapsed) resList.addAll(topics)
        }
        return resList
    }
}

