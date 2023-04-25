package com.study.users.domain.usecase

import com.study.components.search.searchFlow
import com.study.users.domain.model.User
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SearchUsersUseCase @Inject constructor(
    private val repository: UsersRepository, private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String): List<User> = withContext(dispatcher) {
        repository.getUsers()
            .searchFlow(query) { it.name.contains(query, ignoreCase = true) }
    }
}
