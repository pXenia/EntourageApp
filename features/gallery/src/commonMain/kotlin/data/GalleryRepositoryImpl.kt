package com.entourageapp.features.gallery.data

import com.entourageapp.core.network.api.GalleryApi
import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.core.network.dto.gallery.ImageDto
import com.entourageapp.core.network.dto.gallery.ImageUploadedDto
import com.entourageapp.features.gallery.domain.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GalleryRepositoryImpl(
    private val galleryApi: GalleryApi,
    private val roomsApi: RoomsApi
) : GalleryRepository {

    override fun getImages(projectId: Int, roomId: Int?): Flow<List<ImageDto>> = flow {
        emit(galleryApi.getImages(projectId, roomId))
    }.catch { e -> throw e }

    override suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int?,
        note: String?
    ): ImageUploadedDto =
        galleryApi.uploadImage(projectId, fileBytes, fileName, mimeType, roomId, note)

    override suspend fun deleteImage(imageId: Int) {
        galleryApi.deleteImage(imageId)
    }

    override suspend fun updateImage(
        imageId: Int,
        note: String?,
        roomId: Int?
    ) {
        galleryApi.updateImage(imageId, note, roomId)
    }

    override suspend fun getRooms(projectId: Int): List<RoomShortDto> =
        roomsApi.getRoomsShort(projectId)
}
