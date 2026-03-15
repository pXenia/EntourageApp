package com.entourageapp.features.rooms.presentation.createroom

import com.entourageapp.core.network.dto.RoomTypeDto

class CreateRoomState(
    val title: String,
    val roomTypes: List<RoomTypeDto> = emptyList(),
    val selectedRoomType: RoomTypeDto? = null,
    val description: String,
    val square: Float,
    val ceilingHeight: Float,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)