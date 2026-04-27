package com.entourageapp.features.gallery.presentation

import com.entourageapp.core.network.dto.ImageDto

data class GalleryState(
    val images: List<ImageDto> = emptyList(),
    val selectedImageId: Int = -1,
    val status: GalleryStatus = GalleryStatus.Loading,
    val isAddImageVisible: Boolean = false,
    val selectedImageData: SelectedImageData? = null,
    val selectedIds: Set<Int> = emptySet(),
    val isSelectionMode: Boolean = false
) {
    sealed interface GalleryStatus {
        object Loading : GalleryStatus
        object IsEmpty : GalleryStatus
        object Error : GalleryStatus
        object List : GalleryStatus
        object ViewPager : GalleryStatus
    }

    data class SelectedImageData(
        val fileBytes: ByteArray,
        val fileName: String,
        val mimeType: String
    )
}

sealed class GalleryIntent {
    data class ChangeAddImageVisibility(val isVisible: Boolean) : GalleryIntent()
    data class ChangeStatus(val status: GalleryState.GalleryStatus) : GalleryIntent()
    data class ChangeSelectedImageId(val id: Int) : GalleryIntent()

    data class LoadImages(val projectId: Int, val roomId: Int? = null) : GalleryIntent()
    data class UploadImage(
        val projectId: Int,
        val image: GalleryState.SelectedImageData,
        val roomId: Int? = null,
        val note: String? = null
    ) : GalleryIntent()

    data class DeleteImage(val projectId: Int, val imageId: Int) : GalleryIntent()
    data class DeleteSelectedImages(val projectId: Int) : GalleryIntent()
    data class ToggleSelection(val id: Int) : GalleryIntent()
    object ClearSelection : GalleryIntent()
    data class SetSelectedImage(val data: GalleryState.SelectedImageData?) : GalleryIntent()
}