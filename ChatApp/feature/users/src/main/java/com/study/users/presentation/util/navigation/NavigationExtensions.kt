package com.study.users.presentation.util.navigation

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.study.ui.NavConstants
import com.study.ui.R

internal fun Fragment.navigateToProfileFragment(userId: Int) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            requireContext().getString(R.string.deeplink_user_profile)
                .replace("{${NavConstants.USER_ID_KEY}}", userId.toString())
        )
    ).build()
    findNavController().navigate(deeplink)
}
