@file:OptIn(FlowPreview::class)

package com.study.tinkoff.elm


import com.study.tinkoff.di.MutableSearchFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MainActor @Inject constructor(
    dispatcher: CoroutineDispatcher,
    @MutableSearchFlow
    private val searchFlow: MutableSharedFlow<String>
) :
    Actor<MainCommand, MainEvent.Internal> {
    private val actorScope = CoroutineScope(dispatcher + SupervisorJob())

    private val searchEvent: MutableStateFlow<MainEvent.Internal> =
        MutableStateFlow(MainEvent.Internal.SearchSuccess(""))
    private val searchQueries: MutableSharedFlow<String> =
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    init {
        actorScope.launch {
            searchQueries
                .filter { it.isEmpty() || it.isNotBlank() }
                .distinctUntilChanged()
                .debounce(SEARCH_DEBOUNCE)
                .flatMapLatest { query ->
                    flow {
                        searchFlow.emit(query)
                        emit(MainEvent.Internal.SearchSuccess(query))
                    }
                }
                .onEach { successEvent ->
                    searchEvent.emit(successEvent)
                }
                .flowOn(dispatcher)
                .collect()
        }
    }

    fun clear() {
        actorScope.coroutineContext.cancelChildren()
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
