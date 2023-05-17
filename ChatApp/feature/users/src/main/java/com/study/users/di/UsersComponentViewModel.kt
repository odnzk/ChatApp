package com.study.users.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface UsersDepProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dep: UsersDep

    companion object : UsersDepProvider by UsersDepStore
}

object UsersDepStore : UsersDepProvider {
    override var dep: UsersDep by notNull()
}

internal class UsersComponentViewModel : ViewModel() {
    val usersComponent = DaggerUsersComponent.factory().create(UsersDepProvider.dep)

    override fun onCleared() {
        super.onCleared()
        usersComponent.userStoreHolder.store.stop()
    }
}
