package com.study.chat.presentation.util

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.study.chat.R
import com.study.chat.domain.exceptions.ContentHasNotLoadedException
import com.study.chat.domain.exceptions.InvalidMessageContentException
import com.study.chat.domain.exceptions.InvalidTopicTitleException
import com.study.chat.domain.exceptions.SynchronizationException
import com.study.components.extension.toBaseErrorMessage
import com.study.components.model.UiError
import com.study.components.R as ComponentsR

internal fun Throwable.toErrorMessage(): UiError = when (this) {
    is ContentHasNotLoadedException -> UiError(
        this,
        messageRes = R.string.error_content_has_not_loaded_yet,
        descriptionRes = R.string.error_description_content_has_not_loaded_yet,
        imageRes = ComponentsR.drawable.ic_error
    )
    is SynchronizationException -> UiError(
        this,
        messageRes = R.string.error_cannot_synchronize,
        descriptionRes = R.string.error_description_cannot_synchronize,
        imageRes = ComponentsR.drawable.ic_error
    )
    is InvalidTopicTitleException -> UiError(
        this,
        messageRes = R.string.error_invalid_topic_title,
        descriptionRes = R.string.error_invalid_topic_title_description,
        descriptionArgs = maxLength,
        imageRes = ComponentsR.drawable.ic_error
    )
    is InvalidMessageContentException -> UiError(
        this,
        messageRes = R.string.error_invalid_message_content,
        descriptionRes = R.string.error_invalid_message_content_description,
        descriptionArgs = maxLength,
        imageRes = ComponentsR.drawable.ic_error
    )
    else -> toBaseErrorMessage()
}

internal fun String.toEmojiString(): String = runCatching {
    String(Character.toChars(Integer.decode("0x$this")))
}.getOrDefault(this)


internal fun AutoCompleteTextView.setupSuggestionsAdapter(suggestions: List<String>) {
    ArrayAdapter(
        this.context, android.R.layout.simple_dropdown_item_1line, suggestions
    ).also {
        setAdapter(it)
    }
}
