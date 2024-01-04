package com.study.chat.actions.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.study.chat.actions.data.mapper.toUser
import com.study.chat.actions.domain.model.User
import com.study.chat.actions.domain.repository.ActionsRepository
import com.study.chat.common.data.mapper.toFirstMessageSenderId
import com.study.chat.common.data.mapper.toIncomeMessage
import com.study.chat.common.data.source.local.message.LocalMessageDataSource
import com.study.chat.common.data.source.remote.RemoteMessageDataSource
import com.study.chat.common.domain.model.IncomeMessage
import com.study.chat.common.domain.model.MessageDoesNotExistException
import com.study.common.ext.onFailureRestorePrevStateAndThrowError
import com.study.network.model.request.message.MessageNarrowList
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class ActionsRepositoryImpl @Inject constructor(
    private val remoteDS: RemoteMessageDataSource,
    private val localDS: LocalMessageDataSource,
    private val applicationContext: Context
) : ActionsRepository {

    override suspend fun copyMessage(messageId: Int) {
        val content = localDS.getMessageById(messageId)?.content
            ?: throw MessageDoesNotExistException()
        val manager = applicationContext.getSystemService(ClipboardManager::class.java)
        ClipData.newPlainText(content.createLabel(), content).also { clipData ->
            manager.setPrimaryClip(clipData)
        }
    }

    override suspend fun deleteMessage(messageId: Int) {
        val entity = localDS.deleteMessage(messageId) ?: throw MessageDoesNotExistException()
        onFailureRestorePrevStateAndThrowError(action = { remoteDS.deleteMessage(messageId) }) {
            localDS.addMessage(entity)
        }
    }

    override suspend fun getMessageById(messageId: Int): IncomeMessage =
        localDS.getMessageById(messageId)?.toIncomeMessage() ?: requireNotNull(
            remoteDS.getMessages(1, 0, messageId, MessageNarrowList())
                .toFirstMessageSenderId(messageId)
        )

    override suspend fun getCurrentUser(): User {
        return remoteDS.getCurrentUser().toUser()
    }

    private fun String.createLabel(): String = take(LABEL_LENGTH).plus("...")

    companion object {
        private const val LABEL_LENGTH = 10
    }
}
