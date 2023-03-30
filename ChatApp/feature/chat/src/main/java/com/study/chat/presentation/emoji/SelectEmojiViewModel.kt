package com.study.chat.presentation.emoji

import com.study.chat.data.StubEmojiRepository
import com.study.chat.domain.model.Emoji
import com.study.chat.domain.repository.EmojiRepository
import com.study.components.BaseViewModel
import com.study.components.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SelectEmojiViewModel : BaseViewModel<List<Emoji>>() {
    private val emojiRepository: EmojiRepository = StubEmojiRepository
    override val _state: MutableStateFlow<ScreenState<List<Emoji>>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        safeLaunch {
            _state.value = ScreenState.Success(emojiRepository.getEmoji())
        }
    }

}

