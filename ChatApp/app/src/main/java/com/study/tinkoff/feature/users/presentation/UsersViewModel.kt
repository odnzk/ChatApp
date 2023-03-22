package com.study.tinkoff.feature.users.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.components.ScreenState
import com.study.domain.model.User
import com.study.tinkoff.di.StubDiContainer
import com.study.tinkoff.feature.users.domain.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {
    private val usersRepository: UsersRepository = StubDiContainer.bindsUsersRepository()
    private val _state: MutableStateFlow<ScreenState<List<User>>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun onEvent(event: UsersFragmentEvent) = viewModelScope.launch {
        when (event) {
            UsersFragmentEvent.Reload -> loadData()
        }
    }

    private fun loadData() =
        viewModelScope.launch {
            _state.update {
                ScreenState.Success(usersRepository.getUsers())
            }
        }

}
