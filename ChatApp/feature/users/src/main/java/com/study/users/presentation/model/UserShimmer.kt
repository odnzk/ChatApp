package com.study.users.presentation.model

import com.study.components.recycler.shimmer.ShimmerItem

internal object UserShimmer : ShimmerItem<UiUser> {
    override fun content(): UiUser? = null

    const val DEFAULT_SHIMMER_COUNT = 8
}

