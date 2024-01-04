package com.odnzk.auth.presentation.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.odnzk.auth.R
import com.odnzk.ui_compose.PrimaryTextInputLayout

@Composable
internal fun PasswordTextField(
    label: String,
    hint: String,
    password: String,
    onValueChanged: (String) -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val passwordIcon =
        if (passwordVisibility) R.drawable.visibility_on else R.drawable.visibility_off
    PrimaryTextInputLayout(
        label = label,
        hint = hint,
        value = password,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(
                    painterResource(id = passwordIcon),
                    contentDescription =
                    stringResource(
                        id = R.string.common_icon_password_visibility_content_description
                    )
                )
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    ) {
        onValueChanged(it)
    }
}