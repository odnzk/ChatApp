package com.study.tinkoff.di

import android.content.Context
import coil.ImageLoader
import com.odnzk.auth.di.FeatureAuthDep
import com.study.auth.api.di.AuthDep
import com.study.channels.di.ChannelsDep
import com.study.chat.common.di.ChatDep
import com.study.network.di.NetworkDep
import com.study.profile.di.ProfileDep
import com.study.tinkoff.di.module.AppAuthModule
import com.study.tinkoff.di.module.AppBindsModule
import com.study.tinkoff.di.module.AppDatabaseModule
import com.study.tinkoff.di.module.AppModule
import com.study.tinkoff.di.module.AppNetworkModule
import com.study.tinkoff.presentation.MainActivity
import com.study.users.di.UsersDep
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppNetworkModule::class,
        AppAuthModule::class,
        AppBindsModule::class,
        AppDatabaseModule::class
    ]
)
interface AppComponent : ProfileDep, UsersDep, ChatDep, AuthDep, ChannelsDep, NetworkDep, FeatureAuthDep {
    fun inject(activity: MainActivity)

    val imageLoader: ImageLoader

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
