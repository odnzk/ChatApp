package com.study.chat.data

import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.OutcomeMessage
import com.study.chat.domain.model.Reaction
import com.study.chat.domain.repository.EmojiRepository
import com.study.chat.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

internal class StubMessageRepository : MessageRepository {
    private val generator: RandomIncomeMessageGenerator = RandomIncomeMessageGenerator()
    private val emojiRepository: EmojiRepository = StubEmojiRepository
    private val messages: MutableStateFlow<List<IncomeMessage>> = MutableStateFlow(emptyList())

    override suspend fun getMessages(): Flow<List<IncomeMessage>> {
        return if (Random.nextBoolean()) {
            flow { throw RuntimeException() }
        } else {
            messages.value = generator.generateRandomMessages(5)
            messages
        }
    }

    override suspend fun sendMessage(message: OutcomeMessage) {
        if (Random.nextBoolean()) {
            throw RuntimeException()
        } else {
            messages.update {
                it.toMutableList().apply {
                    add(generator.generateRandomMessage(message.content))
                }
            }
        }
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) {
        if (Random.nextBoolean()) {
            throw RuntimeException()
        } else {
            val emoji = emojiRepository.getEmojiByEmojiName(emojiName)
            messages.value = messages.value.apply {
                first { it.messageId == messageId }.reactions.add(
                    Reaction(
                        messageId, 0, emoji
                    )
                )
            }
        }
    }
}
