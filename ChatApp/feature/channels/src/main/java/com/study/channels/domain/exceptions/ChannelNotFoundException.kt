package com.study.channels.domain.exceptions

import com.study.channels.R
import com.study.components.extensions.UserFriendlyError
import com.study.components.extensions.toBaseErrorMessage
import com.study.components.R as CoreR

internal sealed class ChannelModuleException : RuntimeException()

internal class ChannelNotFoundException : ChannelModuleException()

internal class ChannelDoesNotHaveTopicsException : ChannelModuleException()

private fun ChannelModuleException.toErrorMessage(): UserFriendlyError {
    val (messageRes, descriptionRes, imageRes) = when (this) {
        is ChannelNotFoundException -> Triple(
            R.string.error_channel_not_found,
            R.string.error_description_channel_not_found,
            CoreR.drawable.ic_not_found
        )
        is ChannelDoesNotHaveTopicsException -> Triple(
            R.string.error_channel_does_not_have_topics,
            null,
            null
        )
    }
    return UserFriendlyError(this, messageRes, descriptionRes, imageRes)
}

internal fun Throwable.toErrorMessage(): UserFriendlyError {
    return if (this is ChannelModuleException) toErrorMessage() else toBaseErrorMessage()
}
