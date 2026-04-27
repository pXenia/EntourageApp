package com.entourageapp.features.gallery.presentation

import com.entourageapp.core.network.dto.ImageDto

data class GalleryState(
    val images: List<ImageDto> = emptyList(),
    val selectedImageId: Int = -1,
    val status: GalleryStatus = GalleryStatus.Loading
) {
    sealed interface GalleryStatus {
        object Loading : GalleryStatus
        object IsEmpty : GalleryStatus
        object Error : GalleryStatus
        object List : GalleryStatus
        object ViewPager : GalleryStatus
    }
}

sealed class GalleryIntent {
    data class ChangeStatus(val status: GalleryState.GalleryStatus) : GalleryIntent()
    data class ChangeSelectedImageId(val id: Int) : GalleryIntent()

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