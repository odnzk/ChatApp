package com.study.channels.di.module

import com.study.channels.data.source.local.dao.ChannelDao
import com.study.channels.data.source.local.dao.ChannelTopicDao
import com.study.channels.di.ChannelsDatabase
import com.study.channels.di.annotation.ChannelsScope
import com.study.channels.di.annotation.SearchFlow
import com.study.channels.presentation.addChannel.elm.AddChannelActor
import com.study.channels.presentation.addChannel.elm.AddChannelEffect
import com.study.channels.presentation.addChannel.elm.AddChannelEvent
import com.study.channels.presentation.addChannel.elm.AddChannelReducer
import com.study.channels.presentation.addChannel.elm.AddChannelState
import com.study.channels.presentation.channels.elm.ChannelsActor
import com.study.channels.presentation.channels.elm.ChannelsEffect
import com.study.channels.presentation.channels.elm.ChannelsEvent
import com.study.channels.presentation.channels.elm.ChannelsReducer
import com.study.channels.presentation.channels.elm.ChannelsState
import com.study.channels.presentation.channels.model.SearchEvent
import com.study.common.di.FeatureScope
import com.study.components.di.ManualStoreHolder
import com.study.network.model.response.stream.TopicDto
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.MutableSharedFlow
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class ChannelsModule {

    @Provides
    @ChannelsScope
    fun providesAddChannelStore(
        actor: AddChannelActor
    ): StoreHolder<AddChannelEvent, AddChannelEffect, AddChannelState> =
        ManualStoreHolder { ElmStoreCompat(AddChannelState(), AddChannelReducer(), actor) }

    @Provides
    @ChannelsScope
    fun providesChannelsStore(
        actor: ChannelsActor,
        reducer: ChannelsReducer
    ): StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState> =
        ManualStoreHolder { ElmStoreCompat(ChannelsState(), reducer, actor) }

    @Provides
    @ChannelsScope
    @SearchFlow
    fun providesSearchFlow(): MutableSharedFlow<SearchEvent> = MutableSharedFlow()


    @Provides
    @ChannelsScope
    fun providesChannelsDao(channelsDatabase: ChannelsDatabase): ChannelDao =
        channelsDatabase.channelsDao()

    @Provides
    @ChannelsScope
    fun providesTopicsDatabase(channelsDatabase: ChannelsDatabase): ChannelTopicDao =
        channelsDatabase.topicsDao()

}
