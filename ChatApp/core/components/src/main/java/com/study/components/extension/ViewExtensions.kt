package com.study.components.extension

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.study.components.recycler.delegates.Delegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.study.ui.R as CoreR

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

fun ImageView.loadFromUrl(url: String) {
    load(url) {
        memoryCachePolicy(CachePolicy.ENABLED)
        crossfade(true)
        error(CoreR.color.darkest_nero)
        placeholder(CoreR.color.darkest_nero)
    }
}

inline fun <reified T> Bundle.safeGetParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }
}

fun delegatesToList(vararg delegates: Delegate<*, *>): List<Delegate<RecyclerView.ViewHolder, Any>> {
    return delegates.toList() as? List<Delegate<RecyclerView.ViewHolder, Any>>
        ?: error("Cannot create correct list from this delegates")
}
