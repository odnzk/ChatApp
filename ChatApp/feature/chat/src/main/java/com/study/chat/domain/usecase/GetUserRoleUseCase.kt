package com.study.chat.domain.usecase

import com.study.auth.api.UserAuthRepository
import com.study.chat.domain.model.UserRole
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetUserRoleUseCase @Inject constructor(
    private val authRepository: UserAuthRepository,
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(messageId: Int): UserRole = withContext(dispatcher) {
        val senderId = repository.getMessageById(messageId).senderId
        when {
            senderId == authRepository.getUserId() -> UserRole.OWNER
            authRepository.isAdmin() -> UserRole.ADMIN
            else -> UserRole.USER
        }
    }
}
