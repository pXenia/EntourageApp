package com.entourageapp.features.gallery.data

import com.entourageapp.core.network.api.GalleryApi
import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.features.gallery.domain.GalleryImage
import com.entourageapp.features.gallery.domain.GalleryRepository
import com.entourageapp.features.gallery.domain.GalleryRoom
import com.entourageapp.features.gallery.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GalleryRepositoryImpl(
    private val galleryApi: GalleryApi,
    private val roomsApi: RoomsApi
) : GalleryRepository {

    override fun getImages(projectId: Int, roomId: Int?): Flow<List<GalleryImage>> = flow {
        emit(galleryApi.getImages(projectId, roomId))
    }.map { list ->
        list.map { it.toDomain() }
    }.catch { e -> throw e }

    override suspend fun uploadImage(
        projectId: Int,
        fileBytes: ByteArray,
        fileName: String,
        mimeType: String,
        roomId: Int?,
        note: String?
    ) {
        galleryApi.uploadImage(projectId, fileBytes, fileName, mimeType, roomId, note)
    }

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

    override suspend fun getRooms(projectId: Int): List<GalleryRoom> =
        roomsApi.getRoomsShort(projectId).map { it.toDomain() }
}
