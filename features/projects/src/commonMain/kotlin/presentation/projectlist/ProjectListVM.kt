package com.entourageapp.features.projects.presentation.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projects.domain.ProjectCard
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectListVM(
    private val repository: ProjectsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectListState())
    val state: StateFlow<ProjectListState> = _state.asStateFlow()

    private var currentProjectCards = emptyList<ProjectCard>()
    private var archiveProjectCards = emptyList<ProjectCard>()

    fun handleIntent(intent: ProjectListIntent) {
        when (intent) {
            is ProjectListIntent.LoadProjects -> loadProjects()
            is ProjectListIntent.ChangeFilter -> changeFilter(intent.filter)
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            repository.getProjectsList().onStart {
                _state.update { it.copy(isLoading = true, error = null) }
            }.catch { error ->
                _state.update { it.copy(isLoading = false, error = "Ошибка загрузки проектов") }
            }.collect { projects ->
                _state.update { currentState ->
                    currentProjectCards = projects.filter { !it.isCompleted }
                    archiveProjectCards = projects.filter { it.isCompleted }
                    currentState.copy(
                        isLoading = false,
                        projectCards = if (currentState.projectFilter == ProjectFilter.CURRENT) currentProjectCards else archiveProjectCards,
                        error = null
                    )
                }
            }
        }
    }

    private fun changeFilter(filter: ProjectFilter) {
        _state.update { currentState ->
            currentState.copy(
                projectFilter = filter,
                projectCards = when (filter) {
                    ProjectFilter.CURRENT -> currentProjectCards
                    ProjectFilter.ARCHIVE -> archiveProjectCards
                }
            )
        }
    }
}
