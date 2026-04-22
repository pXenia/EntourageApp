package com.entourageapp.features.calculators.presentation.wallpaper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.calculators.domain.CalculatorsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor

class WallpaperVM(
    private val repository: CalculatorsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(WallpaperState())
    val state: StateFlow<WallpaperState> = _state

    fun handleIntent(intent: WallpaperIntent) {
        when (intent) {
            is WallpaperIntent.LoadParams -> loadParams(intent.projectId, intent.roomId)
            is WallpaperIntent.UpdateCeilingHeight -> _state.update { it.copy(ceilingHeight = intent.value) }
            is WallpaperIntent.UpdateRollLength -> _state.update { it.copy(rollLength = intent.value) }
            is WallpaperIntent.UpdateRollWidth -> _state.update { it.copy(rollWidth = intent.value) }
            is WallpaperIntent.UpdatePatternRepeat -> _state.update { it.copy(patternRepeat = intent.value) }
            is WallpaperIntent.UpdateReserve -> _state.update { it.copy(reserve = intent.value) }
            is WallpaperIntent.TogglePatternRepeat -> _state.update {
                it.copy(
                    hasPatternRepeat = !it.hasPatternRepeat,
                    patternRepeat = if (it.hasPatternRepeat) "" else it.patternRepeat
                )
            }
            is WallpaperIntent.ToggleWallSelection -> toggleWall(intent.wallId)
            is WallpaperIntent.UpdateManualWall -> updateManualWall(intent.index, intent.value)
            is WallpaperIntent.AddManualWall -> _state.update { it.copy(manualWalls = it.manualWalls + "") }
            is WallpaperIntent.ShowWallSelectionDialog -> _state.update {
                it.copy(showWallSelectionDialog = true)
            }
            is WallpaperIntent.DismissWallSelectionDialog -> _state.update {
                it.copy(showWallSelectionDialog = false)
            }
            is WallpaperIntent.Calculate -> calculate()
        }
    }

    private fun loadParams(projectId: Int, roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val params = repository.getParams(projectId, roomId)
                _state.update { it.copy(isLoading = false, walls = params.walls, ceilingHeight = (params.ceilingHeight ?: 0).toString()) }
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

        val ceilingHeightCm = s.ceilingHeight.toDoubleOrNull() ?: return
        val rollLengthCm = s.rollLength.toDoubleOrNull() ?: return
        val rollWidthCm = s.rollWidth.toDoubleOrNull() ?: return
        val patternRepeatCm = if (s.hasPatternRepeat) s.patternRepeat.toDoubleOrNull() ?: 0.0 else 0.0
        val reservePercent = s.reserve.toDoubleOrNull() ?: 0.0

        val wallLengthsCm: List<Double> = if (s.walls.isEmpty()) {
            s.manualWalls.mapNotNull { it.toDoubleOrNull() }
        } else {
            s.walls
                .filter { it.id in s.selectedWallIds }
                .map { it.length.toDouble() }
        }

        if (wallLengthsCm.isEmpty()) return

        val effectiveStripLengthCm = if (patternRepeatCm > 0.0) {
            ceil(ceilingHeightCm / patternRepeatCm) * patternRepeatCm
        } else {
            ceilingHeightCm
        }

        val stripsPerRoll = floor(rollLengthCm / effectiveStripLengthCm).toInt()
        if (stripsPerRoll == 0) return

        val totalStrips = wallLengthsCm.sumOf { wallLength ->
            ceil(wallLength / rollWidthCm).toInt()
        }

        val rolls = ceil(totalStrips.toDouble() / stripsPerRoll).toInt()
        val withReserve = ceil(rolls * (1.0 + reservePercent / 100.0)).toInt()

        _state.update { it.copy(result = withReserve) }
    }
}