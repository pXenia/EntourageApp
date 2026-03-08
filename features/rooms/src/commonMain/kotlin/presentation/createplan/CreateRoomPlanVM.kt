package com.entourageapp.features.rooms.presentation.createplan

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CreateRoomPlanVM: ViewModel() {
    private val _state = MutableStateFlow(RoomDrawerState())
    val state: StateFlow<RoomDrawerState> = _state

    // обработка входящих действий
    fun handleIntent(intent: RoomDrawerIntent) {
        when (intent) {
            is RoomDrawerIntent.AddPoint -> addPoint(intent.offset)
            is RoomDrawerIntent.MovePoint -> movePoint(intent.index, intent.offset)
            is RoomDrawerIntent.RemovePoint -> removePoint(intent.index)
            is RoomDrawerIntent.SetMode -> _state.update { it.copy(mode = intent.mode) }
            is RoomDrawerIntent.ToggleSnap -> _state.update { it.copy(snapEnabled = intent.enabled) }
            is RoomDrawerIntent.UpdateCellSize -> _state.update { it.copy(cellSizePx = intent.sizePx) }
            is RoomDrawerIntent.ClearAll -> _state.update { it.copy(points = emptyList()) }
            is RoomDrawerIntent.DragEnd -> _state.update { it.copy(dragIndex = -1) }
        }
    }

    private fun addPoint(point: Offset) {
        _state.update { it.copy(points = it.points + point) }
    }

    private fun movePoint(index: Int, newOffset: Offset) {
        _state.update {
            val newList = it.points.toMutableList()
            if (index in newList.indices) newList[index] = newOffset
            it.copy(points = newList, dragIndex = index)
        }
    }

    private fun removePoint(index: Int) {
        _state.update {
            val newList = it.points.toMutableList()
            if (index in newList.indices) newList.removeAt(index)
            it.copy(points = newList)
        }
    }
}