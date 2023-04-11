package com.study.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.study.common.ScreenState
import com.study.common.extensions.runCatchingNonCancellation
import com.study.components.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest

abstract class SearchViewModel<T : Any>(private val savedStateHandle: SavedStateHandle) :
    BaseViewModel<T>() {
    private val searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    protected abstract val searchAction: suspend (query: String) -> T
    protected abstract val lastSearchedKey: String?
    private var jobObservingQuery: Job? = null

    protected suspend fun search(query: String) = searchQuery.emit(query)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    protected fun observeSearchQuery() {
        jobObservingQuery?.cancel()
        jobObservingQuery =
            searchQuery
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .debounce(SEARCH_DEBOUNCE)
                .mapLatest {
                    val resultList = runCatchingNonCancellation { searchAction(it) }
                    _state.value = if (resultList.isSuccess) {
                        ScreenState.Success(resultList.getOrThrow())
                    } else {
                        ScreenState.Error(resultList.exceptionOrNull()!!)
                    }
                }.flowOn(Dispatchers.Default).launchIn(viewModelScope)
    }

    protected suspend fun restoreLastSearchedQuery() {
        lastSearchedKey?.let {
            savedStateHandle.get<String>(it)?.let { query -> search(query) }
        }
    }

    protected fun saveSearchQuery() {
        lastSearchedKey?.let { savedStateHandle[it] = searchQuery.value }
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }
}
