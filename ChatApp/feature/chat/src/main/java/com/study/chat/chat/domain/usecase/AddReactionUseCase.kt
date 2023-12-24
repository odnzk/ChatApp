package com.study.chat.chat.domain.usecase

import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.common.domain.model.Reaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddReactionUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(reaction: Reaction) = withContext(dispatcher) {
        repository.addReaction(reaction)
    }
}
