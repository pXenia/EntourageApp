package com.entourageapp.features.rooms.presentation.roomlist

import com.entourageapp.features.rooms.domain.RoomCard

data class RoomListState(
    val isLoading: Boolean = false,
    val rooms: List<RoomCard> = emptyList(),
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val selectedRoomId: Int? = null,
    val selectedRoomTitle: String = ""
)

sealed class RoomListIntent {
    data class LoadRooms(val projectId: Int) : RoomListIntent()
    data class ShowDeleteDialog(val roomId: Int, val roomTitle: String) : RoomListIntent()
    object DismissDeleteDialog : RoomListIntent()
    data class DeleteRoom(val projectId: Int) : RoomListIntent()
}
