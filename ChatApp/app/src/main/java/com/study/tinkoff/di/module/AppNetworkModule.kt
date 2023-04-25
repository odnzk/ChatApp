package com.study.tinkoff.di.module

import android.content.Context
import com.study.network.dataSource.ChannelRemoteDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.network.dataSource.UserDataSource
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkImpl
import com.study.network.di.NetworkImplFactory
import com.study.tinkoff.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import javax.inject.Singleton

@Module
class AppNetworkModule {
    @Provides
    fun providesNetworkCredentials(): String =
        Credentials.basic(BuildConfig.USERNAME, BuildConfig.PASSWORD)

    @Provides
    @Singleton
    fun providesNetworkDep(credentials: String, context: Context): NetworkDep =
        object : NetworkDep {
            override val context: Context = context
            override val credentials = credentials
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
