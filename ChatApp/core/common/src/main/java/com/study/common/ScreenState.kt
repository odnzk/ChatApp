package com.study.common

sealed interface ScreenState<out T> {
    object Loading : ScreenState<Nothing>
    class Success<T>(val data: T) : ScreenState<T>
    class Error(val error: Throwable) : ScreenState<Nothing>
}

suspend inline fun <T, R> ScreenState<T>.map(crossinline transform: suspend (value: T) -> R): ScreenState<R> =
    when (this) {
        is ScreenState.Error -> ScreenState.Error(this.error)
        ScreenState.Loading -> ScreenState.Loading
        is ScreenState.Success -> ScreenState.Success(transform(this.data))
    }
