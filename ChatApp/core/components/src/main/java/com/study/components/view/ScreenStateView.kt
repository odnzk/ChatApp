package com.study.components.view

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import com.study.components.R
import com.study.components.model.UiError

class ScreenStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val pbLoading: ContentLoadingProgressBar
    private val ivError: ImageView
    private val tvError: TextView
    private val tvErrorDescription: TextView
    private val btnTryAgain: Button
    var onTryAgainClickListener: OnClickListener = OnClickListener { }
        set(value) {
            field = value
            btnTryAgain.setOnClickListener(field)
        }

    init {
        inflate(context, R.layout.view_screen_state, this)
        pbLoading = findViewById(R.id.view_screen_state_pb_loading)
        ivError = findViewById(R.id.view_screen_state_iv_error)
        tvError = findViewById(R.id.view_screen_state_tv_error)
        tvErrorDescription = findViewById(R.id.view_screen_state_tv_error_description)
        btnTryAgain = findViewById(R.id.view_screen_state_btn_try_again)
        orientation = VERTICAL
    }

    sealed interface ViewState {
        object Loading : ViewState
        class Error(val error: UiError, val isBtnTryAgainVisible: Boolean = true) : ViewState
        object Success : ViewState
    }

    fun setState(state: ViewState) {
        when (state) {
            is ViewState.Error -> setError(state.error, state.isBtnTryAgainVisible)
            ViewState.Loading -> {
                setChildrenVisibility(false)
                pbLoading.isVisible = true
            }
            ViewState.Success -> setChildrenVisibility(false)
        }
    }

    private fun setError(error: UiError, isBtnTryAgainVisible: Boolean = false) {
        setChildrenVisibility(true)
        tvError.text = getString(error.messageRes)
        when {
            error.descriptionRes != null && error.descriptionArgs != null -> {
                tvErrorDescription.text =
                    context.getString(error.descriptionRes, error.descriptionArgs)
            }
            error.descriptionRes != null -> tvErrorDescription.text =
                getString(error.descriptionRes)
            else -> tvErrorDescription.isVisible = false
        }
        error.imageRes?.let { ivError.setImageResource(it) } ?: run { ivError.isVisible = false }
        btnTryAgain.isVisible = isBtnTryAgainVisible
        pbLoading.isVisible = false
    }

    private fun setChildrenVisibility(isChildVisible: Boolean) {
        children.forEach { child -> child.isVisible = isChildVisible }
    }

    private fun getString(@StringRes res: Int): String = context.getString(res)
}
