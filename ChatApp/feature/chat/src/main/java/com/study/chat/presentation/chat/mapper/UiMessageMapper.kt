package com.study.chat.presentation.chat.mapper

import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.chat.model.UiMessage
import com.study.common.extensions.toCalendar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val mapperDefaultDispatcher = Dispatchers.Default

internal suspend fun IncomeMessage.toChatMessage(
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiMessage.ChatMessage =
    withContext(dispatcher) {
        UiMessage.ChatMessage(
            id = messageId,
            content = content,
            calendar = lastEditedTimestamp.toCalendar(),
            reactions = reactions.toList(),
            senderName = senderName,
            senderAvatarUrl = senderAvatarUrl.orEmpty()
        )
    }

internal suspend fun IncomeMessage.toMeMessage(
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiMessage.MeMessage =
    withContext(dispatcher) {
        UiMessage.MeMessage(
            id = messageId,
            content = content,
            calendar = lastEditedTimestamp.toCalendar(),
            reactions = reactions.toList()
        )
    }


internal suspend fun List<IncomeMessage>.toUiMessages(
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): List<UiMessage> =
    withContext(dispatcher) {
        map { message -> if (message.isMeMessage) message.toMeMessage() else message.toChatMessage() }
    }
