package com.odnzk.auth.di

import com.odnzk.auth.presentation.elm.AuthActor
import com.odnzk.auth.presentation.elm.AuthEffect
import com.odnzk.auth.presentation.elm.AuthEvent
import com.odnzk.auth.presentation.elm.AuthReducer
import com.odnzk.auth.presentation.elm.AuthState
import com.odnzk.auth.presentation.login.elm.LoginActor
import com.odnzk.auth.presentation.login.elm.LoginEffect
import com.odnzk.auth.presentation.login.elm.LoginEvent
import com.odnzk.auth.presentation.login.elm.LoginReducer
import com.odnzk.auth.presentation.login.elm.LoginState
import com.odnzk.auth.presentation.signup.elm.SignupActor
import com.odnzk.auth.presentation.signup.elm.SignupEffect
import com.odnzk.auth.presentation.signup.elm.SignupEvent
import com.odnzk.auth.presentation.signup.elm.SignupReducer
import com.odnzk.auth.presentation.signup.elm.SignupState
import com.study.common.di.FeatureScope
import com.study.components.di.ManualStoreHolder
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class AuthStoreModule {

    @Provides
    @FeatureScope
    fun providesLoginStore(reducer: LoginReducer, actor: LoginActor)
            : StoreHolder<LoginEvent, LoginEffect, LoginState> =
        ManualStoreHolder { ElmStoreCompat(LoginState(), reducer, actor) }

    @Provides
    @FeatureScope
    fun providesSignupStore(reducer: SignupReducer, actor: SignupActor)
            : StoreHolder<SignupEvent, SignupEffect, SignupState> =
        ManualStoreHolder { ElmStoreCompat(SignupState(), reducer, actor) }

    @Provides
    @FeatureScope
    fun providesAuthStore(
        reducer: AuthReducer,
        actor: AuthActor
    ): StoreHolder<AuthEvent, AuthEffect, AuthState> =
        ManualStoreHolder { ElmStoreCompat(AuthState(), reducer, actor) }

}