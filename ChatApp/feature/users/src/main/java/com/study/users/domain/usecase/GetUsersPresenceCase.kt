package com.study.users.domain.usecase

import com.study.users.domain.model.UserPresence
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class GetUsersPresenceCase(
    private val repository: UsersRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<UserPresence> = withContext(dispatcher) {
        repository.getUsersPresence()
    }
}

