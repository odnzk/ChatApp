package com.study.tinkoff.presentation

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.odnzk.auth.di.FeatureAuthDepStore
import com.study.channels.common.di.ChannelsDepStore
import com.study.chat.common.di.ChatDepStore
import com.study.profile.di.ProfileDepStore
import com.study.tinkoff.di.AppComponent
import com.study.tinkoff.di.DaggerAppComponent
import com.study.users.di.UsersDepStore
import com.study.ui.R as CoreR

class ChatApp : Application(), ImageLoaderFactory {

    val appComponent: AppComponent by lazy { DaggerAppComponent.factory().create(this) }

    override fun onCreate() {
        super.onCreate()
        ProfileDepStore.dep = appComponent
        ChannelsDepStore.dep = appComponent
        UsersDepStore.dep = appComponent
        ChatDepStore.dep = appComponent
        FeatureAuthDepStore.dep = appComponent
    }

    override fun newImageLoader(): ImageLoader =
        appComponent.imageLoader
            .newBuilder()
            .error(CoreR.color.darkest_nero)
            .placeholder(CoreR.color.darkest_nero)
            .build()
}
