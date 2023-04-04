package com.study.components

import android.view.View.OnClickListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.study.common.ScreenState
import com.study.components.view.ScreenStateView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseScreenStateFragment<FragmentViewModel : ViewModel, Binding : ViewBinding, Data : Any> :
    BaseFragment<FragmentViewModel, Binding>() {
    protected var screenStateView: ScreenStateView? = null
    protected abstract val onTryAgainClick: OnClickListener?
    protected open fun onError(error: Throwable) {
        screenStateView?.setState(ScreenState.Error(error))
        Timber.d(error)
    }

    abstract fun onSuccess(data: Data)
    protected open fun onLoading() {
        screenStateView?.setState(ScreenState.Loading)
    }

    protected fun Flow<ScreenState<Data>>.collectScreenStateSafely(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED
    ) {
        onTryAgainClick?.let { screenStateView?.onTryAgainClickListener = it }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                this@collectScreenStateSafely.collectLatest { state ->
                    when (state) {
                        is ScreenState.Error -> onError(state.error)
                        ScreenState.Loading -> onLoading()
                        is ScreenState.Success -> {
                            screenStateView?.setState(state)
                            onSuccess(state.data)
                        }
                    }
                }
            }
        }
    }
}
