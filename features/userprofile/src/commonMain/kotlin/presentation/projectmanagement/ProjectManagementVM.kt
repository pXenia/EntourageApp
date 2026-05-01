package com.entourageapp.features.userprofile.presentation.projectmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.userprofile.domain.UserProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectManagementVM(
    private val repository: UserProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectManagementState())
    val state: StateFlow<ProjectManagementState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ProjectManagementSideEffect>()
    val sideEffect: SharedFlow<ProjectManagementSideEffect> = _sideEffect.asSharedFlow()

    fun onIntent(intent: ProjectManagementIntent) {
        when (intent) {
            is ProjectManagementIntent.LoadProjects -> loadProjects()
            is ProjectManagementIntent.DeleteProject -> deleteProject(intent.projectId)
            is ProjectManagementIntent.LeaveProject -> deleteProject(intent.projectId)
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getProjectsList()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                    _sideEffect.emit(ProjectManagementSideEffect.ShowError("Ошибка при загрузке проектов"))
                }
                .collect { projects ->
                    _state.update { it.copy(isLoading = false, projects = projects) }
                }
        }
    }

    private fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteProject(projectId)
                _sideEffect.emit(ProjectManagementSideEffect.ShowMessage("Успешно"))
                loadProjects()
            } catch (e: Exception) {
                _sideEffect.emit(ProjectManagementSideEffect.ShowError("Ошибка при выполнении операции"))
            }
        }
    }
}
