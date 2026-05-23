package com.entourageapp.features.rooms.presentation.roomlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.rooms.domain.RoomsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomListVM(
    private val repository: RoomsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RoomListState())
    val state: StateFlow<RoomListState> = _state

    fun handleIntent(intent: RoomListIntent) {
        when (intent) {
            is RoomListIntent.LoadRooms -> loadRooms(intent.projectId)
            is RoomListIntent.ShowDeleteDialog -> _state.update { 
                it.copy(showDeleteDialog = true, selectedRoomId = intent.roomId, selectedRoomTitle = intent.roomTitle) 
            }
            is RoomListIntent.DismissDeleteDialog -> _state.update { 
                it.copy(showDeleteDialog = false, selectedRoomId = null, selectedRoomTitle = "") 
            }
            is RoomListIntent.DeleteRoom -> deleteRoom(intent.projectId)
        }
    }

    private fun loadRooms(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getRoomList(projectId)
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
                .collect { roomCards ->
                    _state.update {
                        it.copy(isLoading = false, rooms = roomCards)
                    }
                }
        }
    }

    private fun deleteRoom(projectId: Int) {
        val roomId = _state.value.selectedRoomId ?: return
        viewModelScope.launch {
            try {
                repository.deleteRoom(roomId)
                _state.update { it.copy(showDeleteDialog = false, selectedRoomId = null, selectedRoomTitle = "") }
                loadRooms(projectId)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, showDeleteDialog = false) }
            }
        }
    }
}
