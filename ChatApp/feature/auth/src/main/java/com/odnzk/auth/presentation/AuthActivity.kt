package com.odnzk.auth.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.odnzk.auth.di.FeatureAuthComponentViewModel
import com.odnzk.auth.presentation.login.LoginScreen
import com.odnzk.auth.presentation.login.elm.LoginActor
import com.odnzk.auth.presentation.login.elm.LoginEffect
import com.odnzk.auth.presentation.login.elm.LoginEvent
import com.odnzk.auth.presentation.login.elm.LoginReducer
import com.odnzk.auth.presentation.login.elm.LoginState
import com.odnzk.ui_compose.ChatAppTheme
import com.study.common.ext.fastLazy
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

internal class AuthActivity : ElmActivity<LoginEvent, LoginEffect, LoginState>() {

    @Inject
    lateinit var reducer: LoginReducer

    @Inject
    lateinit var actor: LoginActor

    override val initEvent: LoginEvent get() = LoginEvent.Ui.Init

    override val storeHolder: StoreHolder<LoginEvent, LoginEffect, LoginState> by fastLazy {
        LifecycleAwareStoreHolder(lifecycle) {
            ElmStoreCompat(
                LoginState(),
                reducer,
                actor
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewModelProvider(this).get<FeatureAuthComponentViewModel>().component.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            ChatAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(store)
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
