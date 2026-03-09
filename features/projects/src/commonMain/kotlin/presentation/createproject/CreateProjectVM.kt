package com.entourageapp.features.projects.presentation.createproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.core.network.ProjectCreateDto
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProjectVM(
    private val repository: ProjectsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateProjectState())
    val state: StateFlow<CreateProjectState> = _state

    fun handleIntent(intent: CreateProjectIntent) {
        when (intent) {
            is CreateProjectIntent.UpdateTitle -> _state.update { it.copy(title = intent.value) }
            is CreateProjectIntent.UpdateStartDate -> _state.update { it.copy(startDate = intent.value) }
            is CreateProjectIntent.UpdateEndDate -> _state.update { it.copy(endDate = intent.value) }
            is CreateProjectIntent.UpdateSquare -> _state.update { it.copy(square = intent.value) }
            is CreateProjectIntent.UpdateBudget -> _state.update { it.copy(budget = intent.value) }
            is CreateProjectIntent.UpdateDescription -> _state.update { it.copy(description = intent.value) }
            is CreateProjectIntent.Submit -> createProject()
        }
    }

    private fun createProject() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val currentState = _state.value
                val dto = ProjectCreateDto(
                    title = currentState.title,
                    startDate = convertToBackendDate(currentState.startDate) ?: "",
                    endDate = currentState.endDate?.let { convertToBackendDate(currentState.endDate) } ?: "",
                    square = currentState.square.toDoubleOrNull() ?: 0.0,
                    budget = currentState.budget.toDoubleOrNull() ?: 0.0,
                    description = currentState.description
                )

                repository.createProject(dto)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

private fun convertToBackendDate(uiDate: String): String? {
    if (uiDate.length != 8) return null
    val day = uiDate.substring(0, 2)
    val month = uiDate.substring(2, 4)
    val year = uiDate.substring(4, 8)
    return "$year-$month-$day"
}