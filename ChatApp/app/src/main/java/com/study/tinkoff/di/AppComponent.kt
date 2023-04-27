package com.study.tinkoff.di

import android.content.Context
import com.study.auth.api.di.AuthDep
import com.study.channels.di.ChannelsDep
import com.study.chat.di.ChatDep
import com.study.database.di.DatabaseDep
import com.study.network.di.NetworkDep
import com.study.profile.di.ProfileDep
import com.study.tinkoff.MainActivity
import com.study.tinkoff.di.module.AppAuthModule
import com.study.tinkoff.di.module.AppBindsModule
import com.study.tinkoff.di.module.AppDatabaseModule
import com.study.tinkoff.di.module.AppModule
import com.study.tinkoff.di.module.AppNetworkModule
import com.study.users.di.UsersDep
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppBindsModule::class,
        AppNetworkModule::class,
        AppAuthModule::class,
        AppDatabaseModule::class
    ]
)
interface AppComponent : ProfileDep, UsersDep, ChatDep, AuthDep, ChannelsDep, NetworkDep,
    DatabaseDep {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
