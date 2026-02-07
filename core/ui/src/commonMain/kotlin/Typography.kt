package com.entourageapp.core.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun getTypography(): Typography {
    val titles = ralewayFontFamily()
    val body = latoFontFamily()

    return Typography(
        // Заголовки (Raleway)
        titleLarge = TextStyle(
            fontFamily = titles,
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp
        ),
        titleMedium = TextStyle(
            fontFamily = titles,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        displayLarge = TextStyle(
            fontFamily = titles,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 38.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),

        // Основа (Lato)
        bodyMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        ),
    )
}