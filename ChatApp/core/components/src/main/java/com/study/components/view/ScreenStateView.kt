package com.study.components.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.widget.ContentLoadingProgressBar
import com.study.components.R
import com.study.components.ScreenState
import java.lang.Integer.max

class ScreenStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val pbLoading: ContentLoadingProgressBar
    private val tvError: TextView
    private val btnTryAgain: Button

    private var widthMiddle: Int = 0

    init {
        inflate(context, R.layout.view_screen_state, this)
        pbLoading = findViewById(R.id.view_screen_state_pb_loading)
        tvError = findViewById(R.id.view_screen_state_tv_error)
        btnTryAgain = findViewById(R.id.view_screen_state_btn_try_again)
    }

    fun setState(state: ScreenState<*>) {
        when (state) {
            is ScreenState.Error -> {
                isVisible = true
                pbLoading.hide()
                tvError.text = state.error.message.orEmpty()
                tvError.isVisible = true
                btnTryAgain.isVisible = true
            }
            ScreenState.Loading -> {
                isVisible = true
                pbLoading.show()
                tvError.isVisible = false
                btnTryAgain.isVisible = false
            }
            is ScreenState.Success -> isVisible = false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(
            pbLoading, widthMeasureSpec, 0, heightMeasureSpec, 0
        )
        val usedHeight = pbLoading.measuredHeight + pbLoading.marginTop + pbLoading.marginBottom
        measureChildWithMargins(tvError, widthMeasureSpec, 0, heightMeasureSpec, usedHeight)
        measureChildWithMargins(
            btnTryAgain, widthMeasureSpec, 0, heightMeasureSpec, usedHeight
        )

        val totalWidth = paddingLeft + paddingRight + max(
            pbLoading.measuredWidth + pbLoading.marginStart + pbLoading.marginEnd, max(
                tvError.measuredWidth + tvError.marginEnd + tvError.marginStart,
                btnTryAgain.measuredWidth + btnTryAgain.marginStart + btnTryAgain.marginEnd
            )
        )
        val totalHeight =
            paddingTop + paddingBottom + pbLoading.measuredHeight + pbLoading.marginTop + pbLoading.marginBottom + tvError.measuredHeight + tvError.marginTop + tvError.marginBottom + btnTryAgain.measuredHeight + btnTryAgain.marginTop + btnTryAgain.marginBottom

        widthMiddle = totalWidth / 2
        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val offsetX = paddingLeft
        var offsetY = paddingTop
        pbLoading.layout(
            offsetX + widthMiddle - pbLoading.measuredWidth / 2 + pbLoading.marginStart,
            offsetY + pbLoading.marginTop,
            offsetX + widthMiddle + pbLoading.measuredWidth / 2 - pbLoading.marginEnd,
            offsetY + pbLoading.measuredHeight + pbLoading.marginTop
        )
        offsetY += pbLoading.measuredHeight + pbLoading.marginTop + pbLoading.marginBottom
        tvError.layout(
            offsetX + widthMiddle - tvError.measuredWidth / 2 + tvError.marginStart,
            offsetY + tvError.marginTop,
            offsetX + widthMiddle + tvError.measuredWidth / 2 - tvError.marginEnd,
            offsetY + tvError.measuredHeight + tvError.marginTop
        )
        offsetY += tvError.measuredHeight + tvError.marginTop + tvError.marginBottom
        btnTryAgain.layout(
            offsetX + widthMiddle - btnTryAgain.measuredWidth / 2 + btnTryAgain.marginStart,
            offsetY + btnTryAgain.marginTop,
            offsetX + widthMiddle + btnTryAgain.measuredWidth / 2 - btnTryAgain.marginEnd,
            offsetY + btnTryAgain.marginTop + btnTryAgain.measuredHeight
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

}
