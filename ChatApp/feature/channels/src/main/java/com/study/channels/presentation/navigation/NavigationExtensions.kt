package com.study.channels.presentation.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.study.ui.R

internal fun NavController.toChannelTopic(channelId: Int, lastMessageId: Int) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            context.getString(R.string.deeplink_chat)
                .replace("{channelId}", channelId.toString())
        )
    ).build()
    navigate(deeplink)
}
