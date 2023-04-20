package com.study.users.di

import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

internal interface UsersDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: UsersDep

    companion object : UsersDepsProvider by UsersDepsStore
}

object UsersDepsStore : UsersDepsProvider {
    override var deps: UsersDep by notNull()
}

internal class UsersComponentViewModel : ViewModel() {
    val usersComponent = DaggerUsersComponent.factory().create(UsersDepsProvider.deps)
}
