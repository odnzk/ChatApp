package com.odnzk.ui_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.study.ui.R

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.primary_button_corner_radius)),
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.primary_button_vertical_padding)),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            style = ChatAppTypography.button,
            text = text.uppercase(),
            modifier = Modifier,
            color = Color.White
        )
    }
}

@Composable
@Preview
fun preview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        PrimaryTextInputLayout(
            label = "label",
            hint = "hint",
            value = "value",
            onValueChanged = {})
        PrimaryButton(text = "text", onClick = {})
    }
}


@Composable
fun PrimaryTextInputLayout(
    label: String,
    hint: String,
    value: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = value,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.text_input_layout_corner_radius)),
        onValueChange = { onValueChanged(it) },
        label = { Text(text = label, color = colorResource(id = R.color.purple_light)) },
        placeholder = { Text(text = hint) },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )

    )
}