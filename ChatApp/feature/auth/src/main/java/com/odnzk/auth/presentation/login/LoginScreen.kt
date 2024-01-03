package com.odnzk.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.odnzk.auth.R
import com.odnzk.auth.presentation.composables.PasswordTextField
import com.odnzk.auth.presentation.login.elm.LoginEffect
import com.odnzk.auth.presentation.login.elm.LoginEvent
import com.odnzk.auth.presentation.login.elm.LoginState
import com.odnzk.ui_compose.ChatAppColors
import com.odnzk.ui_compose.ChatAppTypography
import com.odnzk.ui_compose.LocalDim
import com.odnzk.ui_compose.PrimaryButton
import com.odnzk.ui_compose.PrimaryTextInputLayout
import kotlinx.coroutines.launch
import org.mockito.Mockito
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.effects
import vivid.money.elmslie.coroutines.states


internal typealias LoginStore = Store<LoginEvent, LoginEffect, LoginState>

@Composable
internal fun LoginScreen(
    store: LoginStore,
    modifier: Modifier = Modifier,
    onNavigateToMainGraph: () -> Unit
) {
    val state = store.states.collectAsState(initial = LoginState())
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            LoginContent(store = store, modifier)
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier)
            }
        }
    }

    LaunchedEffect(Unit) {
        store.effects.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToSignup -> {
                    // TODO("implement compose navigation")
                }

                is LoginEffect.ShowSnackbar -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = effect.message)
                    }
                }

                LoginEffect.NavigateToMainGraph -> {
                    onNavigateToMainGraph()
                }
            }
        }
    }
}

@Composable
private fun LoginContent(store: LoginStore, modifier: Modifier) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val error = stringResource(id = R.string.common_error_empty_fields)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDim.current.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.screen_login_title),
            style = ChatAppTypography.h1,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(LocalDim.current.medium))

        PrimaryTextInputLayout(
            label = stringResource(id = R.string.common_field_email),
            hint = stringResource(id = R.string.common_field_email_hint),
            value = username,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        ) {
            username = it
        }
        Spacer(modifier = Modifier.height(LocalDim.current.small))

        PasswordTextField(
            label = stringResource(id = R.string.common_field_password),
            hint = stringResource(id = R.string.common_field_password_hint),
            password = password
        ) { newValue ->
            password = newValue
        }
        Spacer(modifier = Modifier.height(LocalDim.current.small))

        PrimaryButton(text = stringResource(id = R.string.screen_login_button_login)) {
            isError = username.isBlank() || password.isBlank()
            if (!isError) {
                store.accept(LoginEvent.Ui.Login(username = username, password = password))
            }
        }

        if (isError) {
            Text(
                text = error,
                modifier = Modifier.padding(bottom = LocalDim.current.medium),
                color = ChatAppColors.error
            )
        }

        TextButton(onClick = { store.accept(LoginEvent.Ui.UserDoesNotHaveAnAccount) }) {
            Text(
                text = stringResource(id = R.string.screen_login_user_does_not_have_an_account),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    LoginScreen(Mockito.mock()) {
    }
}