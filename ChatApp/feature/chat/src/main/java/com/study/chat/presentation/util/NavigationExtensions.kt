package com.study.chat.presentation.util

import androidx.navigation.fragment.findNavController
import com.study.chat.presentation.actions.ActionsFragment
import com.study.chat.presentation.actions.ActionsFragmentDirections
import com.study.chat.presentation.chat.ChatFragment
import com.study.chat.presentation.chat.ChatFragmentDirections

internal fun ChatFragment.navigateToActionsFragment(
    messageId: Int,
    resultKey: String
) = findNavController().navigate(
    ChatFragmentDirections.actionChatFragmentToActionsFragment(messageId, resultKey)
)

internal fun ChatFragment.navigateToChannelTopic(
    channelId: Int,
    channelTitle: String,
    topicTitle: String
) =
    findNavController().navigate(
        ChatFragmentDirections.actionChatFragmentSelf(channelId, channelTitle, topicTitle)
    )


internal fun ActionsFragment.navigateToEmojiListFragment(resultKey: String) =
    findNavController().navigate(
        ActionsFragmentDirections.actionActionsFragmentToEmojiListFragment(resultKey)
    )

internal fun ActionsFragment.navigateToEditMessageFragment(messageId: Int) {
    findNavController().navigate(
        ActionsFragmentDirections.actionActionsFragmentToEditMessageFragment(
            messageId
        )
    )
}
