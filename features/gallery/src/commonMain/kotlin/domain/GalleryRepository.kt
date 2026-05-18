package com.entourageapp.features.gallery.domain

import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.core.network.dto.gallery.ImageDto
import com.entourageapp.core.network.dto.gallery.ImageUploadedDto
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
    suspend fun deleteImage(imageId: Int)
    suspend fun updateImage(imageId: Int, note: String? = null, roomId: Int? = null)
    suspend fun getRooms(projectId: Int): List<RoomShortDto>
}
