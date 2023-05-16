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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MainActor @Inject constructor(
    dispatcher: CoroutineDispatcher,
    @MutableSearchFlow
    private val searchFlow: MutableSharedFlow<String>
) :
    Actor<MainCommand, MainEvent> {
    private val actorScope = CoroutineScope(dispatcher + SupervisorJob())
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
                    flow<Unit> {
                        searchFlow.emit(query)
                    }
                }
                .flowOn(dispatcher)
                .collect()
        }
    }

    fun clear() {
        actorScope.coroutineContext.cancelChildren()
    }

    override fun execute(command: MainCommand): Flow<MainEvent> = when (command) {
        is MainCommand.Search -> flow { searchQueries.tryEmit(command.query) }
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 500L
    }

}
