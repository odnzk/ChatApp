package com.study.common.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
abstract class BaseSearcher {

    abstract val scope: CoroutineScope
    abstract val filteredQueries: MutableSharedFlow<String>

    private val searchQueries: MutableSharedFlow<String> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    init {
        searchQueries
            .filter { it.isEmpty() || it.isNotBlank() }
            .distinctUntilChanged()
            .debounce(SEARCH_DEBOUNCE)
            .flatMapLatest { query ->
                flow<Unit> { filteredQueries.emit(query) }
            }
            .launchIn(scope)
    }

    fun clear() {
        scope.cancel()
    }

    fun emitQuery(newQuery: String) {
        searchQueries.tryEmit(newQuery)
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }
}
