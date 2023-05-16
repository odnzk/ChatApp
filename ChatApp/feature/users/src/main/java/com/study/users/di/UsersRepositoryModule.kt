package com.study.users.di

import com.study.users.data.UserRepositoryImpl
import com.study.users.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
internal interface UsersRepositoryModule {
    @Reusable
    @Binds
    fun bindsUserRepository(impl: UserRepositoryImpl): UsersRepository
}
