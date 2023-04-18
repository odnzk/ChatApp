package com.study.network.repository.impl

import com.study.network.ZulipApi
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.message.SingleMessageResponse
import com.study.network.repository.MessageDataSource
import javax.inject.Inject

internal class ZulipMessageDataSource @Inject constructor(private val api: ZulipApi) :
    MessageDataSource {

    override suspend fun sendMessage(
        type: MessageType,
        to: String,
        content: String,
        topic: String?
    ): SentMessageResponse = api.sendMessage(type, to, content, topic)

    override suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: MessagesAnchor,
        narrow: MessageNarrowList,
        applyMarkdown: Boolean
    ): AllMessagesResponse = api.getMessages(numBefore, numAfter, anchor, narrow, applyMarkdown)

    override suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchorMessageId: Int,
        narrow: MessageNarrowList,
        applyMarkdown: Boolean
    ): AllMessagesResponse =
        api.getMessages(numBefore, numAfter, anchorMessageId, narrow, applyMarkdown)

    override suspend fun addReactionToMessage(mesId: Int, emojiName: String) =
        api.addReactionToMessage(mesId, emojiName)

    override suspend fun removeReactionFromMessage(mesId: Int, emojiName: String) =
        api.removeReactionFromMessage(mesId, emojiName)

    override suspend fun fetchSingleMessage(
        messageId: Int, applyMarkdown: Boolean
    ): SingleMessageResponse = api.fetchSingleMessage(messageId, applyMarkdown)

}
