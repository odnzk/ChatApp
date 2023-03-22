package com.study.tinkoff.feature.chat.data.repository

import com.study.domain.model.IncomeMessage
import com.study.domain.model.MessageContentType
import com.study.tinkoff.feature.emoji.data.StubEmojiRepository
import java.util.Calendar
import kotlin.random.Random
import kotlin.random.nextInt

class RandomIncomeMessageGenerator {
    private val calendars = generateRandomCalendars()

    fun generateRandomMessage(
        contentList: String = Companion.contentList.random()
    ): IncomeMessage {
        val randomNum = Random.nextInt(senderName.indices)
        val randomMessageId = Random.nextInt()
        val user =
            com.study.domain.model.User(id = randomNum, name = senderName[randomNum], "", "", true)
        return IncomeMessage(
            messageId = randomMessageId,
            client = user,
            content = contentList,
            lastEditedTimestamp = calendars.random().timeInMillis.toInt(),
            reactions = generateRandomReactions(randomMessageId).toMutableList(),
            avatarUrl = null,
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

    private fun generateRandomReactions(messageId: Int): List<com.study.domain.model.Reaction> {
        val resList: MutableList<com.study.domain.model.Reaction> = mutableListOf()
        val emoji = StubEmojiRepository.getEmoji()
        repeat(generateEmojiCount) {
            val randomEmoji = emoji.random()
            resList.add(
                com.study.domain.model.Reaction(
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
