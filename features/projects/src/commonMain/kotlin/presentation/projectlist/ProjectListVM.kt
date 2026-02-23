package com.entourageapp.features.projects.presentation.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projects.domain.Project
import com.entourageapp.features.projects.domain.usecases.GetProjectListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProjectListVM(
    private val getProjectListUseCase: GetProjectListUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectListState())
    val state: StateFlow<ProjectListState> = _state
    private var allProjects = emptyList<Project>()
    private var currentProjects = emptyList<Project>()


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
            allProjects = getProjectListUseCase()
            currentProjects = allProjects.filter { !it.isCompleted }
            _state.update {
                it.copy(isLoading = false, projects = allProjects)
            }
        }
    }

    private fun filterProjects(filter: ProjectFilter) {
        when(filter){
            ProjectFilter.ALL -> _state.update { it.copy(projectFilter = ProjectFilter.ALL, projects = allProjects) }
            ProjectFilter.CURRENT -> _state.update { it.copy(projectFilter = ProjectFilter.CURRENT, projects = currentProjects) }
        }
    }
}