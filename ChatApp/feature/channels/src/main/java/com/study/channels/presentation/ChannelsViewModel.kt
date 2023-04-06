package com.study.channels.presentation

import androidx.lifecycle.SavedStateHandle
import com.study.channels.data.RemoteChannelRepository
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.presentation.model.UiChannel
import com.study.channels.presentation.model.UiChannelModel
import com.study.channels.presentation.model.UiChannelTopic
import com.study.channels.presentation.util.mapper.toChannelsList
import com.study.channels.presentation.util.mapper.toChannelsTreeMap
import com.study.channels.presentation.util.mapper.toUiChannelTopics
import com.study.common.ScreenState
import com.study.common.map
import com.study.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.TreeMap

internal class ChannelsViewModel(savedStateHandle: SavedStateHandle) :
    SearchViewModel<TreeMap<UiChannel, List<UiChannelTopic>>>(savedStateHandle) {
    private val channelRepository: ChannelRepository = RemoteChannelRepository()
    private var channelsFullMap: TreeMap<UiChannel, List<UiChannelTopic>> = TreeMap()
    private var channelSetting: ChannelsFragment.ChannelsSettings =
        ChannelsFragment.ChannelsSettings.SUBSCRIBED_ONLY
    override val searchAction: suspend (query: String) -> TreeMap<UiChannel, List<UiChannelTopic>>
        get() = { searchCurrentChannels(it) }
    override val lastSearchedKey: String? = null

    override val _state: MutableStateFlow<ScreenState<TreeMap<UiChannel, List<UiChannelTopic>>>> =
        MutableStateFlow(ScreenState.Loading)
    val state: Flow<ScreenState<List<UiChannelModel>>> =
        _state.map { state -> state.map { treemap -> treemap.toChannelsList() } }

    init {
        observeSearchQuery()
    }

    fun updateChannelSettings(newSettings: ChannelsFragment.ChannelsSettings) {
        channelSetting = newSettings
        loadData()
    }

    fun onEvent(event: ChannelFragmentEvent) {
        safeLaunch {
            when (event) {
                ChannelFragmentEvent.Reload -> loadData()
                is ChannelFragmentEvent.Search -> search(event.query)
                is ChannelFragmentEvent.UpdateChannelTopics -> manageTopicsForChannel(event.channelId)
            }
        }
    }

    private fun loadData() = safeLaunch {
        val data = if (channelSetting == ChannelsFragment.ChannelsSettings.ALL) {
            channelRepository.getAll()
        } else {
            channelRepository.getSubscribedStreams()
        }.toChannelsTreeMap()
        channelsFullMap = data
        this@ChannelsViewModel._state.value = ScreenState.Success(data)
    }

    private suspend fun manageTopicsForChannel(channelId: Int) = withContext(Dispatchers.Default) {
        (_state.value as? ScreenState.Success)?.data?.let { map ->
            map.ceilingKey(UiChannel(channelId, "", false))?.let { selectedChannel ->
                if (selectedChannel.isCollapsed && map[selectedChannel]?.isEmpty() == true) {
                    val topics = channelRepository.getStreamTopics(channelId)
                    map[selectedChannel] =
                        topics.toUiChannelTopics(channelId, selectedChannel.title)
                }
                _state.value = ScreenState.Success(map)
            }
        }
    }

    private suspend fun searchCurrentChannels(query: String): TreeMap<UiChannel, List<UiChannelTopic>> =
        withContext(Dispatchers.Default) {
            (_state.value as? ScreenState.Success)?.data?.let { _ ->
                TreeMap(channelsFullMap.filterKeys { channel ->
                    channel.title.startsWith(query)
                })
            } ?: TreeMap()
        }
}
