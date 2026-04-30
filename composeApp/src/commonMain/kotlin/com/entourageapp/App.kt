package com.entourageapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.entourageapp.core.ui.EntourageTheme
import com.entourageapp.di.initKoin
import com.entourageapp.presentation.NavRoot

@Composable
@Preview
fun App() {
    EntourageTheme {
        NavRoot()
    }
    initKoin()
}