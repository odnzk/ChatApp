package com.study.common.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun CoroutineScope.safeLaunch(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    crossinline onError: suspend (Throwable) -> Unit,
    crossinline action: suspend () -> Unit
): Job {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        launch(Dispatchers.Main) { onError(throwable) }
    }
    return launch(exceptionHandler + dispatcher) { action() }
}


