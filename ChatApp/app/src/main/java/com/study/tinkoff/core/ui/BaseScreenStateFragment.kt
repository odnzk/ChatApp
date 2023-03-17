package com.study.tinkoff.core.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseScreenStateFragment<FragmentViewModel : ViewModel, Binding : ViewBinding, Data : Any> :
    BaseFragment<FragmentViewModel, Binding>() {

    abstract fun onError(error: Throwable)
    abstract fun onSuccess(data: Data)
    abstract fun onLoading()

    protected fun Flow<ScreenState<Data>>.collectStateFlowSafely(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                this@collectStateFlowSafely.collectLatest { state ->
                    when (state) {
                        is ScreenState.Error -> onError(state.error)
                        ScreenState.Loading -> onLoading()
                        is ScreenState.Success -> onSuccess(state.data)
                    }
                }
            }
        }
    }
}
