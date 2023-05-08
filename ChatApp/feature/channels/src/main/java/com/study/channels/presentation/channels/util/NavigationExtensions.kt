package com.study.channels.presentation.channels.util

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.study.channels.R
import com.study.ui.NavConstants
import com.study.ui.R as CoreR

internal fun Fragment.navigateToChatFragment(channelId: Int, topicTitle: String? = null) {
    val uri = requireContext().getString(CoreR.string.deeplink_chat)
        .replace("{${NavConstants.CHANNEL_ID_KEY}}", channelId.toString())
        .run {
            if (topicTitle != null) {
                replace("{${NavConstants.TOPIC_TITLE_KEY}}", topicTitle)
            } else this
        }
    findNavController().navigate(NavDeepLinkRequest.Builder.fromUri(Uri.parse(uri)).build())
}

internal fun Fragment.navigateToAddChannel() =
    findNavController().navigate(R.id.action_holderChannelsFragment_to_addChannelFragment)
