package com.study.channels.common.di

import com.study.channels.addChannel.presentation.AddChannelFragment
import com.study.channels.addChannel.presentation.elm.AddChannelEffect
import com.study.channels.addChannel.presentation.elm.AddChannelEvent
import com.study.channels.addChannel.presentation.elm.AddChannelState
import com.study.channels.channels.presentation.ChannelsFragment
import com.study.channels.channels.presentation.HolderChannelsFragment
import com.study.channels.channels.presentation.elm.ChannelsEffect
import com.study.channels.channels.presentation.elm.ChannelsEvent
import com.study.channels.channels.presentation.elm.ChannelsState
import dagger.Component
import vivid.money.elmslie.android.storeholder.StoreHolder

@ChannelsScope
@Component(
    dependencies = [ChannelsDep::class],
    modules = [ChannelsBindsModule::class, ChannelsModule::class]
)
internal interface ChannelsComponent {
    fun inject(fragment: ChannelsFragment)
    fun inject(fragment: AddChannelFragment)

    fun inject(fragment: HolderChannelsFragment)

    val addChannelStoreHolder: StoreHolder<AddChannelEvent, AddChannelEffect, AddChannelState>
    val channelsStoreHolder: StoreHolder<ChannelsEvent, ChannelsEffect, ChannelsState>

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChannelsDep): ChannelsComponent
    }
}

