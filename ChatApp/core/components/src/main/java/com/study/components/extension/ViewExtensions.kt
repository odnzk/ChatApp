package com.study.components.extension

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.study.components.recycler.delegates.Delegate
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

inline fun <reified T> Bundle.safeGetParcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }


fun delegatesToList(vararg delegates: Delegate<*, *>): List<Delegate<RecyclerView.ViewHolder, Any>> {
    return delegates.toList() as? List<Delegate<RecyclerView.ViewHolder, Any>>
        ?: error("Cannot create valid list from this delegates")
}

fun ViewBinding.showSnackbar(@StringRes messageRes: Int) =
    Snackbar.make(root, messageRes, Snackbar.LENGTH_SHORT).show()

fun Fragment.showToast(@StringRes messageRes: Int) =
    Toast.makeText(requireContext(), messageRes, Toast.LENGTH_SHORT).show()

fun measureFullHeight(view: View): Int = view.measuredHeight + view.marginTop + view.marginBottom
fun measureFullWidth(view: View): Int = view.measuredWidth + view.marginRight + view.marginLeft
fun measureHeightIfVisible(view: View) = if (view.isVisible) measureFullHeight(view) else 0
fun measureWidthIfVisible(view: View) = if (view.isVisible) measureFullWidth(view) else 0
