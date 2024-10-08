package com.study.chat.test

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.study.chat.data.local.MessageTestDatabase
import com.study.chat.util.TEST_CHANNEL
import com.study.chat.util.TEST_TOPIC
import com.study.chat.util.createMessageEntity
import com.study.chat.common.data.source.local.message.dao.MessageDao
import com.study.chat.common.data.source.local.message.dao.ReactionDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var db: MessageTestDatabase
    private lateinit var messageDao: MessageDao
    private lateinit var reactionDao: ReactionDao

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
    fun closeConnection() {
        db.close()
    }

    @Test
    fun messageInsertion_ByDefault() = runTest {
        val initialCount = messageDao.countMessagesByChannelAndTopic(TEST_CHANNEL, TEST_TOPIC)
        val message = createMessageEntity(TEST_CHANNEL, TEST_TOPIC)
        messageDao.insert(listOf(message))

        assert(
            messageDao.countMessagesByChannelAndTopic(
                TEST_CHANNEL,
                TEST_TOPIC
            ) == initialCount + 1
        )
        assert(messageDao.getMessageById(message.id) == message)
    }

    @Test
    fun messageInsertion_IdenticalItems() = runTest {
        val initialCount = messageDao.countMessagesByChannelAndTopic(TEST_CHANNEL, TEST_TOPIC)
        val message = createMessageEntity(TEST_CHANNEL, TEST_TOPIC)
        messageDao.insert(listOf(message, message, message))

        assert(
            messageDao.countMessagesByChannelAndTopic(
                TEST_CHANNEL,
                TEST_TOPIC
            ) == initialCount + 1
        )
        assert(messageDao.getMessageById(message.id) == message)
    }

    @Test
    fun messageRemoving() = runTest {
        val initialCount = messageDao.countMessagesByChannelAndTopic(TEST_CHANNEL, TEST_TOPIC)
        val message = createMessageEntity(TEST_CHANNEL, TEST_TOPIC)
        messageDao.delete(listOf(message))

        assert(
            messageDao.countMessagesByChannelAndTopic(
                TEST_CHANNEL,
                TEST_TOPIC
            ) == initialCount - 1
        )
        assert(messageDao.getMessageById(message.id) == null)
    }

    @Test
    fun messageUpsert_Insert() = runTest {
        val initialCount = messageDao.countMessagesByChannelAndTopic(TEST_CHANNEL, TEST_TOPIC)
        val message = createMessageEntity(TEST_CHANNEL, TEST_TOPIC)
        messageDao.upsert(listOf(message))

        assert(
            messageDao.countMessagesByChannelAndTopic(
                TEST_CHANNEL,
                TEST_TOPIC
            ) == initialCount + 1
        )
        assert(messageDao.getMessageById(message.id) == message)
    }

    @Test
    fun messageUpsert_Update() = runTest {
        val helperString = "CONTENT"
        val initialCount = messageDao.countMessagesByChannelAndTopic(TEST_CHANNEL, TEST_TOPIC)
        val message = createMessageEntity(TEST_CHANNEL, TEST_TOPIC, id = 99)
        messageDao.insert(listOf(message))
        val updatedMessage = message.copy(content = helperString)
        messageDao.upsert(listOf(updatedMessage))

        assert(
            messageDao.countMessagesByChannelAndTopic(
                TEST_CHANNEL,
                TEST_TOPIC
            ) == initialCount + 1
        )
        assert(messageDao.getMessageById(message.id) == updatedMessage)
    }
}
