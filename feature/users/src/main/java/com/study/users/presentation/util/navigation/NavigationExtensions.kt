package com.study.users.presentation.util.navigation

import android.net.Uri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.study.ui.NavConstants
import com.study.ui.R
import com.study.users.presentation.UsersFragment

internal fun UsersFragment.navigateToProfileFragment(userId: Int) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            requireContext().getString(R.string.deeplink_user_profile)
                .replace("{${NavConstants.USER_ID_KEY}}", userId.toString())
        )
    ).build()
    findNavController().navigate(deeplink)
}
