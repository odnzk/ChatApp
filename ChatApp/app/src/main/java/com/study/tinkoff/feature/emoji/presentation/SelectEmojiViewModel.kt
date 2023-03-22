package com.study.tinkoff.feature.emoji.presentation

import androidx.lifecycle.ViewModel
import com.study.domain.model.Emoji
import com.study.tinkoff.di.StubDiContainer
import com.study.tinkoff.feature.emoji.domain.EmojiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SelectEmojiViewModel : ViewModel() {
    private val emojiRepository: EmojiRepository = StubDiContainer.bindsEmojiRepository()

    private val _state: MutableStateFlow<List<Emoji>> = MutableStateFlow(emptyList())
    val state = _state.asStateFlow()

    init {
        _state.update {
            emojiRepository.getEmoji()
        }
    }

}

