package com.study.chat.domain.exceptions

import com.study.components.extensions.UserFriendlyError
import com.study.components.extensions.toBaseErrorMessage
import com.study.feature.R


internal sealed class ChatModuleException : RuntimeException()
internal class SynchronizationException : ChatModuleException()
internal class ContentHasNotLoadedException : ChatModuleException()

private fun ChatModuleException.toErrorMessage(): UserFriendlyError {
    val (messageRes, descriptionRes, imageRes) = when (this) {
        is ContentHasNotLoadedException -> Triple(
            R.string.error_content_has_not_loaded_yet,
            R.string.error_description_content_has_not_loaded_yet,
            com.study.components.R.drawable.ic_error
        )
        is SynchronizationException -> Triple(
            R.string.error_cannot_synchronize,
            R.string.error_description_cannot_synchronize,
            com.study.components.R.drawable.ic_error
        )
    }
    return UserFriendlyError(this, messageRes, descriptionRes, imageRes)
}

internal fun Throwable.toErrorMessage(): UserFriendlyError {
    return if (this is ChatModuleException) toErrorMessage() else toBaseErrorMessage()
}
