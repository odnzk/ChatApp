package com.study.chat.shared.data.source.remote

import com.study.network.ZulipApi
import com.study.network.model.request.message.FileMessageRequest
import com.study.network.model.request.message.MessageNarrowList
import com.study.network.model.request.message.MessageType
import com.study.network.model.request.message.MessagesAnchor
import com.study.network.model.response.message.AllMessagesResponse
import com.study.network.model.response.message.SentMessageResponse
import com.study.network.model.response.message.UploadedFileResponse
import com.study.network.util.BaseNetworkDataSource
import dagger.Reusable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@Reusable
internal class RemoteMessageDataSource @Inject constructor(private val api: ZulipApi) :
    BaseNetworkDataSource() {

    suspend fun sendMessage(
        type: MessageType, to: Int, content: String, topic: String?
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

    suspend fun addReaction(messageId: Int, emojiName: String) =
        safeRequest { api.addReactionToMessage(messageId, emojiName) }

    suspend fun removeReaction(messageId: Int, emojiName: String) =
        safeRequest { api.removeReactionFromMessage(messageId, emojiName) }

    suspend fun deleteMessage(messageId: Int) = safeRequest { api.deleteMessage(messageId) }

    suspend fun updateMessage(messageId: Int, content: String, topic: String) =
        safeRequest { api.updateMessage(messageId, topic, content) }

    suspend fun upload(file: FileMessageRequest): UploadedFileResponse = safeRequest {
        val filePart = MultipartBody.Part.createFormData(
            name = FILE_FORM_DATA,
            filename = file.name,
            file.bytes.toRequestBody(contentType = file.type.toMediaTypeOrNull())
        )
        api.upload(filePart)
    }

    companion object {
        private const val FILE_FORM_DATA = "file"
    }

}
