package com.study.users.presentation

import androidx.lifecycle.SavedStateHandle
import com.study.components.ScreenState
import com.study.search.SearchViewModel
import com.study.users.data.StubUsersRepository
import com.study.users.domain.model.User
import com.study.users.domain.repository.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class UsersViewModel(savedStateHandle: SavedStateHandle) :
    SearchViewModel<List<User>>(savedStateHandle) {
    private val usersRepository: UsersRepository = StubUsersRepository()
    private var jobObservingUsers: Job? = null
    override val searchAction: suspend (query: String) -> List<User>
        get() = { usersRepository.getUsersByEmail(it) }
    override val lastSearchedKey: String? = null
    override val _state: MutableStateFlow<ScreenState<List<User>>> =
        MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
        observeSearchQuery()
    }

    fun onEvent(event: UsersFragmentEvent) {
        safeLaunch {
            when (event) {
                UsersFragmentEvent.Reload -> loadData()
                is UsersFragmentEvent.Search -> search(event.query)
            }
        }
    }

    private fun loadData() {
        jobObservingUsers?.cancel()
        jobObservingUsers = safeLaunch {
            usersRepository.getUsers().collectLatest { users ->
                _state.value = ScreenState.Success(users)
            }
        }
    }

}
