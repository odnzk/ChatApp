package com.study.chat.di

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.study.chat.chat.presentation.ChatFragment
import com.study.chat.data.StubUserAuthRepository
import com.study.chat.shared.di.ChatDep
import com.study.chat.shared.di.ChatDepStore
import com.study.chat.util.TEST_CHANNEL
import com.study.chat.util.TEST_TOPIC
import com.study.database.dataSource.MessageLocalDataSource
import com.study.network.dataSource.MessageRemoteDataSource
import com.study.ui.NavConstants
import com.study.ui.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

private val searchQueryFlow: Flow<String> = flowOf()

internal fun launchChatFragment(
    isUiNeeded: Boolean = true,
    remoteDS: MessageRemoteDataSource,
    localDS: MessageLocalDataSource,
    dispatcher: CoroutineDispatcher
): FragmentScenario<ChatFragment> {
    ChatDepStore.dep = chatFragmentDep(remoteDS, localDS, dispatcher)
    val args = bundleOf(
        NavConstants.CHANNEL_ID_KEY to TEST_CHANNEL, NavConstants.TOPIC_TITLE_KEY to TEST_TOPIC
    )
    val fragmentThemeResId = R.style.Theme_Messenger
    return if (isUiNeeded) {
        launchFragmentInContainer(
            fragmentArgs = args, themeResId = fragmentThemeResId
        )
    } else {
        launchFragment(fragmentArgs = args, themeResId = fragmentThemeResId)
    }
}


fun chatFragmentDep(
    remoteDS: MessageRemoteDataSource,
    localDS: MessageLocalDataSource,
    dispatcher: CoroutineDispatcher
) = object : ChatDep {
    override val dispatcher = dispatcher
    override val searchFlow = searchQueryFlow
    override val messageRemoteDataSource = remoteDS
    override val messageLocalDataSource = localDS
    override val userAuthRepository = StubUserAuthRepository()
}
