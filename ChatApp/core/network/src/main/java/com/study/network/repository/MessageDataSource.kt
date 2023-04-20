package com.study.network.repository

import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.message.SingleMessageResponse

interface MessageDataSource {
    suspend fun sendMessage(
        type: MessageType,
        to: String,
        content: String,
        topic: String? = null
    ): SentMessageResponse

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: MessagesAnchor,
        narrow: MessageNarrowList = MessageNarrowList(),
        applyMarkdown: Boolean = false
    ): AllMessagesResponse

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchorMessageId: Int,
        narrow: MessageNarrowList = MessageNarrowList(),
        applyMarkdown: Boolean = false
    ): AllMessagesResponse


    suspend fun addReactionToMessage(
        mesId: Int,
        emojiName: String
    )

    suspend fun removeReactionFromMessage(
        mesId: Int,
        emojiName: String
    )

    suspend fun fetchSingleMessage(
        messageId: Int,
        applyMarkdown: Boolean = false
    ): SingleMessageResponse

}
