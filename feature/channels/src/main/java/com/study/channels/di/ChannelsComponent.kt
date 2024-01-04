package com.study.channels.di

import com.study.channels.di.annotation.ChannelsScope
import com.study.channels.di.module.ChannelsBindsModule
import com.study.channels.di.module.ChannelsModule
import com.study.channels.presentation.addChannel.AddChannelFragment
import com.study.channels.presentation.addChannel.elm.AddChannelEffect
import com.study.channels.presentation.addChannel.elm.AddChannelEvent
import com.study.channels.presentation.addChannel.elm.AddChannelState
import com.study.channels.presentation.channels.ChannelsFragment
import com.study.channels.presentation.channels.HolderChannelsFragment
import com.study.channels.presentation.channels.elm.ChannelsEffect
import com.study.channels.presentation.channels.elm.ChannelsEvent
import com.study.channels.presentation.channels.elm.ChannelsState
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

