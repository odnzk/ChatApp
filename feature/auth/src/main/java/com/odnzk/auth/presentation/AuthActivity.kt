package com.odnzk.auth.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.odnzk.auth.di.FeatureAuthComponentViewModel
import com.odnzk.auth.di.MainFeaturesStarter
import com.odnzk.auth.presentation.elm.AuthEffect
import com.odnzk.auth.presentation.elm.AuthEvent
import com.odnzk.auth.presentation.elm.AuthState
import com.odnzk.auth.presentation.login.LoginScreen
import com.odnzk.auth.presentation.login.elm.LoginEffect
import com.odnzk.auth.presentation.login.elm.LoginEvent
import com.odnzk.auth.presentation.login.elm.LoginState
import com.odnzk.ui_compose.ChatAppTheme
import kotlinx.coroutines.launch
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.effects
import vivid.money.elmslie.coroutines.states
import javax.inject.Inject

internal class AuthActivity : ElmActivity<AuthEvent, AuthEffect, AuthState>() {
    @Inject
    lateinit var mainFeaturesStarter: MainFeaturesStarter

    @Inject
    lateinit var authStoreHolder: StoreHolder<AuthEvent, AuthEffect, AuthState>

    @Inject
    lateinit var loginStoreHolder: StoreHolder<LoginEvent, LoginEffect, LoginState>

    override val initEvent get() = AuthEvent.Ui.Init

    override val storeHolder: StoreHolder<AuthEvent, AuthEffect, AuthState> get() = authStoreHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewModelProvider(this).get<FeatureAuthComponentViewModel>().component.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            ChatAppTheme {
                val state = store.states.collectAsState(initial = AuthState())
                val snackbarHostState = remember { SnackbarHostState() }
                val localCoroutineScope = rememberCoroutineScope()

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { scaffoldPadding ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(scaffoldPadding)
                            .fillMaxSize()
                    ) {
                        when {
                            state.value.isLoading -> {
                                CircularProgressIndicator()
                            }

                            state.value.isUserAuthorized == false -> {
                                LoginScreen(store = loginStoreHolder.store) {
                                    mainFeaturesStarter.start(this@AuthActivity)
                                }
                            }

                            else -> Unit
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    store.effects.collect { effect ->
                        when (effect) {
                            AuthEffect.NavigateToMainFeatures -> {
                                mainFeaturesStarter.start(this@AuthActivity)
                            }

                            is AuthEffect.ShowSnackbar -> {
                                localCoroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = effect.message)
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    override fun render(state: AuthState) = Unit

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, AuthActivity::class.java).apply {
                flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
            }
    }
}
