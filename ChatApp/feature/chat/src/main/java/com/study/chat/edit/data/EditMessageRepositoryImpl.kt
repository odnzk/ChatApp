package com.study.chat.edit.data

import com.study.chat.edit.domain.repository.EditMessageRepository
import com.study.chat.shared.data.mapper.toFirstMessageSenderId
import com.study.chat.shared.data.mapper.toIncomeMessage
import com.study.chat.shared.data.source.local.LocalMessageDataSource
import com.study.chat.shared.data.source.remote.RemoteMessageDataSource
import com.study.chat.shared.domain.model.IncomeMessage
import com.study.chat.shared.domain.model.MessageDoesNotExistException
import com.study.common.extension.onFailureRestorePrevStateAndThrowError
import com.study.network.model.request.message.MessageNarrowList
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class EditMessageRepositoryImpl @Inject constructor(
    private val remoteDS: RemoteMessageDataSource,
    private val localDS: LocalMessageDataSource,
) : EditMessageRepository {

    override suspend fun getMessageById(messageId: Int): IncomeMessage =
        localDS.getMessageById(messageId)?.toIncomeMessage() ?: requireNotNull(
            remoteDS.getMessages(1, 0, messageId, MessageNarrowList())
                .toFirstMessageSenderId(messageId)
        )


    override suspend fun updateMessage(messageId: Int, content: String, topic: String) {
        val entity = localDS.updateMessage(messageId, content, topic)
            ?: throw MessageDoesNotExistException()
        onFailureRestorePrevStateAndThrowError({
            remoteDS.updateMessage(
                messageId,
                content,
                topic
            )
        }) {
            localDS.updateMessage(entity)
        }
    }
}
