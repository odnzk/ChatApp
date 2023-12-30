package com.study.users.domain.usecase

import com.study.common.search.NothingFoundForThisQueryException
import com.study.users.domain.model.User
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchUsersUseCase @Inject constructor(
    private val repository: UsersRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String): List<User> =
        withContext(dispatcher) {
            val users = repository.getUsers()
            if (query.isEmpty()) {
                users
            } else {
                users
                    .filter { user -> user.name.contains(query, ignoreCase = true) }
                    .takeIf { it.isNotEmpty() }
                    ?: throw NothingFoundForThisQueryException()
            }
        }
}
