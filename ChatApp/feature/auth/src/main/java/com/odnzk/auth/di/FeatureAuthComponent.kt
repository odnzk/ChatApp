package com.odnzk.auth.di

import com.odnzk.auth.presentation.AuthActivity
import com.odnzk.auth.presentation.elm.AuthEffect
import com.odnzk.auth.presentation.elm.AuthEvent
import com.odnzk.auth.presentation.elm.AuthState
import com.odnzk.auth.presentation.login.elm.LoginEffect
import com.odnzk.auth.presentation.login.elm.LoginEvent
import com.odnzk.auth.presentation.login.elm.LoginState
import com.odnzk.auth.presentation.signup.elm.SignupEffect
import com.odnzk.auth.presentation.signup.elm.SignupEvent
import com.odnzk.auth.presentation.signup.elm.SignupState
import com.study.common.di.FeatureScope
import dagger.Component
import vivid.money.elmslie.android.storeholder.StoreHolder

@[FeatureScope Component(
    dependencies = [FeatureAuthDep::class],
    modules = [BindsModule::class, AuthStoreModule::class]
)]
internal interface FeatureAuthComponent {
    fun inject(activity: AuthActivity)

    val authStoreHolder: StoreHolder<AuthEvent, AuthEffect, AuthState>
    val loginStoreHolder: StoreHolder<LoginEvent, LoginEffect, LoginState>
    val signupStoreHolder: StoreHolder<SignupEvent, SignupEffect, SignupState>

    @Component.Factory
    interface Factory {
        fun create(dependencies: FeatureAuthDep): FeatureAuthComponent
    }
}
