package com.entourageapp.features.projects.presentation.projectlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entourageapp.features.projects.domain.Project
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProjectListVM() : ViewModel() {
    val listOfProjects = listOf<Project>(
        Project(
            id = 1,
            title = "квартира на Ленинском",
            square = "100",
            numberOfRooms = 2,
            numberOfParticipants = 2,
            years = "2023-2026"
        ),
        Project(
            id = 2,
            title = "Дача",
            square = "250",
            numberOfRooms = 10,
            numberOfParticipants = 10,
            years = "2025-2026"
        ),
        Project(
            id = 3,
            title = "Первый проект",
            square = "70",
            numberOfRooms = 2,
            numberOfParticipants = 10,
            years = "202-2026"
        ),
        Project(
            id = 4,
            title = "Первый проект нашей квартиры в химках",
            square = "50",
            numberOfRooms = 19,
            numberOfParticipants = 1,
            years = "2023"
        )
    )
    private val _state = MutableStateFlow(ProjectListState())
    val state: StateFlow<ProjectListState> = _state

    fun handleIntent(intent: ProjectListIntent) {
        when (intent) {
            is ProjectListIntent.LoadProjects -> loadProjects()
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            delay(1000)
            _state.value = _state.value.copy(isLoading = false, projects = listOfProjects)
        }
    }
}