package com.entourageapp.features.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.gallery.domain.GalleryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryVM(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state: StateFlow<GalleryState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<GallerySideEffect>()
    val sideEffect: SharedFlow<GallerySideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: GalleryIntent) {
        when (intent) {
            is GalleryIntent.LoadImages -> loadImages(intent.projectId, intent.roomId)
            is GalleryIntent.LoadRooms -> loadRooms(intent.projectId)
            is GalleryIntent.ToggleSelection -> toggleSelection(intent.imageId)
            is GalleryIntent.ClearSelection -> clearSelection()
            is GalleryIntent.SelectImage -> selectImage(intent.imageId)
            is GalleryIntent.CloseViewPager -> closeViewPager()
            is GalleryIntent.UpdateImage -> updateImage(intent)
            is GalleryIntent.DeleteImage -> deleteImage(intent.projectId, intent.imageId)
            is GalleryIntent.DeleteSelectedImages -> deleteSelectedImages(intent.projectId)
            is GalleryIntent.SetSearchVisibility -> setSearchVisibility(intent.isVisible)
            is GalleryIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is GalleryIntent.SetAddImageVisibility -> setAddImageVisibility(intent.isVisible)
            is GalleryIntent.SetSelectedImageData -> setSelectedImageData(intent.data)
            is GalleryIntent.UploadImage -> uploadImage(intent)
        }
    }

    private fun loadImages(projectId: Int, roomId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(status = GalleryState.GalleryStatus.Loading) }
            repository.getImages(projectId, roomId)
                .catch { e ->
                    _state.update { it.copy(status = GalleryState.GalleryStatus.Error) }
                    _sideEffect.emit(GallerySideEffect.ShowError("Ошибка при загрузке изображений"))
                }
                .collect { images ->
                    _state.update {
                        if (images.isEmpty()) {
                            it.copy(
                                status = GalleryState.GalleryStatus.IsEmpty,
                                images = emptyList()
                            )
                        } else {
                            it.copy(status = GalleryState.GalleryStatus.List, images = images)
                        }
                    }
                }
        }
    }

    private fun loadRooms(projectId: Int) {
        viewModelScope.launch {
            try {
                val rooms = repository.getRooms(projectId)
                _state.update { it.copy(availableRooms = rooms) }
            } catch (e: Exception) {
            }
        }
    }

    private fun toggleSelection(imageId: Int) {
        _state.update { state ->
            val newSelected = if (state.selectedIds.contains(imageId)) {
                state.selectedIds - imageId
            } else {
                state.selectedIds + imageId
            }
            state.copy(
                selectedIds = newSelected,
                isSelectionMode = newSelected.isNotEmpty()
            )
        }
    }

    private fun clearSelection() {
        _state.update { it.copy(isSelectionMode = false, selectedIds = emptySet()) }
    }

    private fun selectImage(imageId: Int) {
        _state.update {
            it.copy(
                selectedImageId = imageId,
                status = GalleryState.GalleryStatus.ViewPager
            )
        }
    }

    private fun closeViewPager() {
        _state.update { it.copy(status = GalleryState.GalleryStatus.List) }
    }

    private fun updateImage(intent: GalleryIntent.UpdateImage) {
        viewModelScope.launch {
            try {
                repository.updateImage(
                    projectId = intent.projectId,
                    imageId = intent.imageId,
                    note = intent.note,
                    roomId = intent.roomId
                )
                _state.update {
                    it.copy(
                        images = it.images.map { image ->
                            if (image.id == intent.imageId) {
                                image.copy(note = intent.note, roomId = intent.roomId)
                            } else {
                                image
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                _sideEffect.emit(GallerySideEffect.ShowError("$e"))
            }
        }
    }

    private fun deleteImage(projectId: Int, imageId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteImage(projectId, imageId)
                _state.update { state ->
                    val remainingImages = state.images.filter { it.id != imageId }
                    state.copy(
                        images = remainingImages,
                        selectedIds = state.selectedIds - imageId,
                        status = if (remainingImages.isEmpty()) GalleryState.GalleryStatus.IsEmpty else state.status
                    )
                }
            } catch (e: Exception) {
                _sideEffect.emit(GallerySideEffect.ShowError("Ошибка при удалении изображения"))
            }
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
                        status = if (remainingImages.isEmpty()) GalleryState.GalleryStatus.IsEmpty else state.status
                    )
                }
            } catch (e: Exception) {
                _sideEffect.emit(GallerySideEffect.ShowError("Ошибка при удалении выбранных изображений"))
            }
        }
    }

    private fun setSearchVisibility(isVisible: Boolean) {
        _state.update {
            it.copy(
                isSearchVisible = isVisible,
                searchQuery = if (!isVisible) "" else it.searchQuery
            )
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun setAddImageVisibility(isVisible: Boolean) {
        _state.update { it.copy(isAddImageVisible = isVisible) }
    }

    private fun setSelectedImageData(data: GalleryState.SelectedImageData?) {
        _state.update { it.copy(selectedImageData = data) }
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
                _sideEffect.emit(GallerySideEffect.ShowError("Ошибка при загрузке изображения"))
            }
        }
    }
}
