package com.study.channels.presentation.holder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HolderChannelViewModel : ViewModel() {
    private val _state: MutableStateFlow<String> = MutableStateFlow("")
    val state = _state.asStateFlow()

    fun search(query: String) {
        _state.value = query
    }
}
