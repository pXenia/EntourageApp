package com.entourageapp.core.network.dto.rooms

import kotlinx.serialization.Serializable

@Serializable
data class RoomShortDto(
    val id: Int,
    val title: String
)