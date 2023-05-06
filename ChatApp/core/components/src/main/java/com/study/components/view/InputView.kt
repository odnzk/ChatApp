package com.study.components.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.study.components.R
import com.study.components.extension.dp
import com.study.components.extension.hideKeyboard
import com.google.android.material.R as MaterialR
import com.study.ui.R as CoreR

class InputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val tilInputBox: TextInputLayout
    private val etInputField: TextInputEditText
    private val btnSubmitData: ShapeableImageView

    private val colorBtnSubmitActive = MaterialColors.getColor(
        context,
        androidx.appcompat.R.attr.colorPrimary,
        context.getColor(CoreR.color.navy_light)
    )

    @ColorInt
    private var colorViewBackground: Int = MaterialColors.getColor(
        context, MaterialR.attr.backgroundColor, context.getColor(CoreR.color.dark_nero)
    )
    private val tintIconAddContent = context.getColor(CoreR.color.charcoal)
    private val tintIconSendMessage = Color.BLACK
    private val textColor = context.getColor(CoreR.color.white)
    private val iconSendMessagePadding = 8.dp(context)

    private var editTextHint = context.getString(R.string.hint_send_message)
    private var isInputSingleLine = false
    private var addContentStateEnabled = false
    private var inputMaxLength = DEFAULT_INPUT_LENGTH

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

        context.withStyledAttributes(attrs, R.styleable.InputView) {
            editTextHint = getString(R.styleable.InputView_hint)
                ?: context.getString(R.string.hint_send_message)
            isInputSingleLine = getBoolean(R.styleable.InputView_singleLine, isInputSingleLine)
            addContentStateEnabled =
                getBoolean(R.styleable.InputView_enableAddContentState, addContentStateEnabled)
            inputMaxLength = getInteger(R.styleable.InputView_maxLength, inputMaxLength)
            colorViewBackground =
                getColor(R.styleable.InputView_viewBackgroundColor, colorViewBackground)
        }

        initInitialState()
    }

    private enum class State {
        SEND_MESSAGE, ADD_CONTENT
    }

    private fun setState(state: State) {
        when (state) {
            State.ADD_CONTENT ->
                btnSubmitData.run {
                    setPadding(0)
                    setBackgroundColor(colorViewBackground)
                    imageTintList = ColorStateList.valueOf(tintIconAddContent)
                    setImageResource(CoreR.drawable.ic_add_content)
                }
            else ->
                btnSubmitData.run {
                    if (addContentStateEnabled) {
                        setPadding(iconSendMessagePadding)
                    } else {
                        setPadding(0)
                        setContentPadding(
                            SEND_STATE_PADDING,
                            SEND_STATE_PADDING,
                            SEND_STATE_PADDING,
                            SEND_STATE_PADDING
                        )
                    }
                    setBackgroundColor(colorBtnSubmitActive)
                    imageTintList = ColorStateList.valueOf(tintIconSendMessage)
                    setImageResource(CoreR.drawable.ic_baseline_send_24)
                }
        }
    }

    private fun initInitialState() {
        setState(if (addContentStateEnabled) State.ADD_CONTENT else State.SEND_MESSAGE)
        setBackgroundColor(colorViewBackground)

        with(etInputField) {
            isSingleLine = isInputSingleLine
            filters += arrayOf(InputFilter.LengthFilter(inputMaxLength))
            setBackgroundColor(colorViewBackground)
            setTextColor(textColor)
            addTextChangedListener { textInput ->
                if (addContentStateEnabled) {
                    setState(if (textInput.isNullOrBlank()) State.ADD_CONTENT else State.SEND_MESSAGE)
                }
            }
        }
        tilInputBox.hint = editTextHint
    }

    companion object {
        private const val DEFAULT_INPUT_LENGTH = 300
        private const val SEND_STATE_PADDING = 12
    }

}
