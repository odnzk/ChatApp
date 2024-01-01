package com.study.channels.common.di

import com.study.channels.addChannel.presentation.AddChannelFragment
import com.study.channels.addChannel.presentation.elm.AddChannelEffect
import com.study.channels.addChannel.presentation.elm.AddChannelEvent
import com.study.channels.addChannel.presentation.elm.AddChannelState
import com.study.channels.channels.presentation.ChannelsFragment
import com.study.common.di.FeatureScope
import dagger.Component
import vivid.money.elmslie.android.storeholder.StoreHolder

@FeatureScope
@Component(
    dependencies = [ChannelsDep::class],
    modules = [ChannelsBindsModule::class, ChannelsModule::class]
)
internal interface ChannelsComponent {
    fun inject(fragment: ChannelsFragment)
    fun inject(fragment: AddChannelFragment)

    val addChannelStoreHolder: StoreHolder<AddChannelEvent, AddChannelEffect, AddChannelState>

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChannelsDep): ChannelsComponent
    }
}

