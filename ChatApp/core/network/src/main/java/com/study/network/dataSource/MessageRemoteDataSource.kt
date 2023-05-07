package com.study.network.dataSource

import com.study.network.ZulipApi
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import javax.inject.Inject

class MessageRemoteDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {

    suspend fun sendMessage(
        type: MessageType,
        to: String,
        content: String,
        topic: String?
    ): SentMessageResponse = safeRequest { api.sendMessage(type, to, content, topic) }

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchor: MessagesAnchor,
        narrow: MessageNarrowList,
        applyMarkdown: Boolean = false
    ): AllMessagesResponse =
        safeRequest { api.getMessages(numBefore, numAfter, anchor, narrow, applyMarkdown) }

    suspend fun getMessages(
        numBefore: Int,
        numAfter: Int,
        anchorMessageId: Int,
        narrow: MessageNarrowList,
        applyMarkdown: Boolean = false
    ): AllMessagesResponse =
        safeRequest { api.getMessages(numBefore, numAfter, anchorMessageId, narrow, applyMarkdown) }

    suspend fun addReaction(mesId: Int, emojiName: String) =
        safeRequest { api.addReactionToMessage(mesId, emojiName) }

    suspend fun removeReaction(mesId: Int, emojiName: String) =
        safeRequest { api.removeReactionFromMessage(mesId, emojiName) }

}
