package com.study.tinkoff.feature.chat.data.repository

import com.study.domain.model.IncomeMessage
import com.study.domain.model.OutcomeMessage
import com.study.tinkoff.feature.chat.domain.repository.MessageRepository
import com.study.tinkoff.feature.emoji.data.StubEmojiRepository
import com.study.tinkoff.feature.emoji.domain.EmojiRepository

class StubMessageRepository : MessageRepository {
    private var messages: MutableList<IncomeMessage> = mutableListOf()
    private val generator: RandomIncomeMessageGenerator = RandomIncomeMessageGenerator()
    private val emojiRepository: EmojiRepository = StubEmojiRepository
    override suspend fun getMessages(): List<IncomeMessage> = messages.toList()

    init {
        for (i in 1..20) {
            messages.add(generator.generateRandomMessage())
        }
    }

    override suspend fun sendMessage(message: OutcomeMessage) {
        messages.add(generator.generateRandomMessage(message.content))
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) {
        val emoji = emojiRepository.getEmojiByEmojiName(emojiName)
        messages.first { it.messageId == messageId }.reactions.add(
            com.study.domain.model.Reaction(
                messageId,
                0,
                emoji
            )
        )
    }
}
