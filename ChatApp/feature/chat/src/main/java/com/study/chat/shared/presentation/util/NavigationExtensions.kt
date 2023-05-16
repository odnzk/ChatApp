package com.study.chat.shared.presentation.util

import androidx.navigation.fragment.findNavController
import com.study.chat.actions.presentation.ActionsFragment
import com.study.chat.actions.presentation.ActionsFragmentDirections
import com.study.chat.chat.presentation.ChatFragment
import com.study.chat.chat.presentation.ChatFragmentDirections

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
