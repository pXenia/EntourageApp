package com.entourageapp.features.projects.presentation.projectdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectDetailVM(
    private val repository: ProjectsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectDetailState())
    val state: StateFlow<ProjectDetailState> = _state

    fun handleIntent(intent: ProjectDetailIntent) {
        when (intent) {
            is ProjectDetailIntent.LoadProject -> loadProject(intent.projectId)
        }
    }

    private fun loadProject(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getProjectById(projectId)
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
                .collect { project ->
                    _state.update { it.copy(isLoading = false, project = project) }
                }
        }
    }
}