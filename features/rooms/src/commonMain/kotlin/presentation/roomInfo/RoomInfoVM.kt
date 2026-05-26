package com.entourageapp.features.rooms.presentation.roomInfo

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.features.rooms.presentation.createroom.WallInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomInfoVM(
    private val roomsApi: RoomsApi
) : ViewModel() {

    private val _state = MutableStateFlow(RoomInfoState())
    val state: StateFlow<RoomInfoState> = _state.asStateFlow()

    fun handleIntent(intent: RoomInfoIntent) {
        when (intent) {
            is RoomInfoIntent.LoadRoom -> loadRoom(intent.projectId, intent.roomId)
        }
    }

    private fun loadRoom(projectId: Int, roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val detailDef = async { roomsApi.getRoomById(roomId) }
                val paramsDef = async { roomsApi.getParams(roomId) }
                val offsetsDef = async { roomsApi.getOffsets(roomId) }

                val detail = detailDef.await()
                val params = paramsDef.await()
                val offsets = offsetsDef.await()
                
                _state.update { it.copy(
                    isLoading = false,
                    title = detail.title,
                    ceilingHeight = detail.ceilingHeight,
                    description = detail.description,
                    square = detail.square,
                    walls = params.walls.mapIndexed { index, wall -> 
                        WallInfo(index = index + 1, lengthM = wall.length)
                    },
                    points = offsets.map { Offset(it.x, it.y) }
                ) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
