package com.entourageapp.features.gallery.presentation

import com.entourageapp.core.network.dto.ImageDto

data class GalleryState(
    val isLoading: Boolean = false,
    val images: List<ImageDto> = emptyList(),
    val isUploading: Boolean = false,
    val error: String? = null
)

sealed class GalleryIntent {
    data class LoadImages(val projectId: Int, val roomId: Int? = null) : GalleryIntent()
    data class UploadImage(
        val projectId: Int,
        val fileBytes: ByteArray,
        val fileName: String,
        val mimeType: String,
        val roomId: Int? = null,
        val note: String? = null
    ) : GalleryIntent()
    data class DeleteImage(val projectId: Int, val imageId: Int) : GalleryIntent()
}