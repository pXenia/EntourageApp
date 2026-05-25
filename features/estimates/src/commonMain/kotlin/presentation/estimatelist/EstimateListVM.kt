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
            is EstimateListIntent.ShowActionDialog -> _state.update {
                it.copy(showActionDialog = true, selectedItemId = intent.itemId, selectedItemName = intent.itemName)
            }
            is EstimateListIntent.DismissActionDialog -> _state.update {
                it.copy(showActionDialog = false)
            }
            is EstimateListIntent.ShowDeleteDialog -> _state.update { 
                it.copy(showActionDialog = false, showDeleteDialog = true, selectedItemId = intent.itemId, selectedItemName = intent.itemName)
            }
            is EstimateListIntent.DismissDeleteDialog -> _state.update { 
                it.copy(showDeleteDialog = false, selectedItemId = null, selectedItemName = "") 
            }
            is EstimateListIntent.DeleteItem -> deleteItem(intent.projectId, intent.roomId, intent.itemId)
            is EstimateListIntent.ExportXlsx -> exportXlsx(intent.projectId)
        }
    }

    private fun loadEstimate(projectId: Int, roomId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
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

    private fun deleteItem(projectId: Int, roomId: Int, itemId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                repository.deleteEstimateItem(itemId)
                _state.update { it.copy(showDeleteDialog = false, selectedItemId = null, selectedItemName = "") }
                loadEstimate(projectId, roomId)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, showDeleteDialog = false, isLoading = false) }
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
