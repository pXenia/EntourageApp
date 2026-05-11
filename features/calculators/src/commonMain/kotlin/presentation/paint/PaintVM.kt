package com.entourageapp.features.calculators.presentation.paint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.calculators.domain.CalculatorsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

class PaintVM(
    private val repository: CalculatorsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PaintState())
    val state: StateFlow<PaintState> = _state

    fun handleIntent(intent: PaintIntent) {
        when (intent) {
            is PaintIntent.LoadParams -> loadParams(intent.projectId, intent.roomId)
            is PaintIntent.ToggleTarget -> _state.update { current ->
                val newTargets = if (intent.target in current.targets) {
                    current.targets - intent.target
                } else {
                    current.targets + intent.target
                }
                current.copy(targets = newTargets, result = 0.0)
            }
            is PaintIntent.UpdateCeilingHeight -> _state.update { it.copy(ceilingHeight = intent.value) }
            is PaintIntent.UpdateManualArea -> _state.update { it.copy(manualArea = intent.value) }
            is PaintIntent.UpdateConsumption -> _state.update { it.copy(consumption = intent.value) }
            is PaintIntent.UpdateLayers -> _state.update { it.copy(layers = intent.value) }
            is PaintIntent.UpdateReserve -> _state.update { it.copy(reserve = intent.value) }
            is PaintIntent.ToggleWallSelection -> toggleWall(intent.wallId)
            is PaintIntent.UpdateManualWall -> updateManualWall(intent.index, intent.value)
            is PaintIntent.AddManualWall -> _state.update { it.copy(manualWalls = it.manualWalls + "") }
            is PaintIntent.ShowWallSelectionDialog -> _state.update {
                it.copy(showWallSelectionDialog = true)
            }
            is PaintIntent.DismissWallSelectionDialog -> _state.update {
                it.copy(showWallSelectionDialog = false)
            }
            is PaintIntent.Calculate -> calculate()
        }
    }

    private fun loadParams(projectId: Int, roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val params = repository.getParams(projectId, roomId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        walls = params.walls,
                        ceilingHeight = (params.ceilingHeight ?: 0).toString()
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun toggleWall(wallId: Int) {
        _state.update { current ->
            val updated = if (wallId in current.selectedWallIds)
                current.selectedWallIds - wallId
            else
                current.selectedWallIds + wallId
            current.copy(selectedWallIds = updated)
        }
    }

    private fun updateManualWall(index: Int, value: String) {
        _state.update { current ->
            val updated = current.manualWalls.toMutableList().also { it[index] = value }
            current.copy(manualWalls = updated)
        }
    }

    private fun calculate() {
        val s = _state.value

        val consumptionM2L = s.consumption.toDoubleOrNull() ?: return
        val layers = s.layers.toDoubleOrNull() ?: 1.0
        val reservePercent = s.reserve.toDoubleOrNull() ?: 0.0

        if (consumptionM2L <= 0.0) return

        var totalAreaM2 = 0.0

        if (PaintTarget.WALLS in s.targets) {
            val ceilingHeightCm = s.ceilingHeight.toDoubleOrNull() ?: return
            val wallLengthsCm: List<Double> = if (s.walls.isEmpty()) {
                s.manualWalls.mapNotNull { it.toDoubleOrNull() }
            } else {
                s.walls
                    .filter { it.id in s.selectedWallIds }
                    .map { it.length.toDouble() }
            }
            if (wallLengthsCm.isNotEmpty()) {
                totalAreaM2 += (wallLengthsCm.sum() * ceilingHeightCm) / 10000.0
            }
        }

        if (PaintTarget.CEILING in s.targets) {
            s.manualArea.toDoubleOrNull()?.let {
                totalAreaM2 += it
            }
        }

        if (totalAreaM2 <= 0.0) return

        val requiredLiters = (totalAreaM2 / consumptionM2L) * layers
        val withReserve = requiredLiters * (1.0 + reservePercent / 100.0)

        // Round to 2 decimal places
        val roundedResult = ceil(withReserve * 100) / 100.0

        _state.update { it.copy(result = roundedResult) }
    }
}
