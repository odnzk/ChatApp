package com.odnzk.auth.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.odnzk.auth.presentation.composables.LoginScreen
import com.odnzk.auth.presentation.elm.LoginEffect
import com.odnzk.auth.presentation.elm.LoginEvent
import com.odnzk.auth.presentation.elm.LoginState
import com.odnzk.ui_compose.ChatAppTheme
import vivid.money.elmslie.android.base.ElmActivity

internal class AuthActivity : ElmActivity<LoginEvent, LoginEffect, LoginState>() {
    override val initEvent: LoginEvent get() = LoginEvent.Ui.Init

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    LoginScreen() { username, password ->
                        // TODO()
                    }
                }
            }
        }
    }

    override fun render(state: LoginState) = when {
        state.isLoading -> {

        }

        else -> {

        }
    }

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, AuthActivity::class.java).apply {
                flags = FLAG_ACTIVITY_CLEAR_TASK
            }
    }
}
