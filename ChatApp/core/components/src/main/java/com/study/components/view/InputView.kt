package com.study.components.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.study.components.R
import com.study.components.extensions.dp
import com.study.components.extensions.hideKeyboard
import java.lang.Integer.max
import com.study.ui.R as CoreResources

class InputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val tilInputBox: TextInputLayout
    private val etInputField: TextInputEditText
    private val btnSubmitData: ShapeableImageView
    private val colorBackgroundSendMessage = MaterialColors.getColor(
        context,
        androidx.appcompat.R.attr.colorPrimary,
        context.getColor(CoreResources.color.navy_light)
    )
    private val iconSendMessagePadding = 12.dp(context)
    private val colorBackgroundAddContent =
        context.getColor(CoreResources.color.darkest_nero)
    private val tintIconAddContent = context.getColor(CoreResources.color.charcoal)
    private val tintIconSendMessage = Color.BLACK

    var btnSubmitClickListener: (inputText: String) -> Unit = {}
        set(value) {
            field = value
            btnSubmitData.setOnClickListener {
                field.invoke(etInputField.text.toString())
                etInputField.setText("")
                focusedChild?.hideKeyboard()
            }
        }

    init {
        inflate(context, R.layout.view_input, this)
        tilInputBox = findViewById(R.id.view_input_til_message)
        etInputField = findViewById(R.id.view_input_et_message)
        btnSubmitData = findViewById(R.id.view_input_btn_send_message)

        setState(State.ADD_CONTENT)
        setBackgroundColor(colorBackgroundAddContent)

        etInputField.addTextChangedListener { textInput ->
            setState(if (textInput.isNullOrBlank()) State.ADD_CONTENT else State.SEND_MESSAGE)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(btnSubmitData, widthMeasureSpec, 0, heightMeasureSpec, 0)
        val usedWidth =
            btnSubmitData.measuredWidth + btnSubmitData.marginLeft + btnSubmitData.marginRight
        measureChildWithMargins(tilInputBox, widthMeasureSpec, usedWidth, heightMeasureSpec, 0)
        val totalWidth =
            (paddingLeft + paddingRight + tilInputBox.marginLeft + tilInputBox.marginRight + tilInputBox.measuredWidth + btnSubmitData.marginLeft + btnSubmitData.marginRight + btnSubmitData.measuredWidth)
        val totalHeight = paddingTop + paddingBottom + max(
            tilInputBox.marginTop + tilInputBox.marginBottom + tilInputBox.measuredHeight,
            btnSubmitData.marginTop + btnSubmitData.marginBottom + btnSubmitData.measuredHeight
        )
        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec)
        )
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val offsetX = width - paddingRight - btnSubmitData.marginRight - btnSubmitData.measuredWidth
        btnSubmitData.layout(
            offsetX,
            paddingTop + btnSubmitData.marginTop,
            width - paddingRight - btnSubmitData.marginRight,
            paddingTop + btnSubmitData.marginTop + btnSubmitData.measuredHeight
        )
        tilInputBox.layout(
            paddingLeft + tilInputBox.marginLeft,
            paddingTop + tilInputBox.marginTop,
            offsetX - btnSubmitData.marginLeft - tilInputBox.marginRight,
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
        SEND_MESSAGE, ADD_CONTENT
    }

    private fun setState(state: State) {
        when (state) {
            State.SEND_MESSAGE -> {
                btnSubmitData.run {
                    setBackgroundColor(colorBackgroundSendMessage)
                    setPadding(iconSendMessagePadding)
                    imageTintList = ColorStateList.valueOf(tintIconSendMessage)
                    setImageResource(R.drawable.ic_baseline_send_24)
                }
            }
            State.ADD_CONTENT -> {
                btnSubmitData.run {
                    setPadding(0)
                    setBackgroundColor(colorBackgroundAddContent)
                    imageTintList = ColorStateList.valueOf(tintIconAddContent)
                    setImageResource(R.drawable.ic_add_content)
                }
            }

        }
    }
}
