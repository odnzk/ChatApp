package com.study.users.domain.usecase

import com.study.users.domain.model.User
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetUsersUseCase @Inject constructor(
    private val repository: UsersRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<User> = withContext(dispatcher) {
        repository.getUsers()
    }
}

