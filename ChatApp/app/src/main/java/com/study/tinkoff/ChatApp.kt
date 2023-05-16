package com.study.tinkoff

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.study.channels.shared.di.ChannelsDepStore
import com.study.chat.shared.di.ChatDepStore
import com.study.profile.di.ProfileDepStore
import com.study.tinkoff.di.AppComponent
import com.study.tinkoff.di.DaggerAppComponent
import com.study.users.di.UsersDepStore
import timber.log.Timber
import timber.log.Timber.Forest.plant
import com.study.ui.R as CoreR

class ChatApp : Application(), ImageLoaderFactory {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) plant(Timber.DebugTree())
        ProfileDepStore.dep = appComponent
        ChannelsDepStore.dep = appComponent
        UsersDepStore.dep = appComponent
        ChatDepStore.dep = appComponent
    }

    override fun newImageLoader(): ImageLoader =
        appComponent.imageLoader
            .newBuilder()
            .error(CoreR.color.darkest_nero)
            .placeholder(CoreR.color.darkest_nero)
            .logger(DebugLogger())
            .build()
}
