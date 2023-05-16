package com.study.channels.shared.di

import com.study.channels.addChannel.data.AddChannelRepositoryImpl
import com.study.channels.addChannel.domain.repository.AddChannelRepository
import com.study.channels.channels.data.ChannelRepositoryImpl
import com.study.channels.channels.domain.repository.ChannelRepository
import com.study.channels.shared.domain.model.Channel
import com.study.channels.shared.domain.util.ChannelValidator
import com.study.common.validation.Validator
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
    fun bindsAddChannelRepository(impl: AddChannelRepositoryImpl): AddChannelRepository

    @Reusable
    @Binds
    fun bindsValidator(impl: ChannelValidator): Validator<Channel>

}
