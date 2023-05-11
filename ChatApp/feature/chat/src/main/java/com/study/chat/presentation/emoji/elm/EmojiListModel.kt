package com.study.chat.presentation.emoji.elm

import com.study.chat.presentation.util.model.UiEmoji

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

internal data class EmojiListState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val emojis: List<UiEmoji> = emptyList()
)
