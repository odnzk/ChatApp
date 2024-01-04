package com.study.chat.emoji.presentation.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

internal class EmojiListReducer @Inject constructor() :
    DslReducer<EmojiListEvent, EmojiListState, EmojiListEffect, EmojiListCommand>() {

    override fun Result.reduce(event: EmojiListEvent): Any = when (event) {
        is EmojiListEvent.Internal.LoadingError -> {
            state { copy(isLoading = false, error = event.error) }
        }
        is EmojiListEvent.Internal.LoadingSuccess -> {
            state { copy(isLoading = false, emojis = event.emojis) }
        }
        EmojiListEvent.Ui.Init -> {
            state { copy(isLoading = true) }
            commands { +EmojiListCommand.LoadEmojiList }
        }
        EmojiListEvent.Ui.Reload -> {
            state { copy(isLoading = true, error = null) }
            commands { +EmojiListCommand.LoadEmojiList }
        }
    }
}
