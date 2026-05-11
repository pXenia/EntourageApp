package com.entourageapp.features.calculators.presentation.paint

import com.entourageapp.core.network.dto.WallDto

enum class PaintTarget {
    WALLS, CEILING
}

data class PaintState(
    val isLoading: Boolean = false,
    val targets: Set<PaintTarget> = setOf(PaintTarget.WALLS),
    val walls: List<WallDto> = emptyList(),
    val ceilingHeight: String = "",
    val manualArea: String = "",
    val consumption: String = "",
    val layers: String = "2",
    val reserve: String = "",
    val result: Double = 0.0,
    val error: String? = null,
    val showWallSelectionDialog: Boolean = false,
    val selectedWallIds: Set<Int> = emptySet(),
    val manualWalls: List<String> = listOf(""),
)

sealed class PaintIntent {
    data class LoadParams(val projectId: Int, val roomId: Int) : PaintIntent()
    data class ToggleTarget(val target: PaintTarget) : PaintIntent()
    data class UpdateCeilingHeight(val value: String) : PaintIntent()
    data class UpdateManualArea(val value: String) : PaintIntent()
    data class UpdateConsumption(val value: String) : PaintIntent()
    data class UpdateLayers(val value: String) : PaintIntent()
    data class UpdateReserve(val value: String) : PaintIntent()
    data class ToggleWallSelection(val wallId: Int) : PaintIntent()
    data class UpdateManualWall(val index: Int, val value: String) : PaintIntent()
    object AddManualWall : PaintIntent()
    object ShowWallSelectionDialog : PaintIntent()
    object DismissWallSelectionDialog : PaintIntent()
    object Calculate : PaintIntent()
}
