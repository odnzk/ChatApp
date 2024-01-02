package com.study.chat.di

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.study.auth.api.Authentificator
import com.study.chat.chat.presentation.ChatFragment
import com.study.chat.common.di.ChatDep
import com.study.chat.common.di.ChatDepStore
import com.study.chat.util.TEST_CHANNEL
import com.study.chat.util.TEST_TOPIC
import com.study.database.dao.ChannelTopicDao
import com.study.database.dao.MessageDao
import com.study.database.dao.ReactionDao
import com.study.network.api.ChannelsApi
import com.study.network.api.MessagesApi
import com.study.ui.NavConstants
import com.study.ui.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val searchQueryFlow: Flow<String> = flowOf()

internal fun launchChatFragment(
    zulipApi: MessagesApi,
    channelsApi: ChannelsApi,
    reactionDao: ReactionDao,
    messageDao: MessageDao,
    topicDao: ChannelTopicDao,
    authentificator: Authentificator,
    context: Context,
    dispatcher: CoroutineDispatcher,
    isUiNeeded: Boolean = true,
): FragmentScenario<ChatFragment> {
    ChatDepStore.dep = chatFragmentDep(
        zulipApi = zulipApi,
        channelsApi = channelsApi,
        reactionDao, messageDao, topicDao, authentificator, context, dispatcher
    )
    val args = bundleOf(
        NavConstants.CHANNEL_ID_KEY to TEST_CHANNEL, NavConstants.TOPIC_TITLE_KEY to TEST_TOPIC
    )
    val fragmentThemeResId = R.style.Theme_ChatApp
    return if (isUiNeeded) {
        launchFragmentInContainer(fragmentArgs = args, themeResId = fragmentThemeResId)
    } else {
        launchFragment(fragmentArgs = args, themeResId = fragmentThemeResId)
    }
}


fun chatFragmentDep(
    zulipApi: MessagesApi,
    channelsApi: ChannelsApi,
    reactionDao: ReactionDao,
    messageDao: MessageDao,
    topicDao: ChannelTopicDao,
    authentificator: Authentificator,
    context: Context,
    dispatcher: CoroutineDispatcher
) = object : ChatDep {
    override val dispatcher: CoroutineDispatcher = dispatcher
    override val searchFlow: Flow<String> = searchQueryFlow
    override val messagesApi: MessagesApi = zulipApi
    override val channelsApi: ChannelsApi = channelsApi
    override val messageDao: MessageDao = messageDao
    override val reactionDao: ReactionDao = reactionDao
    override val topicDao: ChannelTopicDao = topicDao
    override val authentificator: Authentificator = authentificator
    override val applicationContext: Context = context
}
