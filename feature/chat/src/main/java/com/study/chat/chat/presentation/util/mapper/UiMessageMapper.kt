package com.study.chat.chat.presentation.util.mapper

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.study.chat.chat.presentation.model.ChatListItem
import com.study.chat.chat.presentation.model.DateSeparator
import com.study.chat.chat.presentation.model.TopicSeparator
import com.study.chat.chat.presentation.model.UiMessage
import com.study.chat.common.domain.model.IncomeMessage
import com.study.common.ext.isSameDay

internal fun IncomeMessage.toChatMessage(currUserId: Int): UiMessage.ChatMessage {
    val imageUrlResult = content.findImageUrl()
    return UiMessage.ChatMessage(
        id = id,
        content = imageUrlResult.contentWithoutImageUrl,
        calendar = calendar,
        reactions = mapUiReactions(currUserId),
        senderName = requireNotNull(senderName),
        senderAvatarUrl = senderAvatarUrl.orEmpty(),
        topic = topic,
        imageUrl = imageUrlResult.imageUrl
    )
}

internal fun IncomeMessage.toMeMessage(currUserId: Int): UiMessage.MeMessage {
    val imageUrlResult = content.findImageUrl()
    return UiMessage.MeMessage(
        id = id,
        content = imageUrlResult.contentWithoutImageUrl,
        calendar = calendar,
        reactions = mapUiReactions(currUserId),
        topic = topic,
        imageUrl = imageUrlResult.imageUrl
    )
}

internal fun IncomeMessage.toUiMessage(currUserId: Int): UiMessage =
    if (currUserId == senderId) toMeMessage(currUserId) else toChatMessage(currUserId)

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

private val imageUrlRegex = """\[(.*?)]\((/user_uploads.*?\.(jpg|jpeg|gif|png))\)""".toRegex()
private fun String.findImageUrl(): ImageUrlResult {
    val matches = imageUrlRegex.find(this)
    return if (matches != null) {
        ImageUrlResult(matches.groupValues[2], replace(matches.groupValues[0], ""))
    } else ImageUrlResult(null, this)
}

private class ImageUrlResult(val imageUrl: String?, val contentWithoutImageUrl: String)
