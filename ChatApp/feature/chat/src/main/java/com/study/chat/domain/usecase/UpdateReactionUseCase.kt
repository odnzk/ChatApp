package com.study.chat.domain.usecase

import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateReactionUseCase @Inject constructor(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        wasReactionSelected: Boolean,
        messageId: Int,
        emojiName: String,
        prevSelectedEmoji: String? = null
    ) = withContext(dispatcher) {
        if (wasReactionSelected) {
            repository.removeReaction(messageId, emojiName)
        } else {
            prevSelectedEmoji?.let { repository.removeReaction(messageId, prevSelectedEmoji) }
            repository.addReaction(messageId, emojiName)
        }
    }
}
