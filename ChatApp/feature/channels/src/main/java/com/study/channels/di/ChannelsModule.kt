package com.study.channels.di

import com.study.channels.domain.model.Channel
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
import com.study.common.di.FeatureScope
import com.study.common.search.Searcher
import dagger.Module
import dagger.Provides
import dagger.Reusable
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class ChannelsModule {

    @Provides
    @FeatureScope
    fun providesStore(
        reducer: ChannelsReducer,
        actor: ChannelsActor
    ): Store<ChannelsEvent, ChannelsEffect, ChannelsState> {
        return ElmStoreCompat(ChannelsState(), reducer, actor)
    }

    @Provides
    @FeatureScope
    fun providesAddChannelStore(actor: AddChannelActor): Store<AddChannelEvent, AddChannelEffect, AddChannelState> =
        ElmStoreCompat(AddChannelState(), AddChannelReducer(), actor)

    @Provides
    @Reusable
    fun providesChannelSearcher(): Searcher<Channel> = object : Searcher<Channel> {
        override val searchPredicate: (Channel, String) -> Boolean = { channel, query ->
            channel.title.contains(query, ignoreCase = true)
        }
    }
}
