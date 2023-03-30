package com.study.chat.data

import com.study.chat.domain.model.IncomeMessage
import com.study.chat.domain.model.MessageContentType
import com.study.chat.domain.model.Reaction
import com.study.chat.domain.repository.EmojiRepository
import java.util.Calendar
import kotlin.random.Random
import kotlin.random.nextInt

internal class RandomIncomeMessageGenerator {
    private val calendars = generateRandomCalendars()
    private val emojiRepository: EmojiRepository = StubEmojiRepository
    suspend fun generateRandomMessages(count: Int): List<IncomeMessage> {
        val resList = mutableListOf<IncomeMessage>()
        for (i in 1..count) {
            resList.add(generateRandomMessage())
        }
        return resList
    }

    suspend fun generateRandomMessage(
        contentList: String = Companion.contentList.random()
    ): IncomeMessage {
        val randomNum = Random.nextInt(senderName.indices)
        val randomMessageId = Random.nextInt()
        return IncomeMessage(
            messageId = randomMessageId,
            senderName = senderName[randomNum],
            content = contentList,
            lastEditedTimestamp = calendars.random().timeInMillis.toInt(),
            reactions = generateRandomReactions(randomMessageId).toMutableList(),
            senderAvatarUrl = null,
            contentType = MessageContentType.TEXT,
            displayRecipient = Any(),
            isMeMessage = Random.nextBoolean()
        )
    }

    private fun generateRandomCalendars(): List<Calendar> {
        val resList: MutableList<Calendar> = mutableListOf()
        repeat(generateCalendarCount) {
            val new = Calendar.getInstance().apply {
                add(Calendar.HOUR, calendarHoursGapRange.random())
            }
            resList.add(new)
        }
        return resList
    }

    private suspend fun generateRandomReactions(messageId: Int): List<Reaction> {
        val resList: MutableList<Reaction> = mutableListOf()
        val emoji = emojiRepository.getEmoji()
        repeat(generateEmojiCount) {
            val randomEmoji = emoji.random()
            resList.add(
                Reaction(
                    messageId = messageId,
                    userId = Random.nextInt(),
                    randomEmoji
                )
            )
        }
        return resList
    }

    companion object {
        private const val generateEmojiCount = 4
        private const val generateCalendarCount = 20
        private val calendarHoursGapRange = (1..500)
        private val senderName = arrayOf("Darrell", "Masha", "Mike", "John", "Dasha")
        private val contentList = arrayOf(
            "Hello", "How are you?".repeat(2), "Bye", "Fine, thanks, and you?".repeat(5)
        )
    }
}
