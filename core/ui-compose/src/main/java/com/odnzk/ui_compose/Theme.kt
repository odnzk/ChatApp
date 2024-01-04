package com.odnzk.ui_compose

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ChatAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ChatAppColors,
        typography = ChatAppTypography,
        content = content
    )
}

@SuppressLint("ConflictingOnColor")
val ChatAppColors = lightColors(
    primary = Color(0xFF5057F4),
    primaryVariant = Color.White,
    onPrimary = Color.Black,
    secondary = Color(0xFF745EA7),
    secondaryVariant = Color.Gray,
    onSecondary = Color.Cyan,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFF87A91),
    onError = Color.White
)

