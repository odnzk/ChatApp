package com.study.users.presentation

import androidx.lifecycle.SavedStateHandle
import com.study.common.ScreenState
import com.study.search.SearchViewModel
import com.study.users.data.RemoteUserRepository
import com.study.users.domain.repository.UsersRepository
import com.study.users.domain.usecase.GetUsersUseCase
import com.study.users.presentation.model.UiUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class UsersViewModel(savedStateHandle: SavedStateHandle) :
    SearchViewModel<List<UiUser>>(savedStateHandle) {
    private val repository: UsersRepository = RemoteUserRepository()
    private val getUsersUseCase = GetUsersUseCase(repository, Dispatchers.Default)

    private var fullUserList: List<UiUser> = emptyList()
    override val searchAction: suspend (query: String) -> List<UiUser>
        get() = { query -> fullUserList.filter { it.name.startsWith(query) } }
    override val lastSearchedKey: String? = null
    override val _state: MutableStateFlow<ScreenState<List<UiUser>>> =
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

    private fun loadData() = safeLaunch {
        val result = getUsersUseCase()
        _state.value = ScreenState.Success(result)
        fullUserList = result
    }

}
