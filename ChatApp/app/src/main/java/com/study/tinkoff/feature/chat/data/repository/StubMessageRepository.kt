package com.study.tinkoff.feature.chat.data.repository

import com.study.tinkoff.core.domain.model.Reaction
import com.study.tinkoff.core.domain.model.message.IncomeMessage
import com.study.tinkoff.core.domain.model.message.OutcomeMessage
import com.study.tinkoff.feature.chat.domain.repository.MessageRepository
import com.study.tinkoff.feature.chat.domain.util.RandomReceivedMessageGenerator
import com.study.tinkoff.feature.select_emoji.data.StubEmojiRepository
import com.study.tinkoff.feature.select_emoji.domain.EmojiRepository

object StubMessageRepository : MessageRepository {
    private var messages: MutableList<IncomeMessage> = mutableListOf()
    private val generator: RandomReceivedMessageGenerator = RandomReceivedMessageGenerator()
    private val emojiRepository: EmojiRepository = StubEmojiRepository

    override fun getMessages(): List<IncomeMessage> = messages.toList()

    init {
        for (i in 1..3) {
            messages.add(generator.generateRandomMessage())
        }
    }

    override suspend fun sendMessage(message: OutcomeMessage) {
        messages.add(generator.generateRandomMessage(message.content))
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) {
        val emoji = emojiRepository.getEmojiByEmojiName(emojiName)
        messages.first { it.messageId == messageId }.reactions.add(Reaction(messageId, 0, emoji))
    }
}
