package com.study.network.di

import com.study.network.repository.MessageDataSource
import com.study.network.repository.StreamDataSource
import com.study.network.repository.UserDataSource
import com.study.network.repository.impl.ZulipMessageDataSource
import com.study.network.repository.impl.ZulipStreamDataSource
import com.study.network.repository.impl.ZulipUserDataSource
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface NetworkRepositoryModule {
    @Binds
    @Reusable
    fun bindsMessageRepository(impl: ZulipMessageDataSource): MessageDataSource

    @Binds
    @Reusable
    fun bindsUserRepository(impl: ZulipUserDataSource): UserDataSource

    @Binds
    @Reusable
    fun bindsStreamRepository(impl: ZulipStreamDataSource): StreamDataSource
}
