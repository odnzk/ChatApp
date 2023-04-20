package com.study.channels.di

import com.study.channels.presentation.ChannelsFragment
import com.study.common.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ChannelsDep::class],
    modules = [ChannelsRepositoryModule::class, ChannelsModule::class]
)
internal interface ChannelsComponent {
    fun inject(fragment: ChannelsFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: ChannelsDep): ChannelsComponent
    }
}

