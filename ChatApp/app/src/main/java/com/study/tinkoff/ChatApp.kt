package com.study.tinkoff

import android.app.Application
import com.study.channels.di.ChannelsDepStore
import com.study.chat.di.ChatDepStore
import com.study.profile.di.ProfileDepsStore
import com.study.tinkoff.di.AppComponent
import com.study.tinkoff.di.DaggerAppComponent
import com.study.users.di.UsersDepsStore
import timber.log.Timber
import timber.log.Timber.Forest.plant

class ChatApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) plant(Timber.DebugTree())
        ProfileDepsStore.deps = appComponent
        ChannelsDepStore.dep = appComponent
        UsersDepsStore.deps = appComponent
        ChatDepStore.dep = appComponent
    }
}
