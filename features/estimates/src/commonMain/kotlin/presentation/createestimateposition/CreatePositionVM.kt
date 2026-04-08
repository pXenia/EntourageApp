package presentation.createestimateposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.dto.EstimateItemCreateDto
import domain.EstimateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePositionVM(
    private val repository: EstimateRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePositionState())
    val state: StateFlow<CreatePositionState> = _state

    fun handleIntent(intent: CreatePositionIntent) {
        when (intent) {
            is CreatePositionIntent.LoadDictionaries -> loadDictionaries(intent.projectId)
            is CreatePositionIntent.UpdateName -> _state.update { it.copy(name = intent.value) }
            is CreatePositionIntent.UpdatePrice -> _state.update { it.copy(price = intent.value) }
            is CreatePositionIntent.UpdateQuantity -> _state.update { it.copy(quantity = intent.value) }
            is CreatePositionIntent.SelectType -> _state.update { it.copy(selectedType = intent.type) }
            is CreatePositionIntent.SelectUnit -> _state.update { it.copy(selectedUnit = intent.unit) }
            is CreatePositionIntent.Submit -> submitPosition(intent.projectId)
        }
    }

    private fun loadDictionaries(projectId: Int) {
        viewModelScope.launch {
            try {
                val types = repository.getItemTypes(projectId)
                val units = repository.getUnits(projectId)

                val defaultType = types.find { it.id == 1 } ?: types.firstOrNull()
                val defaultUnit = units.find { it.id == 1 } ?: units.firstOrNull()

                _state.update {
                    it.copy(
                        availableTypes = types,
                        availableUnits = units,
                        selectedType = defaultType,
                        selectedUnit = defaultUnit
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Ошибка загрузки справочников") }
            }
        }
    }

    private fun submitPosition(projectId: Int) {
        val s = _state.value

        if (s.name.isBlank() || s.quantity.isBlank() || s.price.isBlank() || s.selectedType == null || s.selectedUnit == null) {
            _state.update { it.copy(error = "Заполните все обязательные поля") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val dto = EstimateItemCreateDto(
                    roomId = s.roomId,
                    itemTypeId = s.selectedType.id,
                    name = s.name.trim(),
                    quantity = s.quantity.toDoubleOrNull() ?: 0.0,
                    unitId = s.selectedUnit.id,
                    price = s.price.toDoubleOrNull() ?: 0.0
                )

                repository.addEstimateItem(projectId, dto)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}