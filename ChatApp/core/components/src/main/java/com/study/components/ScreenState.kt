package com.study.components

sealed interface ScreenState<out T> {
    object Loading : ScreenState<Nothing>
    class Success<T>(val data: T) : ScreenState<T>
    class Error(val error: Throwable) : ScreenState<Nothing>
}

inline fun <T, R> ScreenState<T>.map(crossinline transform: (value: T) -> R): ScreenState<R> =
    when (this) {
        is ScreenState.Error -> ScreenState.Error(this.error)
        ScreenState.Loading -> ScreenState.Loading
        is ScreenState.Success -> ScreenState.Success(transform(this.data))
    }
