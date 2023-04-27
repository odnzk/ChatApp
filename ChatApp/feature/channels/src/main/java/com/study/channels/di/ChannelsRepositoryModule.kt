package com.study.channels.di

import com.study.channels.data.ChannelRepositoryImpl
import com.study.channels.domain.repository.ChannelRepository
import dagger.Binds
import dagger.Module

@Module
internal interface ChannelsRepositoryModule {

    @Binds
    fun bindsChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

}
