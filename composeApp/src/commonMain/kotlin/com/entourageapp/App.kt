package com.entourageapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.entourageapp.core.ui.EntourageTheme

@Composable
@Preview
fun App() {
    EntourageTheme {
        NavRoot()
    }
    initKoin()
}