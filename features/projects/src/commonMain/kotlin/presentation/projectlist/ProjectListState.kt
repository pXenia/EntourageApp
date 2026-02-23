package com.entourageapp.features.projects.presentation.projectlist

import com.entourageapp.features.projects.domain.Project

data class ProjectListState(
    val isLoading: Boolean = false,
    val projectFilter: ProjectFilter = ProjectFilter.ALL,
    val projects: List<Project> = emptyList(),
    val error: Error? = null
)

sealed class ProjectListIntent {
    object LoadProjects : ProjectListIntent()
    data class FilterProjects (val filter: ProjectFilter) : ProjectListIntent()
}