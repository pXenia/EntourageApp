package com.entourageapp.core.network.dto.rooms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallDto(
    val id: Int,
    val length: Float
)

@Serializable
data class RoomParamsDto(
    @SerialName("ceiling_height")
    val ceilingHeight: Float?,
    val walls: List<WallDto>
)