package com.study.chat.test

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.study.chat.data.local.LocalDataSourceProvider
import com.study.chat.data.local.MessageTestDatabase
import com.study.chat.data.remote.RemoteDataSourceProvider
import com.study.chat.di.GeneralDepContainer
import com.study.chat.shared.data.source.local.LocalMessageDataSource
import com.study.chat.shared.data.source.remote.RemoteMessageDataSource
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.database.model.tuple.MessageWithReactionsTuple
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MessagePagingMediatorTest {
    private lateinit var db: MessageTestDatabase
    private lateinit var messageDao: MessageDao
    private lateinit var reactionDao: ReactionDao
    private val server = RemoteDataSourceProvider().createServer()
    private val remoteProvider = RemoteDataSourceProvider()
    private val networkDep = remoteProvider.createNetworkDep()

    @After
    fun clear() {
        db.close()
    }

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MessageTestDatabase::class.java
        ).build()
        messageDao = db.messageDao()
        reactionDao = db.reactionDao()
    }

    @After
    fun tearDown() {
        runTest {
            messageDao.deleteAll()
            reactionDao.deleteAll()
        }
        db.close()
    }

    @Test
    fun loadMessages_ByDefault() = runTest {
        val mediator = GeneralDepContainer.createMessagePagingMediator(
            local = LocalMessageDataSource(
                messageDao,
                reactionDao
            ), remote = RemoteMessageDataSource(remoteProvider.provide(networkDep).zulipApi)
        )
        val pagingState = createPagingState()
        val result = mediator.load(LoadType.REFRESH, pagingState)

        assert(result is RemoteMediator.MediatorResult.Success)
        assert((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        assert(messageDao.countAllMessages() != 0)
    }

    @Test
    fun loadMessages_NoNetwork() = runTest {
        val mediator = GeneralDepContainer.createMessagePagingMediator(
            local = LocalDataSourceProvider().provide(messageDao, reactionDao),
            remote = RemoteMessageDataSource(remoteProvider.provide(networkDep).zulipApi)
        )
        val pagingState = createPagingState()
        server.shutdown()
        val result = mediator.load(LoadType.REFRESH, pagingState)

        assert(result is RemoteMediator.MediatorResult.Error)
        assert(messageDao.countAllMessages() == 0)
    }

    private fun createPagingState() = PagingState<Int, MessageWithReactionsTuple>(
        pages = listOf(),
        null,
        PagingConfig(10),
        10
    )
}
