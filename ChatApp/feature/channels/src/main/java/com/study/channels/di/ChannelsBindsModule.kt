package com.study.channels.di

import com.study.channels.data.ChannelRepositoryImpl
import com.study.channels.domain.model.Channel
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.domain.util.ChannelValidator
import com.study.common.Validator
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface ChannelsBindsModule {

    @Reusable
    @Binds
    fun bindsChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @Reusable
    @Binds
    fun bindsValidator(impl: ChannelValidator): Validator<Channel>

}
