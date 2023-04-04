package com.study.tinkoff

import android.app.Application
import timber.log.Timber
import timber.log.Timber.Forest.plant

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) plant(Timber.DebugTree())
    }
}
