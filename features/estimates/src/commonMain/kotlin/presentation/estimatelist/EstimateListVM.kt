package com.entourageapp.features.estimates.presentation.estimatelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.database.saveFile
import com.entourageapp.features.estimates.domain.EstimateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EstimateListVM(
    private val repository: EstimateRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(EstimateListState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: EstimateListIntent) {
        when (intent) {
            is EstimateListIntent.LoadData -> loadEstimate(intent.projectId, intent.roomId)
            is EstimateListIntent.UpdateSearch -> _state.update { it.copy(searchQuery = intent.query) }
            is EstimateListIntent.DeleteItem -> { /* логика удаления */ }
            is EstimateListIntent.ExportXlsx -> exportXlsx(intent.projectId)
        }
    }

    private fun loadEstimate(projectId: Int, roomId: Int) {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = repository.getEstimateList(projectId, if (roomId == 0) null else roomId)
                _state.update { it.copy(
                    items = response.items,
                    totalSum = response.totalSum,
                    itemsCount = response.itemsCount,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun exportXlsx(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true) }
            runCatching {
                val bytes = repository.exportEstimateXlsx(projectId)
                saveFile("estimate.xlsx", bytes)
            }.onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
            _state.update { it.copy(isExporting = false) }
        }
    }
}