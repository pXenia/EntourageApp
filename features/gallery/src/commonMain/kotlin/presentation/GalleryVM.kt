package com.entourageapp.features.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.gallery.domain.GalleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryVM(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state: StateFlow<GalleryState> = _state

    fun handleIntent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.ChangeStatus -> _state.update { it.copy(status = intent.status) }
            is GalleryIntent.ChangeSelectedImageId -> _state.update { it.copy(selectedImageId = intent.id) }
            is GalleryIntent.LoadImages -> loadImages(intent.projectId, intent.roomId)
            is GalleryIntent.UploadImage -> uploadImage(intent)
            is GalleryIntent.DeleteImage -> deleteImage(intent.projectId, intent.imageId)
            is GalleryIntent.DeleteSelectedImages -> deleteSelectedImages(intent.projectId)
            is GalleryIntent.ToggleSelection -> toggleSelection(intent.id)
            is GalleryIntent.ClearSelection -> _state.update { it.copy(isSelectionMode = false, selectedIds = emptySet()) }
            is GalleryIntent.ChangeAddImageVisibility -> _state.update { it.copy(isAddImageVisible = intent.isVisible) }
            is GalleryIntent.SetSelectedImage -> _state.update { it.copy(selectedImageData = intent.data) }
        }
    }

    private fun loadImages(projectId: Int, roomId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(status = GalleryState.GalleryStatus.Loading) }
            repository.getImages(projectId, roomId)
                .catch { e ->
                    _state.update { it.copy(status = GalleryState.GalleryStatus.Error) }
                }
                .collect { images ->
                    _state.update { it.copy(status = GalleryState.GalleryStatus.List, images = images) }
                }
        }
    }

    private fun uploadImage(intent: GalleryIntent.UploadImage) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isAddImageVisible = false, selectedImageData = null) }
                repository.uploadImage(
                    projectId = intent.projectId,
                    fileBytes = intent.image.fileBytes,
                    fileName = intent.image.fileName,
                    mimeType = intent.image.mimeType,
                    roomId = intent.roomId,
                    note = intent.note
                )
                loadImages(intent.projectId, intent.roomId)
            } catch (e: Exception) {
                _state.update { it.copy(status = GalleryState.GalleryStatus.Error) }
            }
        }
    }

    private fun deleteImage(projectId: Int, imageId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteImage(projectId, imageId)
                _state.update {
                    it.copy(
                        images = it.images.filter { img -> img.id != imageId },
                        status = if (it.images.size <= 1) GalleryState.GalleryStatus.IsEmpty else GalleryState.GalleryStatus.List,
                        selectedIds = it.selectedIds - imageId
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = GalleryState.GalleryStatus.Error) }
            }
        }
    }

    private fun toggleSelection(id: Int) {
        _state.update {
            val newSelected = if (it.selectedIds.contains(id)) {
                it.selectedIds - id
            } else {
                it.selectedIds + id
            }
            it.copy(
                selectedIds = newSelected,
                isSelectionMode = newSelected.isNotEmpty()
            )
        }
    }

    private fun deleteSelectedImages(projectId: Int) {
        viewModelScope.launch {
            val idsToDelete = _state.value.selectedIds
            try {
                idsToDelete.forEach { id ->
                    repository.deleteImage(projectId, id)
                }
                _state.update { state ->
                    val remainingImages = state.images.filter { !idsToDelete.contains(it.id) }
                    state.copy(
                        images = remainingImages,
                        isSelectionMode = false,
                        selectedIds = emptySet(),
                        status = if (remainingImages.isEmpty()) GalleryState.GalleryStatus.IsEmpty else GalleryState.GalleryStatus.List
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(status = GalleryState.GalleryStatus.Error) }
            }
        }
    }
}