package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.gallery.ImageDto
import com.entourageapp.core.network.dto.gallery.ImageUploadedDto

interface GalleryApi {
    suspend fun getImages(projectId: Int, roomId: Int? = null): List<ImageDto>
    suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int? = null,
        note: String? = null
    ): ImageUploadedDto
    suspend fun deleteImage(imageId: Int)
    suspend fun updateImage(imageId: Int, note: String?, roomId: Int?)
}
