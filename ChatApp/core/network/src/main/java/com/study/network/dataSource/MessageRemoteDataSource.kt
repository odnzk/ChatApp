package com.study.network.dataSource

import com.study.network.ZulipApi
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.message.SingleMessageResponse
import javax.inject.Inject

class MessageRemoteDataSource @Inject constructor(private val api: ZulipApi) {

    suspend fun sendMessage(
        type: MessageType,
        to: String,
        content: String,
        topic: String?
    ): SentMessageResponse = api.sendMessage(type, to, content, topic)

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: MessagesAnchor,
        narrow: MessageNarrowList,
        applyMarkdown: Boolean = false
    ): AllMessagesResponse = api.getMessages(numBefore, numAfter, anchor, narrow, applyMarkdown)

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchorMessageId: Int,
        narrow: MessageNarrowList,
        applyMarkdown: Boolean = false
    ): AllMessagesResponse =
        api.getMessages(numBefore, numAfter, anchorMessageId, narrow, applyMarkdown)

    suspend fun addReactionToMessage(mesId: Int, emojiName: String) =
        api.addReactionToMessage(mesId, emojiName)

    suspend fun removeReactionFromMessage(mesId: Int, emojiName: String) =
        api.removeReactionFromMessage(mesId, emojiName)

    suspend fun fetchSingleMessage(
        messageId: Int, applyMarkdown: Boolean = false
    ): SingleMessageResponse = api.fetchSingleMessage(messageId, applyMarkdown)

}
