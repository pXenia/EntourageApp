package com.entourageapp.features.gallery.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberImagePickerLauncher(
    onResult: (bytes: ByteArray, fileName: String, mimeType: String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val bytes = context.contentResolver
            .openInputStream(uri)
            ?.readBytes()
            ?: return@rememberLauncherForActivityResult
        val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        onResult(bytes, fileName, mime)
    }
    return { launcher.launch("image/*") }
}