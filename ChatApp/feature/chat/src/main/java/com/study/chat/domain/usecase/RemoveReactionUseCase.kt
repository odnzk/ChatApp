package com.study.chat.domain.usecase

import com.study.chat.domain.model.Reaction
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RemoveReactionUseCase @Inject constructor(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(reaction: Reaction) = withContext(dispatcher) {
        repository.removeReaction(reaction)
    }
}
