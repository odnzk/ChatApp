package com.study.search.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.color.MaterialColors
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.study.components.extensions.dp
import com.study.search.R
import java.lang.Integer.max

class SimpleSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val tilInputBox: TextInputLayout
    private val etInputField: TextInputEditText
    private val btnCancel: ImageView
    private val defaultPaddings = 16.dp(context)
    private val colorViewNotFocused = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.backgroundColor,
        context.getColor(com.study.ui.R.color.item_background_color)
    )
    private val colorViewFocused = MaterialColors.getColor(
        context,
        androidx.appcompat.R.attr.colorPrimary,
        context.getColor(com.study.ui.R.color.navy_light)
    )
    private val drawableViewNotFocused = R.drawable.baseline_search_24
    private val viewTextColor = MaterialColors.getColor(
        context, com.google.android.material.R.attr.colorOnBackground, Color.WHITE
    )
    var searchHint: String = context.getString(R.string.default_search_hint)
        set(value) {
            field = value
            etInputField.hint = field
        }
    var afterChangeTextListener: (text: Editable?) -> Unit = {}
        set(value) {
            field = value
            etInputField.addTextChangedListener { text ->
                setState(if (text?.isNotEmpty() == true) State.SEARCHING else State.PAUSED)
                field(text)
            }
        }

    init {
        inflate(context, R.layout.view_search, this)
        tilInputBox = findViewById(R.id.view_search_til)
        etInputField = findViewById(R.id.view_search_et)
        btnCancel = findViewById(R.id.view_search_iv_cancel)

        with(etInputField) {
            hint = searchHint
            etInputField.maxLines = 1
            gravity = Gravity.CENTER_VERTICAL
            setTextAppearance(com.study.ui.R.style.TextAppearance_Messenger_Title)
            TextViewCompat.setCompoundDrawableTintList(this, ColorStateList.valueOf(viewTextColor))
            setTextColor(viewTextColor)
            setHintTextColor(viewTextColor)
            if (paddingBottom == 0 && paddingTop == 0 && paddingLeft == 0 && paddingRight == 0) {
                setPadding(defaultPaddings)
            }
            if (compoundDrawablePadding == 0) {
                compoundDrawablePadding = defaultPaddings
            }
        }
        setState(State.PAUSED)
        btnCancel.setOnClickListener { etInputField.setText("") }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(btnCancel, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val usedWidth = btnCancel.measuredWidth + btnCancel.marginLeft + btnCancel.marginRight
        measureChildWithMargins(tilInputBox, widthMeasureSpec, usedWidth, heightMeasureSpec, 0)
        val totalWidth =
            (paddingLeft + paddingRight + tilInputBox.marginLeft + tilInputBox.marginRight + tilInputBox.measuredWidth + btnCancel.marginLeft + btnCancel.marginRight + btnCancel.measuredWidth)
        val totalHeight = paddingTop + paddingBottom + max(
            tilInputBox.marginTop + tilInputBox.marginBottom + tilInputBox.measuredHeight,
            btnCancel.marginTop + btnCancel.marginBottom + btnCancel.measuredHeight
        )
        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec)
        )
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft + btnCancel.marginLeft
        btnCancel.layout(
            offsetX,
            paddingTop + btnCancel.marginTop,
            offsetX + btnCancel.measuredWidth,
            paddingTop + btnCancel.marginTop + btnCancel.measuredHeight
        )
        offsetX += btnCancel.measuredWidth + btnCancel.marginRight
        tilInputBox.layout(
            offsetX + tilInputBox.marginLeft,
            paddingTop + tilInputBox.marginTop,
            offsetX + tilInputBox.measuredWidth + tilInputBox.marginLeft,
            paddingTop + tilInputBox.marginTop + tilInputBox.measuredHeight
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


    private enum class State {
        SEARCHING, PAUSED
    }

    private fun setState(state: State) {
        when (state) {
            State.SEARCHING -> {
                etInputField.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                setBackgroundColor(colorViewFocused)
                btnCancel.isVisible = true
            }
            State.PAUSED -> {
                etInputField.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, drawableViewNotFocused, 0
                )
                setBackgroundColor(colorViewNotFocused)
                btnCancel.isVisible = false
            }
        }
    }
}
