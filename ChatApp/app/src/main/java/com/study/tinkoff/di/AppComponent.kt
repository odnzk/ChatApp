package com.study.tinkoff.di

import android.content.Context
import com.study.auth.api.di.AuthDep
import com.study.channels.di.ChannelsDep
import com.study.chat.di.ChatDep
import com.study.network.di.NetworkDep
import com.study.profile.di.ProfileDeps
import com.study.tinkoff.MainActivity
import com.study.users.di.UsersDep
import dagger.BindsInstance
import dagger.Component

@[AppScope Component(modules = [AppModule::class, AppBindsModule::class])]
interface AppComponent : ProfileDeps, UsersDep, ChatDep, AuthDep, ChannelsDep, NetworkDep {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
