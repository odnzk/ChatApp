package com.study.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.common.ScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

abstract class BaseViewModel<T> : ViewModel() {
    protected abstract val _state: MutableStateFlow<ScreenState<T>>

    protected val baseExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope
            _state.value = ScreenState.Error(throwable)
        }

    protected fun <T> Flow<T>.handleErrors(): Flow<T> = catch { e ->
        if (e is CancellationException) throw e
        _state.value = ScreenState.Error(e)
    }

    protected inline fun ViewModel.safeLaunch(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        crossinline action: suspend () -> Unit
    ): Job = viewModelScope.launch(baseExceptionHandler + dispatcher) { action() }

}
