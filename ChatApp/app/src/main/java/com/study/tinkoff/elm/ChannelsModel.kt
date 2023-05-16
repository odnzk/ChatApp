package com.study.tinkoff.elm

class MainState

sealed interface MainEffect

sealed interface MainCommand {
    class Search(val query: String) : MainCommand
}

sealed interface MainEvent {
    sealed interface Ui : MainEvent {
        object Init : Ui
        class Search(val query: String) : Ui
    }
}
