package com.entourageapp.features.gallery.domain

import com.entourageapp.core.network.dto.gallery.ImageDto
import com.entourageapp.core.network.dto.RoomShortDto

data class GalleryImage(
    val id: Int,
    val projectId: Int,
    val roomId: Int? = null,
    val note: String? = null,
    val url: String? = null
)

data class GalleryRoom(
    val id: Int,
    val title: String
)

fun ImageDto.toDomain(): GalleryImage = GalleryImage(
    id = id,
    projectId = projectId,
    roomId = roomId,
    note = note,
    url = url
)

fun RoomShortDto.toDomain(): GalleryRoom = GalleryRoom(
    id = id,
    title = title
)
