package com.study.tinkoff.util

import android.content.Context
import android.content.Intent
import com.odnzk.auth.di.MainFeaturesStarter
import com.study.tinkoff.presentation.MainActivity
import javax.inject.Inject

class MainFeaturesStarterImpl @Inject constructor() : MainFeaturesStarter {
    override fun start(context: Context) {
        context.startActivity(MainActivity.createIntent(context).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}
