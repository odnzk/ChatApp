package com.study.chat.presentation.chat.util.mapper

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.study.chat.domain.model.IncomeMessage
import com.study.chat.presentation.chat.util.model.ChatListItem
import com.study.chat.presentation.chat.util.model.DateSeparator
import com.study.chat.presentation.chat.util.model.TopicSeparator
import com.study.chat.presentation.chat.util.model.UiMessage
import com.study.common.extension.isSameDay

internal fun IncomeMessage.toChatMessage(currUserId: Int): UiMessage.ChatMessage =
    UiMessage.ChatMessage(
        id = id,
        content = content,
        calendar = calendar,
        reactions = mapUiReactions(currUserId),
        senderName = requireNotNull(senderName),
        senderAvatarUrl = senderAvatarUrl.orEmpty(),
        topic = topic
    )


internal fun IncomeMessage.toMeMessage(currUserId: Int): UiMessage.MeMessage =
    UiMessage.MeMessage(
        id = id,
        content = content,
        calendar = calendar,
        reactions = mapUiReactions(currUserId),
        topic = topic
    )


internal fun IncomeMessage.toUiMessage(currUserId: Int): UiMessage =
    if (currUserId == senderId) toMeMessage(currUserId) else toChatMessage(currUserId)

//internal fun PagingData<IncomeMessage>.toUiMessages(currUserId: Int): PagingData<UiMessage> =
//    map { it.toUiMessage(currUserId) }

internal fun PagingData<IncomeMessage>.toUiMessagesWithSeparators(
    currUserId: Int,
    isChannelChat: Boolean
): PagingData<ChatListItem> =
    map { it.toUiMessage(currUserId) }.insertChatListSeparators(isChannelChat)

internal fun PagingData<UiMessage>.insertDateSeparators(): PagingData<ChatListItem> =
    insertSeparators { before, after ->
        if (before == null || after == null) return@insertSeparators null
        val beforeDate = before.calendar
        if (!after.calendar.isSameDay(beforeDate)) DateSeparator(beforeDate) else null
    }

internal fun PagingData<UiMessage>.insertChatListSeparators(isChannelChat: Boolean): PagingData<ChatListItem> =
    if (isChannelChat) insertTopicSeparators() else insertDateSeparators()

private fun PagingData<UiMessage>.insertTopicSeparators(): PagingData<ChatListItem> =
    insertSeparators { before, after ->
        if (before == null || after == null) return@insertSeparators null
        val beforeTopic = before.topic
        if (beforeTopic != after.topic) TopicSeparator(beforeTopic, after.calendar) else null
    }
