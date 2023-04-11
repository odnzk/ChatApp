package com.study.chat.presentation.emoji.elm

import com.study.chat.domain.repository.EmojiRepository
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class EmojiListFactory(
    repository: EmojiRepository
) {
    private val store by lazy {
        ElmStoreCompat(
            EmojiListState(),
            EmojiListReducer(),
            EmojiListActor(repository)
        )
    }

    fun create() = store
}

