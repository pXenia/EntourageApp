package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.ImageUploadedDto
import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.RoomShortDto

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
    suspend fun deleteImage(projectId: Int, imageId: Int): MessageDto
    suspend fun updateImage(projectId: Int, imageId: Int, note: String?, roomId: Int?): MessageDto
    suspend fun getRooms(projectId: Int): List<RoomShortDto>
}
