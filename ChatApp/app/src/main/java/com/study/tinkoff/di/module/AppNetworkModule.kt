package com.study.tinkoff.di.module

import android.content.Context
import coil.ImageLoader
import com.study.network.api.AuthApi
import com.study.network.api.ChannelsApi
import com.study.network.api.MessagesApi
import com.study.network.api.UsersApi
import com.study.network.di.NetworkDep
import com.study.network.di.NetworkProvider
import com.study.network.di.NetworkProviderFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppNetworkModule {
    @Provides
    @Singleton
    fun providesNetworkDep(context: Context): NetworkDep = object : NetworkDep {
        override val context: Context = context
    }

    @Provides
    @Singleton
    fun providesNetworkImpl(networkDep: NetworkDep): NetworkProvider =
        NetworkProviderFactory.create(networkDep)

    @Provides
    fun provideMessagesApi(impl: NetworkProvider): MessagesApi = impl.messagesApi

    @Provides
    fun provideChannelsApi(impl: NetworkProvider): ChannelsApi = impl.channelsApi

    @Provides
    fun provideUsersApi(impl: NetworkProvider): UsersApi = impl.usersApi

    @Provides
    fun provideAuthApi(impl: NetworkProvider): AuthApi = impl.authApi

    @Provides
    fun provideCoilLoader(impl: NetworkProvider): ImageLoader = impl.imageLoader
}
