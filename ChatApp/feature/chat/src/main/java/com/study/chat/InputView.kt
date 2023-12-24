package com.study.chat

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.study.components.ext.hideKeyboard
import com.study.ui.R as CoreR

internal class InputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val tilInputBox: TextInputLayout
    private val etInputField: TextInputEditText
    private val btnSendMessage: FloatingActionButton
    private val btnUpload: FloatingActionButton

    private val inputTextColor = context.getColor(CoreR.color.white)
    private var inputHint: String? = null
    private var isInputSingleLine = false
    private var inputMaxLength = DEFAULT_INPUT_LENGTH

    var btnUploadContentListener: () -> Unit = {}
        set(value) {
            field = value
            btnUpload.setOnClickListener { field.invoke() }
        }

    var btnSendMessageClickListener: (inputText: String) -> Unit = {}
        set(value) {
            field = value
            btnSendMessage.setOnClickListener {
                field.invoke(etInputField.text.toString())
                etInputField.setText("")
                focusedChild?.hideKeyboard()
            }
        }

    init {
        inflate(context, R.layout.view_input, this)
        tilInputBox = findViewById(R.id.view_input_til_message)
        etInputField = findViewById(R.id.view_input_et_message)
        btnSendMessage = findViewById(R.id.view_input_btn_send_message)
        btnUpload = findViewById(R.id.view_input_btn_upload)

        setupStyledAttributes(context, attrs)
        setupInitialState()
    }

    private fun setupStyledAttributes(context: Context, attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.InputView) {
            inputHint = getString(R.styleable.InputView_hint)
            isInputSingleLine = getBoolean(R.styleable.InputView_singleLine, isInputSingleLine)
            inputMaxLength = getInteger(R.styleable.InputView_maxLength, inputMaxLength)
        }
    }

    private enum class State {
        SEND_MESSAGE, UPLOAD_CONTENT
    }

    private fun setState(state: State) {
        when(state){
            State.SEND_MESSAGE -> {
                btnUpload.isVisible = false
                btnSendMessage.isVisible = true
            }
            State.UPLOAD_CONTENT -> {
                btnUpload.isVisible = true
                btnSendMessage.isVisible = false
            }
        }
    }

    private fun setupInitialState() {
        setState(State.UPLOAD_CONTENT)
        with(etInputField) {
            isSingleLine = isInputSingleLine
            filters += arrayOf(InputFilter.LengthFilter(inputMaxLength))
            setTextColor(inputTextColor)
            addTextChangedListener { textInput ->
                setState(if (textInput.isNullOrBlank()) State.UPLOAD_CONTENT else State.SEND_MESSAGE)
            }
        }
        tilInputBox.hint = inputHint
    }

    companion object {
        private const val DEFAULT_INPUT_LENGTH = 300
    }

}
