package com.study.channels.di

import com.study.channels.presentation.elm.ChannelsActor
import com.study.channels.presentation.elm.ChannelsEffect
import com.study.channels.presentation.elm.ChannelsEvent
import com.study.channels.presentation.elm.ChannelsReducer
import com.study.channels.presentation.elm.ChannelsState
import com.study.common.FeatureScope
import dagger.Module
import dagger.Provides
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
}
