package com.entourageapp.features.projects.presentation.projectlist

import com.entourageapp.features.projects.domain.ProjectCard

data class ProjectListState(
    val isLoading: Boolean = false,
    val projectFilter: ProjectFilter = ProjectFilter.CURRENT,
    val projectCards: List<ProjectCard> = emptyList(),
    val error: String? = null
)

sealed interface ProjectListIntent {
    data object LoadProjects : ProjectListIntent
    data class ChangeFilter(val filter: ProjectFilter) : ProjectListIntent
}
