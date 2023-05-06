package com.study.channels.di

import com.study.channels.presentation.addChannel.AddChannelFragment
import com.study.channels.presentation.channels.ChannelsFragment
import com.study.common.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ChannelsDep::class],
    modules = [ChannelsRepositoryModule::class, ChannelsModule::class]
)
internal interface ChannelsComponent {
    fun inject(fragment: ChannelsFragment)
    fun inject(fragment: AddChannelFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChannelsDep): ChannelsComponent
    }
}

