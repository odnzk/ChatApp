package com.study.profile.domain.usecase

import com.study.profile.domain.model.UserDetailed
import com.study.profile.domain.repository.UserRepository
import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
internal class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): UserDetailed = withContext(dispatcher) {
        repository.getCurrentUser()
    }
}

