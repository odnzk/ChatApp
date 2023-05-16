package com.study.chat.emoji.presentation.elm

import com.study.chat.chat.presentation.model.UiEmoji

internal data class EmojiListState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val emojis: List<UiEmoji> = emptyList()
)

internal sealed interface EmojiListEvent {
    sealed interface Internal : EmojiListEvent {
        class LoadingSuccess(val emojis: List<UiEmoji>) : Internal
        class LoadingError(val error: Throwable) : Internal
    }

    sealed interface Ui : EmojiListEvent {
        object Init : Ui
        object Reload : Ui
    }
}

internal sealed interface EmojiListEffect

internal sealed interface EmojiListCommand {
    object LoadEmojiList : EmojiListCommand
}
