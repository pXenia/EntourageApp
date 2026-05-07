package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomDto(
    val id: Int,
    val title: String,
    @SerialName("type_id")
    val type: Int?,
    val description: String?,
    val square: Float,
    @SerialName("ceiling_height")
    val ceilingHeight: Float,
)

@Serializable
data class RoomAddDto(
    val title: String,
    @SerialName("type_id")
    val typeCode: Int? = null,
    val description: String? = null,
    val square: Float? = null,
    @SerialName("ceiling_height")
    val ceilingHeight: Float? = null
)

@Serializable
data class WallAddDto(val length: Float)

@Serializable
data class OffsetAddDto(val x: Float, val y: Float)

@Serializable
data class OffsetDto(
    val id: Int,
    val x: Float,
    val y: Float
)

@Serializable
data class RoomFullUpdateDto(
    val title: String,
    @SerialName("type_id")
    val typeCode: Int? = null,
    val description: String? = null,
    val square: Float? = null,
    @SerialName("ceiling_height")
    val ceilingHeight: Float? = null,
    val walls: List<WallAddDto> = emptyList(),
    val offsets: List<OffsetAddDto> = emptyList()
)

@Serializable
data class RoomCreatedDto(
    val message: String,
    @SerialName("room_id")
    val roomId: Int
)