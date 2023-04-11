@file:OptIn(FlowPreview::class)

package com.study.tinkoff.elm


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import vivid.money.elmslie.coroutines.Actor

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainActor(dispatcher: CoroutineDispatcher) :
    Actor<MainCommand, MainEvent.Internal> {

    private val searchEvent: MutableStateFlow<MainEvent.Internal> =
        MutableStateFlow(MainEvent.Internal.SearchSuccess(""))
    private val searchQueries: MutableSharedFlow<String> = MutableSharedFlow()

    init {
        searchQueries
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .debounce(SEARCH_DEBOUNCE)
            .flatMapLatest { query ->
                flow {
                    emit(MainEvent.Internal.SearchSuccess(query))
                }
            }
            .onEach { success ->
                searchEvent.emit(success)
            }
            .flowOn(dispatcher)
    }

    override fun execute(command: MainCommand): Flow<MainEvent.Internal> = when (command) {
        is MainCommand.Search -> {
            searchQueries.tryEmit(command.query)
            searchEvent
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }

}
