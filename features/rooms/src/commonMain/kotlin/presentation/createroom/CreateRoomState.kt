package com.entourageapp.features.rooms.presentation.createroom

import androidx.compose.ui.geometry.Offset
import com.entourageapp.core.network.dto.RoomTypeDto

data class CreateRoomState(
    val title: String = "",
    val roomTypes: List<RoomTypeDto> = emptyList(),
    val selectedRoomType: RoomTypeDto? = null,
    val description: String = "",
    val ceilingHeight: String = "",
    val points: List<Offset> = emptyList(),
    val walls: List<WallInfo> = emptyList(),
    val square: Float = 0f,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class CreateRoomIntent {
    data class OnTitleChanged(val value: String) : CreateRoomIntent()
    data class OnDescriptionChanged(val value: String) : CreateRoomIntent()
    data class OnCeilingHeightChanged(val value: String) : CreateRoomIntent()
    data class OnRoomTypeSelected(val roomType: RoomTypeDto) : CreateRoomIntent()
    data class OnPlanSaved(
        val points: List<Offset>,
        val walls: List<WallInfo>,
        val square: Float
    ) : CreateRoomIntent()
    data class LoadRoomTypes(val projectId: Int) : CreateRoomIntent()
    data class Submit(val projectId: Int) : CreateRoomIntent()
}