package com.study.channels.presentation.elm

import com.study.channels.domain.model.ChannelFilter
import com.study.channels.domain.usecase.GetChannelTopicsUseCase
import com.study.channels.domain.usecase.GetChannelsUseCase
import com.study.channels.domain.usecase.SearchChannelUseCase
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class ChannelsStoreFactory(
    private val channelFilter: ChannelFilter,
    private val getChannels: GetChannelsUseCase,
    private val getChannelTopics: GetChannelTopicsUseCase,
    private val searchChannels: SearchChannelUseCase
) {
    private val store by lazy {
        ElmStoreCompat(
            ChannelsState(channelFilter = channelFilter),
            ChannelsReducer(),
            ChannelsActor(getChannels, getChannelTopics, searchChannels)
        )
    }

    fun create() = store
}

