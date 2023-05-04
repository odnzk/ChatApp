package com.study.chat.presentation.util

import com.study.chat.R
import com.study.chat.domain.exceptions.ContentHasNotLoadedException
import com.study.chat.domain.exceptions.SynchronizationException
import com.study.components.extension.toBaseErrorMessage
import com.study.components.model.UiError
import com.study.components.R as ComponentsR

internal fun Throwable.toErrorMessage(): UiError = when (this) {
    is ContentHasNotLoadedException -> UiError(
        this,
        R.string.error_content_has_not_loaded_yet,
        R.string.error_description_content_has_not_loaded_yet,
        ComponentsR.drawable.ic_error
    )
    is SynchronizationException -> UiError(
        this,
        R.string.error_cannot_synchronize,
        R.string.error_description_cannot_synchronize,
        ComponentsR.drawable.ic_error
    )
    else -> toBaseErrorMessage()
}

internal fun String.toEmojiString(): String = runCatching {
    String(Character.toChars(Integer.decode("0x$this")))
}.getOrDefault(this)
