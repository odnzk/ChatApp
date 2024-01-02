package com.odnzk.ui_compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.study.ui.R

@Composable
fun PrimaryButton(content: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.primary_button_corner_radius)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.primary_button_vertical_padding))
    ) {
        Text(
            style = ChatAppTypography.button,
            text = content.uppercase(),
            modifier = Modifier
        )
    }
}

@Composable
fun PrimaryTextInputLayout(
    label: String,
    hint: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.text_input_layout_corner_radius)),
        onValueChange = { onValueChanged(it) },
        label = { Text(text = label, color = colorResource(id = R.color.purple_light)) },
        placeholder = { Text(text = hint, color = colorResource(id = R.color.purple_light)) },
        modifier = Modifier.fillMaxWidth()
    )
}