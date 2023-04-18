package com.study.chat.presentation.emoji.elm

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
            commands { +EmojiListCommand.LoadEmojiList }
        }
        EmojiListEvent.Ui.Reload -> {
            commands { +EmojiListCommand.LoadEmojiList }
        }
    }
}
