package com.study.tinkoff.feature.chat.presentation.util

import com.study.common.extensions.toCalendar
import com.study.domain.model.IncomeMessage
import com.study.tinkoff.feature.chat.presentation.model.UiMessage

fun IncomeMessage.toChatMessage(): UiMessage.ChatMessage = UiMessage.ChatMessage(
    id = messageId,
    content = content,
    calendar = lastEditedTimestamp.toCalendar(),
    reactions = reactions.toList(),
    senderName = client.name,
    senderAvatarUrl = client.avatarUrl
)

fun IncomeMessage.toMeMessage(): UiMessage.MeMessage = UiMessage.MeMessage(
    id = messageId,
    content = content,
    calendar = lastEditedTimestamp.toCalendar(),
    reactions = reactions.toList()
)


fun List<IncomeMessage>.toUiMessages(): List<UiMessage> {
    return map { message -> if (message.isMeMessage) message.toMeMessage() else message.toChatMessage() }
}
