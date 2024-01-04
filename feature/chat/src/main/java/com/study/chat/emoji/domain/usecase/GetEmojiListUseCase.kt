package com.study.chat.emoji.domain.usecase

import com.study.chat.emoji.domain.repository.EmojiRepository
import com.study.chat.common.domain.model.Emoji
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetEmojiListUseCase @Inject constructor(
    private val repository: EmojiRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): List<Emoji> = withContext(dispatcher) { repository.getEmoji() }
}
