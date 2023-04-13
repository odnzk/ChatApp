package com.study.chat.presentation.chat.util.mapper

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.common.extensions.isSameDay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


private val mapperDefaultDispatcher = Dispatchers.Default

internal suspend fun IncomeMessage.toChatMessage(
    currUserId: Int,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiMessage.ChatMessage =
    withContext(dispatcher) {
        UiMessage.ChatMessage(
            id = id,
            content = content,
            calendar = calendar,
            reactions = mapUiReactions(currUserId),
            senderName = senderName,
            senderAvatarUrl = senderAvatarUrl.orEmpty()
        )
    }

internal suspend fun IncomeMessage.toMeMessage(
    currUserId: Int,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiMessage.MeMessage =
    withContext(dispatcher) {
        UiMessage.MeMessage(
            id = id,
            content = content,
            calendar = calendar,
            reactions = mapUiReactions(currUserId)
        )
    }


internal suspend fun IncomeMessage.toUiMessage(
    currUserId: Int,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): UiMessage =
    withContext(dispatcher) {
        if (currUserId == senderId) toMeMessage(currUserId) else toChatMessage(currUserId)
    }

internal suspend fun PagingData<IncomeMessage>.toUiMessageWithDateGrouping(
    currUserId: Int,
    dispatcher: CoroutineDispatcher = mapperDefaultDispatcher
): PagingData<Any> = withContext(dispatcher) {
    map { it.toUiMessage(currUserId) }.insertSeparators { before, after ->
        if (before == null || after == null) return@insertSeparators null
        val afterDate = after.calendar
        val beforeDate = before.calendar
        if (!beforeDate.isSameDay(afterDate)) afterDate else null
    }
}
