package com.study.chat.chat.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.common.data.mapper.toIncomeMessage
import com.study.chat.common.data.mapper.toMessageEntity
import com.study.chat.common.data.mapper.toReactionEntity
import com.study.chat.common.data.mapper.toTextMessage
import com.study.chat.common.data.source.local.LocalMessageDataSource
import com.study.chat.common.data.source.remote.RemoteMessageDataSource
import com.study.chat.common.domain.model.IncomeMessage
import com.study.chat.common.domain.model.NOT_YET_SYNCHRONIZED_ID
import com.study.chat.common.domain.model.OutcomeMessage
import com.study.chat.common.domain.model.Reaction
import com.study.common.ext.onFailureRestorePrevStateAndThrowError
import com.study.common.ext.runCatchingNonCancellation
import com.study.database.model.tuple.MessageWithReactionsTuple
import com.study.network.model.request.message.FileMessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// todo fix loading messages
internal class ChatRepositoryImpl @Inject constructor(
    private val remoteDS: RemoteMessageDataSource,
    private val localDS: LocalMessageDataSource,
    private val applicationContext: Context,
    private val messagePagingMediatorFactory: MessagesPagingMediator.Factory
) : ChatRepository {

    override fun getMessages(
        channelId: Int, topicName: String?, searchQuery: String
    ): Flow<PagingData<IncomeMessage>> = createMessagesPager(
        channelId,
        topicName,
        searchQuery
    ).flow.map { pagingData -> pagingData.map { it.toIncomeMessage() } }


    override suspend fun sendMessage(message: OutcomeMessage) = when (message) {
        is OutcomeMessage.File -> {
            val receivedMessage = uploadFile(message)
            sendTextMessage(receivedMessage)
        }

        is OutcomeMessage.Text -> sendTextMessage(message)
    }


    override suspend fun deleteIrrelevantMessages(channelId: Int, topicTitle: String) {
        localDS.removeIrrelevantMessages(channelId, topicTitle)
    }

    override suspend fun addReaction(reaction: Reaction) {
        val entity = reaction.toReactionEntity()
        localDS.addReaction(entity)
        onFailureRestorePrevStateAndThrowError(action = {
            remoteDS.addReaction(
                reaction.messageId,
                reaction.emoji.name
            )
        }) {
            localDS.removeReaction(entity)
        }
    }

    override suspend fun removeReaction(reaction: Reaction) {
        val entity = reaction.toReactionEntity()
        localDS.removeReaction(entity)
        onFailureRestorePrevStateAndThrowError(action = {
            remoteDS.removeReaction(
                reaction.messageId,
                reaction.emoji.name
            )
        }) {
            localDS.addReaction(entity)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createMessagesPager(
        channelId: Int, topicName: String?, searchQuery: String
    ): Pager<Int, MessageWithReactionsTuple> = Pager(
        config = PagingConfig(
            PAGE_SIZE,
            enablePlaceholders = false,
            maxSize = MAX_PAGER_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            initialLoadSize = PAGE_SIZE * 3
        ),
        remoteMediator = messagePagingMediatorFactory.create(channelId, topicName, searchQuery)
    )
    {
        localDS.getMessages(channelId, topicName, searchQuery)
    }

    private suspend fun uploadFile(message: OutcomeMessage.File): OutcomeMessage.Text {
        val file = readFile(Uri.parse(message.uri))
        return remoteDS.upload(file).uri?.takeIf { it.isNotEmpty() }?.let { serverUri ->
            message.toTextMessage(file.name, serverUri)
        } ?: error("Invalid uri from server")
    }

    private fun readFile(uri: Uri): FileMessageRequest {
        val fileName: String = applicationContext.contentResolver.query(uri, null, null, null, null)
            ?.use { cursor ->
                if (cursor.count == 0) error("File not found")
                cursor.moveToFirst()
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).takeIf { it >= 0 }
                    ?.let { nameIndex ->
                        return@use cursor.getString(nameIndex)
                    }
            } ?: error("Cannot read file")
        val bytes = applicationContext.contentResolver.openInputStream(uri)!!.use { inputStream ->
            inputStream.readBytes()
        }
        val type: String =
            applicationContext.contentResolver.getType(uri) ?: error("Invalid file type")
        return FileMessageRequest(name = fileName, bytes = bytes, type = type)
    }

    private suspend fun sendTextMessage(message: OutcomeMessage.Text) {
        val entity = message.toMessageEntity()
        localDS.addMessage(entity)
        runCatchingNonCancellation {
            remoteDS.sendMessage(
                message.type,
                message.channelId,
                message.content,
                message.topicTitle
            )
        }.onSuccess { response ->
            localDS.updateMessageId(NOT_YET_SYNCHRONIZED_ID, requireNotNull(response.id))
        }.onFailure {
            localDS.deleteMessage(NOT_YET_SYNCHRONIZED_ID)
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val MAX_PAGER_SIZE = 50
        private const val PREFETCH_DISTANCE = 5
    }

}
