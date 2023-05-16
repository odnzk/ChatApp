package com.study.chat.test

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.study.chat.data.local.LocalDataSourceProvider
import com.study.chat.data.local.MessageTestDatabase
import com.study.chat.data.remote.RemoteDataSourceProvider
import com.study.chat.di.GeneralDepContainer
import com.study.chat.shared.data.source.remote.RemoteMessageDataSource
import com.study.chat.util.TEST_CHANNEL
import com.study.chat.util.TEST_TOPIC
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    private lateinit var db: MessageTestDatabase
    private lateinit var messageDao: MessageDao
    private lateinit var reactionDao: ReactionDao
    private val remoteProvider = RemoteDataSourceProvider()
    private val networkDep = remoteProvider.createNetworkDep()

    @After
    fun clear() {
        runTest {
            messageDao.deleteAll()
            reactionDao.deleteAll()
        }
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


    @Test
    fun getMessages_DbNotEmpty() = runTest {
        val localDS = LocalDataSourceProvider().provide(messageDao, reactionDao)
        val api = RemoteDataSourceProvider().provide(networkDep).zulipApi
        val repository =
            GeneralDepContainer.createMessageRepositoryImpl(localDS, RemoteMessageDataSource(api))

        // Test verification goes simply by getting a list of PagingData,
        // if the repository did not get anything, the test would not pass due to a time limit
        repository.getMessages(TEST_CHANNEL, TEST_TOPIC, "").first()
    }
}
