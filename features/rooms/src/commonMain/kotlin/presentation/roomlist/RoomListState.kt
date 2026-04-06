package com.entourageapp.features.rooms.presentation.roomlist

import com.entourageapp.features.rooms.domain.RoomCard

data class RoomListState(
    val isLoading: Boolean = false,
    val rooms: List<RoomCard> = emptyList(),
    val error: String? = null
)

sealed class RoomListIntent {
    data class LoadRooms(val projectId: Int) : RoomListIntent()
}