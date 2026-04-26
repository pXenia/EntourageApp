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
            is GalleryIntent.LoadImages -> loadImages(intent.projectId, intent.roomId)
            is GalleryIntent.UploadImage -> uploadImage(intent)
            is GalleryIntent.DeleteImage -> deleteImage(intent.projectId, intent.imageId)
        }
    }

    private fun loadImages(projectId: Int, roomId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getImages(projectId, roomId)
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { images ->
                    _state.update { it.copy(isLoading = false, images = images) }
                }
        }
    }

    private fun uploadImage(intent: GalleryIntent.UploadImage) {
        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, error = null) }
            try {
                repository.uploadImage(
                    projectId = intent.projectId,
                    fileBytes = intent.fileBytes,
                    fileName = intent.fileName,
                    mimeType = intent.mimeType,
                    roomId = intent.roomId,
                    note = intent.note
                )
                _state.update { it.copy(isUploading = false, error = null) }
                loadImages(intent.projectId, intent.roomId)
            } catch (e: Exception) {
                _state.update { it.copy(isUploading = false, error = e.message) }
            }
        }
    }

    private fun deleteImage(projectId: Int, imageId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteImage(projectId, imageId)
                _state.update { it.copy(images = it.images.filter { img -> img.id != imageId }) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}