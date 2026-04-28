package com.entourageapp.features.gallery.presentation

import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.RoomShortDto

data class GalleryState(
    val images: List<ImageDto> = emptyList(),
    val selectedImageId: Int? = null,
    val status: GalleryStatus = GalleryStatus.Loading,
    val isAddImageVisible: Boolean = false,
    val selectedImageData: SelectedImageData? = null,
    val selectedIds: Set<Int> = emptySet(),
    val isSelectionMode: Boolean = false,
    val searchQuery: String = "",
    val isSearchVisible: Boolean = false,
    val availableRooms: List<RoomShortDto> = emptyList()
) {
    sealed interface GalleryStatus {
        data object Loading : GalleryStatus
        data object IsEmpty : GalleryStatus
        data object Error : GalleryStatus
        data object List : GalleryStatus
        data object ViewPager : GalleryStatus
    }

    data class SelectedImageData(
        val fileBytes: ByteArray,
        val fileName: String,
        val mimeType: String
    )
}

sealed interface GalleryIntent {
    data class LoadImages(val projectId: Int, val roomId: Int? = null) : GalleryIntent
    data class LoadRooms(val projectId: Int) : GalleryIntent
    
    data class ToggleSelection(val imageId: Int) : GalleryIntent
    data object ClearSelection : GalleryIntent
    
    data class SelectImage(val imageId: Int) : GalleryIntent
    data object CloseViewPager : GalleryIntent
    
    data class UpdateImage(val projectId: Int, val imageId: Int, val note: String?, val roomId: Int?) : GalleryIntent
    data class DeleteImage(val projectId: Int, val imageId: Int) : GalleryIntent
    data class DeleteSelectedImages(val projectId: Int) : GalleryIntent
    
    data class SetSearchVisibility(val isVisible: Boolean) : GalleryIntent
    data class UpdateSearchQuery(val query: String) : GalleryIntent
    
    data class SetAddImageVisibility(val isVisible: Boolean) : GalleryIntent
    data class SetSelectedImageData(val data: GalleryState.SelectedImageData?) : GalleryIntent
    data class UploadImage(
        val projectId: Int,
        val image: GalleryState.SelectedImageData,
        val roomId: Int? = null,
        val note: String? = null
    ) : GalleryIntent
}

sealed interface GallerySideEffect {
    data class ShowError(val message: String) : GallerySideEffect
    data object NavigateBack : GallerySideEffect
}
