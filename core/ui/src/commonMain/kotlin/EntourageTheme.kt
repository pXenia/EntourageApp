package com.entourageapp.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun EntourageTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = getTypography(),
        content = content
    )
}