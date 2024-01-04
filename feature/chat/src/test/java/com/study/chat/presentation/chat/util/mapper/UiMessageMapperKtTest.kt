package com.study.chat.presentation.chat.util.mapper

import com.study.chat.chat.presentation.util.mapper.toChatMessage
import com.study.chat.chat.presentation.util.mapper.toMeMessage
import com.study.chat.common.domain.model.Emoji
import com.study.chat.common.domain.model.IncomeMessage
import com.study.chat.common.domain.model.Reaction
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class UiMessageMapperKtTest {
    private fun createCalendar() = Calendar.getInstance().apply { timeInMillis = 1 }

    private fun createIncomeMessage(
        calendar: Calendar = createCalendar(),
        reaction: Reaction = createReaction()
    ) = IncomeMessage(
        id = 0,
        senderAvatarUrl = "url",
        senderName = "sender",
        senderId = USER_ID - 1,
        content = "content",
        calendar = calendar,
        reactions = listOf(reaction),
        channelId = 0,
        topic = "topic"
    )

    private fun createReaction() = Reaction(messageId = 0, userId = 0, emoji = Emoji("", ""))

    @Test
    fun `mapping IncomeMessage to ChatMessage`() {
        val income = createIncomeMessage()
        val chatMes = income.toChatMessage(USER_ID)

        assertEquals(0, chatMes.id)
        assertEquals("url", chatMes.senderAvatarUrl)
        assertEquals("sender", chatMes.senderName)
        assertEquals("content", chatMes.content)
        assertEquals("topic", chatMes.topic)
        assertEquals(income.calendar, chatMes.calendar)
    }

    @Test
    fun `mapping IncomeMessage to MeMessage`() {
        val income = createIncomeMessage()
        val meMes = income.toMeMessage(USER_ID)

        assertEquals(USER_ID, meMes.id)
        assertEquals("content", meMes.content)
        assertEquals(income.calendar, meMes.calendar)
    }

    companion object {
        private const val USER_ID = 0
    }

}
