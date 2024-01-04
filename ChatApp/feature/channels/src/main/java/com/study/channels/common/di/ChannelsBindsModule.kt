package com.study.channels.common.di

import com.study.channels.addChannel.data.AddChannelRepositoryImpl
import com.study.channels.addChannel.domain.repository.AddChannelRepository
import com.study.channels.channels.data.ChannelRepositoryImpl
import com.study.channels.channels.domain.repository.ChannelRepository
import com.study.channels.channels.presentation.model.SearchEvent
import com.study.channels.common.domain.model.Channel
import com.study.channels.common.domain.util.ChannelValidator
import com.study.common.validation.Validator
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Module
internal interface ChannelsBindsModule {

    @Binds
    fun bindsChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @Binds
    fun bindsAddChannelRepository(impl: AddChannelRepositoryImpl): AddChannelRepository

    @Binds
    fun bindsValidator(impl: ChannelValidator): Validator<Channel>

    @Binds
    fun bindsSearchFlow(@SearchFlow impl: MutableSharedFlow<SearchEvent>): Flow<SearchEvent>

}
