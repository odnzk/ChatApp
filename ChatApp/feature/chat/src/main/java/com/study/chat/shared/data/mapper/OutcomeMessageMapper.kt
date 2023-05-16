package com.study.chat.shared.data.mapper

import com.study.chat.shared.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.shared.domain.model.OutcomeMessage
import com.study.database.model.MessageEntity

internal fun OutcomeMessage.Text.toMessageEntity() =
    MessageEntity(
        id = NOT_YET_SYNCHRONIZED_ID,
        channelId = channelId,
        topicTitle = topicTitle,
        senderId = senderId,
        senderAvatarUrl = null,
        senderName = null,
        content = content,
        calendar = calendar
    )

internal fun OutcomeMessage.File.toTextMessage(
    fileName: String,
    serverUri: String
): OutcomeMessage.Text =
    OutcomeMessage.Text(
        channelId = channelId,
        topicTitle = topicTitle,
        content = uriToTextContent(fileName = fileName, fileUri = serverUri),
        senderId = senderId
    )

private fun uriToTextContent(fileName: String, fileUri: String) = "[$fileName]($fileUri)"
