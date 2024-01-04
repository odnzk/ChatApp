package com.odnzk.auth.presentation

import android.content.Context

object AuthStarter {
    fun start(context: Context) {
        context.startActivity(AuthActivity.createIntent(context))
    }
}