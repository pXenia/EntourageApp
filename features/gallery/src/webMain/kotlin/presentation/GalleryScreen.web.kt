package com.entourageapp.features.gallery.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun GalleryScreen(
    projectId: Int,
    roomId: Int,
    modifier: Modifier,
    onBackClick: () -> Unit,
    viewModel: GalleryVM
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Gallery is not supported on Web yet")
    }
}
