package com.odnzk.ui_compose

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.study.ui.R

val ChatAppTypography = Typography(
    h1 = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
        fontFamily = FontFamily(Font(R.font.satoshi_bold))
    ),
    h2 = TextStyle(
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 18.sp,
        fontFamily = FontFamily(Font(R.font.satoshi_bold))
    ),
    h3 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 26.sp,
        fontFamily = FontFamily(Font(R.font.satoshi_bold))
    ),
    h4 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    h5 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    h6 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 17.sp
    ),
    defaultFontFamily = FontFamily(Font(R.font.satoshi_regular)),
    body1 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 17.sp
    ),
    button = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )
)
