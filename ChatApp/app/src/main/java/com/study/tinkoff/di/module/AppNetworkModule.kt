package com.study.tinkoff.di.module

import android.content.Context
import com.study.network.dataSource.ChannelRemoteDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.network.dataSource.UserDataSource
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkImpl
import com.study.network.di.NetworkImplFactory
import com.study.network.di.UserCredentials
import com.study.tinkoff.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppNetworkModule {

    @Provides
    @Singleton
    fun providesCredentials(): UserCredentials =
        UserCredentials(BuildConfig.USERNAME, BuildConfig.PASSWORD)

    @Provides
    @Singleton
    fun providesBaseUrl(): String = "https://tinkoff-android-spring-2023.zulipchat.com/api/v1/"

    @Provides
    @Singleton
    fun providesNetworkDep(
        context: Context,
        credentials: UserCredentials,
        baseUrl: String
    ): NetworkDep =
        object : NetworkDep {
            override val context: Context = context
            override val credentials = credentials
            override val baseUrl: String = baseUrl
        }

    @Provides
    @Singleton
    fun providesNetworkImpl(networkDep: NetworkDep): NetworkImpl =
        NetworkImplFactory.create(networkDep)

    @Provides
    fun provideMessageDataSource(impl: NetworkImpl): MessageRemoteDataSource =
        impl.messageDataSource

    @Provides
    fun provideStreamDataSource(impl: NetworkImpl): ChannelRemoteDataSource = impl.streamDataSource

    @Provides
    fun provideUserDataSource(impl: NetworkImpl): UserDataSource = impl.userDataSource
}
