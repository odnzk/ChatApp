package com.study.components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.study.components.extensions.collectFlowSafely
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment<FragmentViewModel : ViewModel, Binding : ViewBinding> : Fragment() {
    protected abstract val viewModel: FragmentViewModel
    protected abstract val binding: Binding

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupListeners()
        observeState()
    }

    protected abstract fun observeState()

    protected open fun setupListeners() = Unit

    protected open fun initUI() = Unit

    protected inline fun <T : Any> Flow<T>.collectFlowSafely(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline onCollect: suspend (T) -> Unit
    ) {
        this@BaseFragment.collectFlowSafely(this, lifecycleState) {
            onCollect(it)
        }
    }
}
