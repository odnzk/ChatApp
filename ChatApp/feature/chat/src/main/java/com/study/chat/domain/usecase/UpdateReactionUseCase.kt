package com.study.chat.domain.usecase

import com.study.chat.domain.repository.MessageRepository
import com.study.chat.presentation.chat.model.UiMessage
import com.study.chat.presentation.chat.util.mapper.toUiMessage
import com.study.common.extensions.firstWithIndex
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateReactionUseCase(
    private val repository: MessageRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        list: List<Any>,
        selectedMessageId: Int,
        selectedEmojiName: String,
        currentUserId: Int
    ): List<Any> = withContext(dispatcher) {
        val messages = list.filterIsInstance<UiMessage>()
        val mesWithIndex = messages.firstWithIndex { elem -> elem.id == selectedMessageId }
        val reaction = mesWithIndex.first.reactions.find { it.emoji.name == selectedEmojiName }
        if (reaction?.isSelected == true) {
            repository.removeReaction(selectedMessageId, selectedEmojiName)
        } else {
            repository.addReaction(selectedMessageId, selectedEmojiName)
        }
        repository.fetchMessage(selectedMessageId).toUiMessage(currentUserId).let {
            list.toMutableList().apply { set(mesWithIndex.second, it) }
        }
    }
}
