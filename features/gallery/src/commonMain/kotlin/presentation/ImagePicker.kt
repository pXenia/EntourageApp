package com.entourageapp.features.gallery.presentation

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(
    onResult: (bytes: ByteArray, fileName: String, mimeType: String) -> Unit
): () -> Unit