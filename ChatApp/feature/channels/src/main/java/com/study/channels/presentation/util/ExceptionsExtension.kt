package com.study.channels.presentation.util

import com.study.channels.R
import com.study.channels.domain.exceptions.ChannelDoesNotHaveTopicsException
import com.study.channels.domain.exceptions.ChannelNotFoundException
import com.study.components.extensions.UiError
import com.study.components.extensions.toBaseErrorMessage

internal fun Throwable.toErrorMessage(): UiError = when (this) {
    is ChannelNotFoundException -> UiError(
        this,
        R.string.error_channel_not_found,
        R.string.error_description_channel_not_found,
        com.study.components.R.drawable.ic_not_found
    )
    is ChannelDoesNotHaveTopicsException -> UiError(
        this,
        R.string.error_channel_does_not_have_topics,
        null,
        null
    )
    else -> toBaseErrorMessage()
}
