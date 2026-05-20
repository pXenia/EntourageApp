package com.entourageapp.features.estimates.presentation.createestimateposition

import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import com.entourageapp.core.network.dto.RoomShortDto

data class CreatePositionState(
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val selectedType: EstimateItemTypeDto? = null,
    val selectedUnit: MeasureUnitDto? = null,
    val selectedRoom: RoomShortDto? = null,

    val availableTypes: List<EstimateItemTypeDto> = emptyList(),
    val availableUnits: List<MeasureUnitDto> = emptyList(),
    val availableRooms: List<RoomShortDto> = emptyList(),

    val isRoomDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    val total: Double get() = (price.toDoubleOrNull() ?: 0.0) * (quantity.toDoubleOrNull() ?: 0.0)
}

sealed class CreatePositionIntent {
    data class LoadDictionaries(val projectId: Int, val roomId: Int) : CreatePositionIntent()
    data class UpdateName(val value: String) : CreatePositionIntent()
    data class UpdatePrice(val value: String) : CreatePositionIntent()
    data class UpdateQuantity(val value: String) : CreatePositionIntent()
    data class SelectType(val type: EstimateItemTypeDto) : CreatePositionIntent()
    data class SelectUnit(val unit: MeasureUnitDto) : CreatePositionIntent()
    data class SelectRoom(val room: RoomShortDto) : CreatePositionIntent()
    object ClearRoom : CreatePositionIntent()
    data class Submit(val projectId: Int) : CreatePositionIntent()
}