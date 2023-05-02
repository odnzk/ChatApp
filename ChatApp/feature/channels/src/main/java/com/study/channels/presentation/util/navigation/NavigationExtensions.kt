package com.study.channels.presentation.util.navigation

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.study.ui.NavConstants
import com.study.ui.R

internal fun Fragment.navigateToChannelTopic(channelTitle: String, topicTitle: String) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            requireContext().getString(R.string.deeplink_chat)
                .replace("{${NavConstants.CHANNEL_TITLE_KEY}}", channelTitle)
                .replace("{${NavConstants.TOPIC_TITLE_KEY}}", topicTitle)
        )
    ).build()
    findNavController().navigate(deeplink)
}
