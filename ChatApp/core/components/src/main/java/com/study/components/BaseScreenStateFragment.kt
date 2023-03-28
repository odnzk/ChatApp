package com.study.components

import android.view.View.OnClickListener
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
    protected abstract val onTryAgainClick: OnClickListener?
    protected open fun onError(error: Throwable) = Unit
    abstract fun onSuccess(data: Data)
    protected open fun onLoading() = Unit

    protected fun Flow<ScreenState<Data>>.collectScreenStateSafely(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
    ) {
        onTryAgainClick?.let { screenStateView?.onTryAgainClickListener = it }
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
