package com.study.channels.common.di

import com.study.channels.addChannel.presentation.elm.AddChannelActor
import com.study.channels.addChannel.presentation.elm.AddChannelEffect
import com.study.channels.addChannel.presentation.elm.AddChannelEvent
import com.study.channels.addChannel.presentation.elm.AddChannelReducer
import com.study.channels.addChannel.presentation.elm.AddChannelState
import com.study.channels.channels.presentation.elm.ChannelsActor
import com.study.channels.channels.presentation.elm.ChannelsEffect
import com.study.channels.channels.presentation.elm.ChannelsEvent
import com.study.channels.channels.presentation.elm.ChannelsReducer
import com.study.channels.channels.presentation.elm.ChannelsState
import com.study.channels.channels.presentation.model.SearchEvent
import com.study.components.di.ManualStoreHolder
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

}
