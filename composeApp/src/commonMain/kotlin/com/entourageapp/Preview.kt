package com.entourageapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.entourageapp.core.ui.EntourageTheme
import com.entourageapp.features.projects.presentation.ProjectListScreen

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    EntourageTheme {
        ProjectListScreen()
    }
}