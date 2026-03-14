package com.entourageapp.features.rooms.presentation.roomlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.rooms.domain.RoomsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomsVM(
    private val repository: RoomsRepository,
    private val projectId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(RoomListState())
    val state: StateFlow<RoomListState> = _state

    fun handleIntent(intent: RoomListIntent) {
        when (intent) {
            is RoomListIntent.LoadRooms -> loadRooms(intent.projectId)
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
}