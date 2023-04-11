package com.study.tinkoff.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

internal class MainReducer :
    DslReducer<MainEvent, MainState, MainEffect, MainCommand>() {

    override fun Result.reduce(event: MainEvent) = when (event) {
        is MainEvent.Ui.Init -> Unit
        is MainEvent.Ui.Search -> {
            commands { +MainCommand.Search(event.query) }
        }
        is MainEvent.Internal.SearchSuccess -> {
            state { copy(searchQuery = event.query) }
        }
    }
}
