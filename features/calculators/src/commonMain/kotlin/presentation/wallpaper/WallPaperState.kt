package com.entourageapp.features.calculators.presentation.wallpaper

import com.entourageapp.core.network.dto.WallDto

data class WallpaperState(
    val isLoading: Boolean = false,
    val walls: List<WallDto> = emptyList(),
    val ceilingHeight: String = "",
    val rollLength: String = "",
    val rollWidth: String = "",
    val hasPatternRepeat: Boolean = false,
    val patternRepeat: String = "",
    val reserve: String = "",
    val result: Int = 0,
    val error: String? = null,
    val showWallSelectionDialog: Boolean = false,
    val selectedWallIds: Set<Int> = emptySet(),
    val manualWalls: List<String> = listOf(""),
)

sealed class WallpaperIntent {
    data class LoadWalls(val projectId: Int, val roomId: Int) : WallpaperIntent()
    data class UpdateCeilingHeight(val value: String) : WallpaperIntent()
    data class UpdateRollLength(val value: String) : WallpaperIntent()
    data class UpdateRollWidth(val value: String) : WallpaperIntent()
    data class UpdatePatternRepeat(val value: String) : WallpaperIntent()
    data class UpdateReserve(val value: String) : WallpaperIntent()
    data class ToggleWallSelection(val wallId: Int) : WallpaperIntent()
    data class UpdateManualWall(val index: Int, val value: String) : WallpaperIntent()
    object TogglePatternRepeat : WallpaperIntent()
    object AddManualWall : WallpaperIntent()
    object ShowWallSelectionDialog : WallpaperIntent()
    object DismissWallSelectionDialog : WallpaperIntent()
    object Calculate : WallpaperIntent()
}