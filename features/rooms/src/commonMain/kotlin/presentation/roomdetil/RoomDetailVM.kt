package com.entourageapp.features.rooms.presentation.roomdetil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.rooms.domain.RoomsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomDetailVM(
    private val repository: RoomsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RoomDetailState())
    val state: StateFlow<RoomDetailState> = _state

    fun handleIntent(intent: RoomDetailIntent) {
        when (intent) {
            is RoomDetailIntent.LoadRoom -> loadRoom(intent.projectId, intent.roomId)
        }
    }

    private fun loadRoom(projectId: Int, roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getRoomById(roomId)
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
                .collect { room ->
                    _state.update { it.copy(isLoading = false, room = room) }
                }
        }
    }
}