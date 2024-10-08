package com.study.channels.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface ChannelsDepProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: ChannelsDep

    companion object : ChannelsDepProvider by ChannelsDepStore
}

object ChannelsDepStore : ChannelsDepProvider {
    override var dep: ChannelsDep by notNull()
}

internal class ChannelsComponentViewModel : ViewModel() {
    val channelsComponent by lazy {
        DaggerChannelsComponent.factory()
            .create(ChannelsDepStore.dep)
    }

    override fun onCleared() {
        super.onCleared()
        channelsComponent.run {
            addChannelStoreHolder.store.stop()
            channelsStoreHolder.store.stop()
        }
    }
}
