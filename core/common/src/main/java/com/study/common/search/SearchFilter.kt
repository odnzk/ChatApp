package com.study.common.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchFilter(private val listener: Listener) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val allQueries: MutableSharedFlow<String> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    private val filteredQueries = MutableSharedFlow<String>()

    init {
        allQueries
            .filter { it.isEmpty() || it.isNotBlank() }
            .distinctUntilChanged()
            .debounce(SEARCH_DEBOUNCE)
            .flatMapLatest { query ->
                flow<Unit> { filteredQueries.emit(query) }
            }.launchIn(scope)

        scope.launch {
            filteredQueries.collect { query ->
                listener.onNewQuery(query)
            }
        }

    }

    fun emitQuery(newQuery: String) {
        allQueries.tryEmit(newQuery)
    }

    fun clear() {
        scope.cancel()
    }

    fun interface Listener {
        fun onNewQuery(query: String)
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }

}
