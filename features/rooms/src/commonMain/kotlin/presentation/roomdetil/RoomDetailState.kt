package com.entourageapp.features.rooms.presentation.roomdetil

import com.entourageapp.features.rooms.domain.RoomDetail

data class RoomDetailState(
    val isLoading: Boolean = false,
    val room: RoomDetail? = null,
    val error: String? = null
)

sealed class RoomDetailIntent {
    data class LoadRoom(val projectId: Int, val roomId: Int) : RoomDetailIntent()
}