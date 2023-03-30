package com.study.channels.presentation

import androidx.lifecycle.SavedStateHandle
import com.study.channels.data.StubChannelRepository
import com.study.channels.domain.model.Channel
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.presentation.mapper.toUiChannelTopics
import com.study.channels.presentation.mapper.toUiChannels
import com.study.channels.presentation.model.UiChannel
import com.study.channels.presentation.model.UiChannelModel
import com.study.channels.presentation.model.UiChannelTopic
import com.study.components.ScreenState
import com.study.components.map
import com.study.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.TreeMap

internal class ChannelsViewModel(savedStateHandle: SavedStateHandle) :
    SearchViewModel<TreeMap<UiChannel, List<UiChannelTopic>>>(savedStateHandle) {
    private val channelRepository: ChannelRepository = StubChannelRepository()
    private var channelsFullMap: TreeMap<UiChannel, List<UiChannelTopic>> = TreeMap()
    private var channelSetting: ChannelsFragment.ChannelsSettings =
        ChannelsFragment.ChannelsSettings.SUBSCRIBED_ONLY
    override val searchAction: suspend (query: String) -> TreeMap<UiChannel, List<UiChannelTopic>>
        get() = { searchCurrentChannels(it) }
    override val lastSearchedKey: String? = null
    override val _state: MutableStateFlow<ScreenState<TreeMap<UiChannel, List<UiChannelTopic>>>> =
        MutableStateFlow(ScreenState.Loading)
    val state: Flow<ScreenState<List<UiChannelModel>>> =
        _state.map { state -> state.map { treemap -> treemap.toList() } }

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
        }
        this@ChannelsViewModel._state.value = ScreenState.Success(toTreeMap(data))
    }

    private suspend fun manageTopicsForChannel(channelId: Int) = withContext(Dispatchers.Default) {
        (_state.value as? ScreenState.Success)?.data?.let { map ->
            map.ceilingKey(UiChannel(channelId, "", false))?.let { selectedChannel ->
                selectedChannel.isCollapsed = !selectedChannel.isCollapsed
                if (selectedChannel.isCollapsed && map[selectedChannel]?.isEmpty() == true) {
                    val topics = channelRepository.getStreamTopics(channelId)
                    map[selectedChannel] = topics.toUiChannelTopics(channelId)
                }
                _state.value = ScreenState.Success(map)
            }
        }
    }

    private suspend fun toTreeMap(channels: List<Channel>): TreeMap<UiChannel, List<UiChannelTopic>> =
        withContext(Dispatchers.Default) {
            val map = TreeMap<UiChannel, List<UiChannelTopic>>()
            channels.toUiChannels().forEach { channel -> map[channel] = emptyList() }
            return@withContext map
        }

    private suspend fun Map<UiChannel, List<UiChannelTopic>>.toList(): List<UiChannelModel> =
        withContext(Dispatchers.Default) {
            val resList = mutableListOf<UiChannelModel>()
            forEach { (channel, topics) ->
                resList.add(channel)
                if (channel.isCollapsed) resList.addAll(topics)
            }
            return@withContext resList
        }

    private suspend fun searchCurrentChannels(query: String): TreeMap<UiChannel, List<UiChannelTopic>> =
        withContext(Dispatchers.Default) {
            (_state.value as? ScreenState.Success)?.data?.let { map ->
                if (channelsFullMap.size < map.size) channelsFullMap = map
                TreeMap(channelsFullMap.filterKeys { channel ->
                    channel.title.startsWith(query)
                })
            } ?: TreeMap()
        }
}
