package com.entourageapp.features.projects.presentation.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projects.domain.ProjectCard
import com.entourageapp.features.projects.domain.usecases.GetProjectListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProjectListVM(
    private val getProjectListUseCase: GetProjectListUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectListState())
    val state: StateFlow<ProjectListState> = _state
    private var allProjectCards = emptyList<ProjectCard>()
    private var archiveProjectCards = emptyList<ProjectCard>()


    fun handleIntent(intent: ProjectListIntent) {
        when (intent) {
            is ProjectListIntent.LoadProjects -> loadProjects()
            is ProjectListIntent.FilterProjects -> filterProjects(intent.filter)
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            delay(1000)

            getProjectListUseCase()
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message.toString()) }
                }
                .collect { projects ->
                    allProjectCards = projects
                    archiveProjectCards = allProjectCards.filter { it.isCompleted }
                    _state.update {
                        it.copy(isLoading = false, projectCards = allProjectCards)
                    }
                }
        }
    }

    private fun filterProjects(filter: ProjectFilter) {
        when(filter){
            ProjectFilter.ALL -> _state.update { it.copy(projectFilter = ProjectFilter.ALL, projectCards = allProjectCards) }
            ProjectFilter.ARCHIVE -> _state.update { it.copy(projectFilter = ProjectFilter.ARCHIVE, projectCards = archiveProjectCards) }
        }
    }
}