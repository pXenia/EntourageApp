package com.entourageapp.features.rooms.presentation.createplan

import androidx.compose.ui.geometry.Offset

enum class DrawMode { DRAW, EDIT, DELETE }

data class RoomDrawerState(
    val points: List<Offset> = emptyList(),
    val mode: DrawMode = DrawMode.DRAW,
    val snapEnabled: Boolean = true,
    val dragIndex: Int = -1,
    val cellSizePx: Float = 0f
)

sealed class RoomDrawerIntent {
    data class AddPoint(val offset: Offset) : RoomDrawerIntent()
    data class MovePoint(val index: Int, val offset: Offset) : RoomDrawerIntent()
    data class RemovePoint(val index: Int) : RoomDrawerIntent()
    data class SetMode(val mode: DrawMode) : RoomDrawerIntent()
    data class ToggleSnap(val enabled: Boolean) : RoomDrawerIntent()
    data class UpdateCellSize(val sizePx: Float) : RoomDrawerIntent()
    object ClearAll : RoomDrawerIntent()
    object DragEnd : RoomDrawerIntent()
}