package presentation.createestimateposition

import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.MeasureUnitDto

data class CreatePositionState(
    val name: String = "",
    val selectedType: EstimateItemTypeDto? = null,
    val selectedUnit: MeasureUnitDto? = null,
    val price: String = "",
    val quantity: String = "",
    val roomId: Int? = null,
    val availableTypes: List<EstimateItemTypeDto> = emptyList(),
    val availableUnits: List<MeasureUnitDto> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    val total: Double
        get() = (price.toDoubleOrNull() ?: 0.0) * (quantity.toDoubleOrNull() ?: 0.0)
}

sealed class CreatePositionIntent {
    data class LoadDictionaries(val projectId: Int) : CreatePositionIntent()
    data class UpdateName(val value: String) : CreatePositionIntent()
    data class SelectType(val type: EstimateItemTypeDto) : CreatePositionIntent()
    data class SelectUnit(val unit: MeasureUnitDto) : CreatePositionIntent()
    data class UpdatePrice(val value: String) : CreatePositionIntent()
    data class UpdateQuantity(val value: String) : CreatePositionIntent()
    data class Submit(val projectId: Int) : CreatePositionIntent()
}