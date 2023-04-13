package com.study.tinkoff.elm

internal data class MainState(
    val searchQuery: String = ""
)

internal sealed interface MainEffect

internal sealed interface MainCommand {
    class Search(val query: String) : MainCommand
}

internal sealed interface MainEvent {
    sealed interface Ui : MainEvent {
        object Init : Ui
        class Search(val query: String) : Ui
    }

    sealed interface Internal : MainEvent {
        class SearchSuccess(val query: String) : Internal
    }
}
