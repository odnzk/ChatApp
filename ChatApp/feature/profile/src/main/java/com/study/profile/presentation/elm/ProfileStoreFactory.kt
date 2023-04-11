package com.study.profile.presentation.elm

import com.study.profile.domain.usecase.GetUserPresenceUseCase
import com.study.profile.domain.usecase.GetUserUseCase
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal object ProfileStoreFactory {
    fun create(getUserUseCase: GetUserUseCase, getUserPresenceUseCase: GetUserPresenceUseCase) =
        ElmStoreCompat(
            ProfileState(),
            ProfileReducer(),
            ProfileActor(getUserUseCase, getUserPresenceUseCase)
        )
}
