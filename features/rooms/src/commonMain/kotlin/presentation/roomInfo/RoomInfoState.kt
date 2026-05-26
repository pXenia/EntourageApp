package com.entourageapp.features.rooms.presentation.roomInfo

import androidx.compose.ui.geometry.Offset
import com.entourageapp.features.rooms.presentation.createroom.WallInfo

data class RoomInfoState(
    val title: String = "",
    val ceilingHeight: Float? = null,
    val description: String? = null,
    val square: Float? = null,
    val walls: List<WallInfo> = emptyList(),
    val points: List<Offset> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val perimeter: Float
        get() = walls.sumOf { it.lengthM.toDouble() }.toFloat()

    val wallArea: Float
        get() = ceilingHeight?.let { it * perimeter } ?: 0f
}

sealed class RoomInfoIntent {
    data class LoadRoom(val projectId: Int, val roomId: Int) : RoomInfoIntent()
}
