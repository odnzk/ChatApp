package com.study.chat.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.study.chat.data.MessageRepositoryImpl
import com.study.chat.data.pagination.MessagesPagingMediator
import com.study.chat.domain.repository.MessageRepository
import com.study.chat.util.TEST_CHANNEL
import com.study.chat.util.TEST_TOPIC
import com.study.database.dataSource.MessageLocalDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher


@OptIn(ExperimentalCoroutinesApi::class)
internal object GeneralDepContainer {
    val applicationContext: Context = ApplicationProvider.getApplicationContext()

    private fun messagePagingFactory(
        local: MessageLocalDataSource,
        remote: MessageRemoteDataSource
    ) = object : MessagesPagingMediator.Factory {
        override fun create(
            channelTitle: String,
            topicName: String,
            query: String
        ): MessagesPagingMediator {
            return MessagesPagingMediator(
                localDs = local,
                remoteDS = remote,
                query = query,
                channelTitle = channelTitle,
                topicTitle = topicName
            )
        }
    }

    fun createMessagePagingMediator(
        local: MessageLocalDataSource,
        remote: MessageRemoteDataSource,
        channelTitle: String = TEST_CHANNEL,
        topicName: String = TEST_TOPIC,
        query: String = ""

    ) = MessagesPagingMediator(local, remote, channelTitle, topicName, query)

    fun createMessageRepositoryImpl(
        local: MessageLocalDataSource,
        remote: MessageRemoteDataSource,
        dispatcher: TestDispatcher
    ): MessageRepository {
        return MessageRepositoryImpl(
            remoteDS = remote,
            localDS = local,
            dispatcher,
            messagePagingFactory(local, remote)
        )
    }


}
