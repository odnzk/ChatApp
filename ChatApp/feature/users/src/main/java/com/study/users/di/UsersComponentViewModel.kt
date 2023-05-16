package com.study.users.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface UsersDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: UsersDep

    companion object : UsersDepsProvider by UsersDepStore
}

object UsersDepStore : UsersDepsProvider {
    override var dep: UsersDep by notNull()
}

internal class UsersComponentViewModel : ViewModel() {
    val usersComponent = DaggerUsersComponent.factory().create(UsersDepsProvider.dep)

    override fun onCleared() {
        super.onCleared()
        usersComponent.userStoreHolder.store.stop()
    }
}
