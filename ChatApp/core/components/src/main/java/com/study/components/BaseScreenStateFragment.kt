package com.study.components

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.study.components.view.ScreenStateView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseScreenStateFragment<FragmentViewModel : ViewModel, Binding : ViewBinding, Data : Any> :
    BaseFragment<FragmentViewModel, Binding>() {
    protected abstract val screenStateView: ScreenStateView?
    abstract fun onError(error: Throwable)
    abstract fun onSuccess(data: Data)
    abstract fun onLoading()

    protected fun Flow<ScreenState<Data>>.collectScreenStateSafely(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                this@collectScreenStateSafely.collectLatest { state ->
                    screenStateView?.setState(state)
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
