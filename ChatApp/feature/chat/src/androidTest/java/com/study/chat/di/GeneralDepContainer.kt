package com.study.chat.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.study.chat.chat.data.ChatRepositoryImpl
import com.study.chat.chat.data.MessagesPagingMediator
import com.study.chat.chat.domain.repository.ChatRepository
import com.study.chat.shared.data.source.local.LocalMessageDataSource
import com.study.chat.shared.data.source.remote.RemoteMessageDataSource
import com.study.chat.util.TEST_CHANNEL
import com.study.chat.util.TEST_TOPIC

internal object GeneralDepContainer {
    val applicationContext: Context = ApplicationProvider.getApplicationContext()

    private fun messagePagingFactory(
        local: LocalMessageDataSource, remote: RemoteMessageDataSource
    ) = object : MessagesPagingMediator.Factory {
        override fun create(
            channelId: Int, topicName: String?, query: String
        ): MessagesPagingMediator = MessagesPagingMediator(
            localDs = local,
            remoteDS = remote,
            query = query,
            channelId = channelId,
            topicTitle = topicName
        )
    }

    fun createMessagePagingMediator(
        local: LocalMessageDataSource,
        remote: RemoteMessageDataSource,
        channelId: Int = TEST_CHANNEL,
        topicName: String = TEST_TOPIC,
        query: String = ""
    ) = MessagesPagingMediator(
        local, remote, channelId = channelId, topicTitle = topicName, query = query
    )

    fun createMessageRepositoryImpl(
        local: LocalMessageDataSource, remote: RemoteMessageDataSource
    ): ChatRepository {
        return ChatRepositoryImpl(
            remoteDS = remote,
            localDS = local,
            applicationContext = applicationContext,
            messagePagingFactory(local, remote)
        )
    }

}
