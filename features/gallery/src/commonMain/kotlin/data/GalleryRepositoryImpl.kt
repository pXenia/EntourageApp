package com.entourageapp.features.gallery.data

import com.entourageapp.core.network.api.GalleryApi
import com.entourageapp.core.network.dto.ImageDto
import com.entourageapp.core.network.dto.ImageUploadedDto
import com.entourageapp.features.gallery.domain.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GalleryRepositoryImpl(
    private val api: GalleryApi
) : GalleryRepository {

    override fun getImages(projectId: Int, roomId: Int?): Flow<List<ImageDto>> = flow {
        emit(api.getImages(projectId, roomId))
    }.catch { e -> throw e }

    override suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int?,
        note: String?
    ): ImageUploadedDto =
        api.uploadImage(projectId, fileBytes, fileName, mimeType, roomId, note)

    override suspend fun deleteImage(projectId: Int, imageId: Int) {
        api.deleteImage(projectId, imageId)
    }

    override suspend fun updateImage(
        projectId: Int,
        imageId: Int,
        note: String?,
        roomId: Int?
    ) {
        api.updateImage(projectId, imageId, note, roomId)
    }
}