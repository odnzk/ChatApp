package com.study.profile.di

import com.study.profile.data.RemoteUserRepository
import com.study.profile.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface ProfileRepositoryModule {
    @Reusable
    @Binds
    fun bindsUserRepository(impl: RemoteUserRepository): UserRepository
}
