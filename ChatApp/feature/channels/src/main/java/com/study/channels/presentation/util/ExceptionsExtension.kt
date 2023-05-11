package com.study.channels.presentation.util

import com.study.channels.R
import com.study.channels.domain.exceptions.ChannelAlreadyExistsException
import com.study.channels.domain.exceptions.ChannelDoesNotHaveTopicsException
import com.study.channels.domain.exceptions.ChannelNotFoundException
import com.study.channels.domain.exceptions.InvalidChannelTitleException
import com.study.components.extension.toBaseErrorMessage
import com.study.components.model.UiError
import com.study.components.R as ComponentsR

internal fun Throwable.toErrorMessage(): UiError = when (this) {
    is ChannelNotFoundException -> UiError(
        this,
        messageRes = R.string.error_channel_not_found,
        descriptionRes = R.string.error_description_channel_not_found,
        imageRes = ComponentsR.drawable.ic_not_found
    )
    is ChannelDoesNotHaveTopicsException -> UiError(
        this,
        messageRes = R.string.error_channel_does_not_have_topics,
        null,
        null
    )
    is ChannelAlreadyExistsException -> UiError(
        this,
        messageRes = R.string.error_channel_already_exists,
        null,
        null
    )
    is InvalidChannelTitleException -> UiError(
        this,
        messageRes = R.string.error_invalid_channel_title,
        descriptionRes = R.string.error_invalid_channel_title_description,
        imageRes = ComponentsR.drawable.ic_error,
        descriptionArgs = maxLength
    )
    else -> toBaseErrorMessage()
}
