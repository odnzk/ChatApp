package com.study.tinkoff.core.ui

sealed interface ScreenState<out T> {
    object Loading : ScreenState<Nothing>
    class Success<T>(val data: T) : ScreenState<T>
    class Error(val error: Throwable) : ScreenState<Nothing>
}
