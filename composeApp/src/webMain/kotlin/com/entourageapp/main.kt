package com.entourageapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.entourageapp.core.ui.EntourageTheme
import com.entourageapp.di.initKoin
import com.entourageapp.presentation.NavRoot

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport {
        EntourageTheme {
            NavRoot()
        }
    }
}