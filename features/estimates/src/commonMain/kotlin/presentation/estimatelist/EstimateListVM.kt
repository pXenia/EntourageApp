package presentation.estimatelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.EstimateRepository
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
            is EstimateListIntent.LoadData -> loadEstimate(intent.projectId)
            is EstimateListIntent.UpdateSearch -> _state.update { it.copy(searchQuery = intent.query) }
            is EstimateListIntent.DeleteItem -> { /* логика удаления */ }
        }
    }

    private fun loadEstimate(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = repository.getEstimateList(projectId)
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
}