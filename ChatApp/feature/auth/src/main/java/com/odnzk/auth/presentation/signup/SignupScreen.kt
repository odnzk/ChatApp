package com.odnzk.auth.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.odnzk.auth.R
import com.odnzk.auth.presentation.composables.PasswordTextField
import com.odnzk.auth.presentation.signup.elm.SignupEffect
import com.odnzk.auth.presentation.signup.elm.SignupEvent
import com.odnzk.auth.presentation.signup.elm.SignupState
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

internal typealias SignupStore = Store<SignupEvent, SignupEffect, SignupState>

private const val EMPTY_ERROR = -1

@Composable
internal fun SignupScreen(store: SignupStore, modifier: Modifier = Modifier) {
    val state = store.states.collectAsState(initial = SignupState())
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
            Content(store = store, modifier)
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier)
            }
        }
    }

    LaunchedEffect(Unit) {
        store.effects.collect { effect ->
            when (effect) {
                SignupEffect.NavigateToLogin -> TODO("implement compose navigation")
                is SignupEffect.ShowSnackbar -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = effect.message)
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(store: SignupStore, modifier: Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var error by remember { mutableIntStateOf(EMPTY_ERROR) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalDim.current.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.screen_signup_title),
            style = ChatAppTypography.h1,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(LocalDim.current.medium))

        PrimaryTextInputLayout(
            label = stringResource(id = R.string.common_field_email),
            hint = stringResource(id = R.string.common_field_email_hint),
            value = email,
            singleLine = true
        ) {
            email = it
        }
        Spacer(modifier = Modifier.height(LocalDim.current.small))

        PrimaryTextInputLayout(
            label = stringResource(id = R.string.screen_signup_field_full_name),
            hint = stringResource(id = R.string.screen_signup_filed_full_name_hint),
            value = fullName,
            singleLine = true
        ) {
            fullName = it
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

        PasswordTextField(
            label = stringResource(id = R.string.screen_signup_field_confirm_password),
            hint = stringResource(id = R.string.screen_signup_field_confirm_password_hint),
            password = confirmPassword
        ) { newValue ->
            confirmPassword = newValue
        }
        Spacer(modifier = Modifier.height(LocalDim.current.small))

        PrimaryButton(text = stringResource(id = R.string.screen_login_button_login)) {
            error = when {
                email.isBlank() || password.isBlank() || fullName.isBlank() ->
                    R.string.common_error_empty_fields

                confirmPassword != password -> R.string.screen_signup_error_passwords_do_not_match
                else -> EMPTY_ERROR
            }
            if (error != EMPTY_ERROR) {
                store.accept(
                    SignupEvent.Ui.Signup(
                        email = email,
                        password = password,
                        fullName = fullName
                    )
                )
            }
        }

        if (error == EMPTY_ERROR) {
            Text(
                text = stringResource(id = error),
                modifier = Modifier.padding(bottom = LocalDim.current.medium),
                color = ChatAppColors.error
            )
        }

        TextButton(onClick = { store.accept(SignupEvent.Ui.UserAlreadyHasAccount) }) {
            Text(
                text = stringResource(id = R.string.screen_login_user_does_not_have_an_account),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
@Preview
private fun preview() {
    SignupScreen(store = Mockito.mock())
}