package com.study.chat.presentation.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.study.chat.presentation.chat.ChatFragment
import com.study.chat.presentation.chat.ChatFragmentDirections

internal fun Fragment.navigateToEmojiListFragment(resultKey: String) {
    findNavController().navigate(
        ChatFragmentDirections.actionChatFragmentToEmojiListFragment(resultKey)
    )
}

internal fun ChatFragment.navigateToChannelTopic(channelTitle: String, topicTitle: String) {
    findNavController().navigate(
        ChatFragmentDirections.actionChatFragmentSelf(
            channelTitle,
            topicTitle
        )
    )
}
