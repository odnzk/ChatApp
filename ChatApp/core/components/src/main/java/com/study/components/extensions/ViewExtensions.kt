package com.study.components.extensions

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun View.hideKeyboard() {
    context?.getSystemService(InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

inline fun <T : Any> Fragment.collectFlowSafely(
    flow: Flow<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline onCollect: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
            flow.collectLatest { onCollect(it) }
        }
    }
}
