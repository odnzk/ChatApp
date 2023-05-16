package com.study.tinkoff.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import javax.inject.Inject

class MainReducer @Inject constructor() :
    DslReducer<MainEvent, MainState, MainEffect, MainCommand>() {

    override fun Result.reduce(event: MainEvent) = when (event) {
        is MainEvent.Ui.Init -> Unit
        is MainEvent.Ui.Search -> {
            commands { +MainCommand.Search(event.query) }
        }
    }
}
