package com.study.tinkoff.core.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    protected abstract fun setupListeners()

    protected abstract fun initUI()

    protected inline fun <T> Flow<T>.collectFlowSafely(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline onCollect: suspend (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                this@collectFlowSafely.collectLatest { onCollect(it) }
            }
        }
    }

}
