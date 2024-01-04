package com.study.users.domain.usecase

import com.study.users.domain.model.UserPresence
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetUsersPresenceCase @Inject constructor(
    private val repository: UsersRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<UserPresence> = withContext(dispatcher) {
        repository.getUsersPresence()
    }
}

