package com.study.channels.di.module

import com.study.channels.data.ChannelRepositoryImpl
import com.study.channels.di.annotation.SearchFlow
import com.study.channels.domain.repository.ChannelRepository
import com.study.channels.presentation.channels.model.SearchEvent
import com.study.channels.domain.model.Channel
import com.study.channels.domain.util.ChannelValidator
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
    fun bindsValidator(impl: ChannelValidator): Validator<Channel>

    @Binds
    fun bindsSearchFlow(@SearchFlow impl: MutableSharedFlow<SearchEvent>): Flow<SearchEvent>

}
