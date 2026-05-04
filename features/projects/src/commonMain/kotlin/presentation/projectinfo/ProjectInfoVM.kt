package com.entourageapp.features.projects.presentation.projectinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectInfoVM(
    private val repository: ProjectsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectInfoState())
    val state: StateFlow<ProjectInfoState> = _state.asStateFlow()

    fun handleIntent(intent: ProjectInfoIntent) {
        when (intent) {
            is ProjectInfoIntent.LoadProject -> loadProject(intent.projectId)
        }
    }

    private fun loadProject(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val projectFlow = repository.getProjectById(projectId)
            val membersFlow = flow { emit(repository.getProjectMembers(projectId)) }

            combine(projectFlow, membersFlow) { project, members ->
                _state.update { 
                    it.copy(
                        isLoading = false,
                        project = project,
                        members = members
                    )
                }
            }.catch { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }.collect {}
        }
    }
}
