package com.entourageapp.features.calculators.presentation.laminate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.calculators.domain.CalculatorsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

class LaminateVM(
    private val repository: CalculatorsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LaminateState())
    val state: StateFlow<LaminateState> = _state

    fun handleIntent(intent: LaminateIntent) {
        when (intent) {
            is LaminateIntent.LoadParams -> loadParams(intent.projectId, intent.roomId)
            is LaminateIntent.UpdateFloorArea -> _state.update { it.copy(floorArea = intent.value) }
            is LaminateIntent.UpdatePackArea -> _state.update { it.copy(packArea = intent.value) }
            is LaminateIntent.UpdateReserve -> _state.update { it.copy(reserve = intent.value) }
            is LaminateIntent.Calculate -> calculate()
        }
    }

    private fun loadParams(projectId: Int, roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // We might still want to get the floor area if available from room params
                // However, RoomParamsDto currently only has walls and ceiling height.
                // RoomDetailDto has square (area). 
                // For now, let's just keep it simple or check if repository can provide it.
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun calculate() {
        val s = _state.value

        val floorAreaM2 = s.floorArea.toDoubleOrNull() ?: return
        val packAreaM2 = s.packArea.toDoubleOrNull() ?: return
        val reservePercent = s.reserve.toDoubleOrNull() ?: 0.0

        if (packAreaM2 <= 0) return

        val withReserve = floorAreaM2 * (1.0 + reservePercent / 100.0)
        val boxes = ceil(withReserve / packAreaM2).toInt()

        _state.update { it.copy(result = boxes) }
    }
}
