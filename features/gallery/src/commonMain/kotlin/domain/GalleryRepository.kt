package com.entourageapp.features.gallery.domain

import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.ImageUploadedDto
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImages(projectId: Int, roomId: Int? = null): Flow<List<ImageDto>>
    suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int? = null,
        note: String? = null
    ): ImageUploadedDto
    suspend fun deleteImage(projectId: Int, imageId: Int)
    suspend fun updateImage(projectId: Int, imageId: Int, note: String? = null, roomId: Int? = null)
}