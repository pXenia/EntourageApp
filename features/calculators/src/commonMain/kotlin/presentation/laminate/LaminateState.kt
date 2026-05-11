package com.entourageapp.features.calculators.presentation.laminate

data class LaminateState(
    val isLoading: Boolean = false,
    val floorArea: String = "",
    val packArea: String = "",
    val reserve: String = "10",
    val result: Int = 0,
    val error: String? = null,
)

sealed class LaminateIntent {
    data class LoadParams(val projectId: Int, val roomId: Int) : LaminateIntent()
    data class UpdateFloorArea(val value: String) : LaminateIntent()
    data class UpdatePackArea(val value: String) : LaminateIntent()
    data class UpdateReserve(val value: String) : LaminateIntent()
    object Calculate : LaminateIntent()
}
