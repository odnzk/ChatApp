package com.study.chat.test

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.study.chat.data.StubUserAuthRepository
import com.study.chat.data.local.MessageTestDatabase
import com.study.chat.data.remote.RemoteDataSourceProvider
import com.study.chat.di.launchChatFragment
import com.study.chat.shared.presentation.util.toEmojiString
import com.study.chat.util.TEST_TOPIC
import com.study.chat.util.screen.ChatScreen
import com.study.chat.util.screen.TestState
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import io.github.kakaocup.kakao.common.views.KView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.study.components.R as CoreR

@OptIn(ExperimentalCoroutinesApi::class)
internal class ChatFragmentTest : TestCase() {
    private lateinit var db: MessageTestDatabase
    private lateinit var messageDao: MessageDao
    private lateinit var reactionDao: ReactionDao
    private lateinit var topicDao: ChannelTopicDao

    private val authRepository = StubUserAuthRepository()
    private val dispatcher = UnconfinedTestDispatcher()
    private val server = RemoteDataSourceProvider().createServer()
    private val remoteProvider = RemoteDataSourceProvider()
    private val networkDep = remoteProvider.createNetworkDep()
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val zulipApi = remoteProvider.provide(networkDep).zulipApi

    @After
    fun clear() {
        runTest {
            messageDao.deleteAll()
            reactionDao.deleteAll()
            topicDao.deleteAll()
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
        topicDao = db.topicDao()
    }


    @Test
    fun messagesAreDisplayed_ByDefault() = run {
        launchChatFragment(
            zulipApi, reactionDao, messageDao, topicDao, authRepository, context, dispatcher
        )
        val chatScreen = ChatScreen()

        step("Отображение списка сообщений") {
            chatScreen.messageList {
                isDisplayed()
            }
        }
        step("Отображение каждого сообщения") {
            chatScreen.messageList.children<ChatScreen.MessageItem> {
                checkIfDisplayed()
            }
        }
        step("Отображение успешной загрузки") {
            chatScreen.checkScreenStateView(TestState.SUCCESS)
        }
    }


    @Test
    fun messageWithoutReaction_ByDefault() = run {
        launchChatFragment(
            zulipApi, reactionDao, messageDao, topicDao, authRepository, context, dispatcher
        )
        val chatScreen = ChatScreen()

        chatScreen.messageList.lastChild<ChatScreen.MessageItem> {
            step("Сообщение отображено корректно") {
                checkIfDisplayed()
            }
            step("Pеакции не видно") {
                reactions.isNotDisplayed()
            }
        }
    }


    @Test
    fun messageWithMyReaction_ByDefault() = run {
        launchChatFragment(
            zulipApi, reactionDao, messageDao, topicDao, authRepository, context, dispatcher
        )
        val chatScreen = ChatScreen()

        chatScreen.messageList.firstChild<ChatScreen.MessageItem> {
            step("Сообщение отображено корректно") {
                content.containsText("message with emoji")
                checkIfDisplayed()
            }
            step("Реакции видны") {
                reactions.isDisplayed()
            }
            step("Ecть одна реакция с состоянием isSelected = true") {
                KView { containsText("1f60c".toEmojiString()) }.isDisplayed()
            }
        }
    }


    @Test
    fun initStateIsDisplayed_ByDefault() = run {
        launchChatFragment(
            zulipApi, reactionDao, messageDao, topicDao, authRepository, context, dispatcher
        )
        val chatScreen = ChatScreen()

        step("Текущий топик отображается") {
            chatScreen.chatTopic {
                isDisplayed()
                containsText(TEST_TOPIC)
            }
        }
        step("Подсказка для поля ввода сообщения отображается") {
            chatScreen.inputViewTextInputLayout.hasHint(CoreR.string.hint_send_message)
        }
        step("Список сообщений виден") {
            chatScreen.messageList {
                isDisplayed()
            }
        }
        step("Отображение успешной загрузки") {
            chatScreen.checkScreenStateView(TestState.SUCCESS)
        }
    }


    @Test
    fun getMessages_NetworkError() = run {
        server.shutdown()
        launchChatFragment(
            zulipApi, reactionDao, messageDao, topicDao, authRepository, context, dispatcher
        )
        val chatScreen = ChatScreen()

        step("Лист сообщений не виден") {
            chatScreen.messageList {
                isNotDisplayed()
            }
        }
        step("Отображение ошибки") {
            chatScreen.checkScreenStateView(TestState.ERROR)
        }
    }

}

