package com.study.channels.di

import com.study.channels.data.RemoteChannelRepository
import com.study.channels.domain.repository.ChannelRepository
import dagger.Binds
import dagger.Module

@Module
internal interface ChannelsRepositoryModule {

    @Binds
    fun bindsChannelRepository(impl: RemoteChannelRepository): ChannelRepository

}
