package com.entourageapp.features.gallery.domain

import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImages(projectId: Int, roomId: Int? = null): Flow<List<GalleryImage>>
    suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int? = null,
        note: String? = null
    )
    suspend fun deleteImage(imageId: Int)
    suspend fun updateImage(imageId: Int, note: String? = null, roomId: Int? = null)
    suspend fun getRooms(projectId: Int): List<GalleryRoom>
}
