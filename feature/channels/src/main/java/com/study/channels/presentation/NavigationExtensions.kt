package com.study.channels.presentation

import android.net.Uri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.study.channels.R
import com.study.channels.presentation.channels.ChannelsFragment
import com.study.channels.presentation.channels.HolderChannelsFragment
import com.study.ui.NavConstants
import com.study.ui.R as CoreR

internal fun ChannelsFragment.navigateToChatFragment(
    channelId: Int,
    channelTitle: String,
    topicTitle: String? = null,
    topicColor: Int? = null
) {
    var uri = requireContext().getString(CoreR.string.deeplink_chat)
        .replace("{${NavConstants.CHANNEL_ID_KEY}}", channelId.toString())
        .replace("{${NavConstants.CHANNEL_TITLE_KEY}}", channelTitle)
        .replace("{${NavConstants.TOPIC_COLOR_KEY}}", topicColor?.toString() ?: NavConstants.NO_TOPIC_COLOR)

    if (topicTitle != null) {
        uri = uri.replace("{${NavConstants.TOPIC_TITLE_KEY}}", topicTitle)
    }

    findNavController().navigate(NavDeepLinkRequest.Builder.fromUri(Uri.parse(uri)).build())
}

internal fun HolderChannelsFragment.navigateToAddChannel() =
    findNavController().navigate(R.id.action_holderChannelsFragment_to_addChannelFragment)
