package com.study.chat.presentation.chat.util.mapper

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.common.extension.isSameDay

internal fun IncomeMessage.toChatMessage(currUserId: Int): UiMessage.ChatMessage =
    UiMessage.ChatMessage(
        id = id,
        content = content,
        calendar = calendar,
        reactions = mapUiReactions(currUserId),
        senderName = requireNotNull(senderName),
        senderAvatarUrl = senderAvatarUrl.orEmpty()
    )


internal fun IncomeMessage.toMeMessage(currUserId: Int): UiMessage.MeMessage =
    UiMessage.MeMessage(
        id = id,
        content = content,
        calendar = calendar,
        reactions = mapUiReactions(currUserId)
    )


internal fun IncomeMessage.toUiMessage(currUserId: Int): UiMessage =
    if (currUserId == senderId) toMeMessage(currUserId) else toChatMessage(currUserId)

internal fun PagingData<IncomeMessage>.toUiMessages(currUserId: Int): PagingData<UiMessage> =
    map { it.toUiMessage(currUserId) }

internal fun PagingData<UiMessage>.groupByDate(): PagingData<Any> =
    insertSeparators { before, after ->
        if (before == null || after == null) return@insertSeparators null
        val afterDate = after.calendar
        val beforeDate = before.calendar
        if (!beforeDate.isSameDay(afterDate)) afterDate else null
    }

