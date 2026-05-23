package com.entourageapp.core.network.dto.rooms

import kotlinx.serialization.Serializable

@Serializable
data class RoomTypeDto(
    val id: Int,
    val title: String
)