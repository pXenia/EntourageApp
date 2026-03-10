package com.entourageapp.features.projects.presentation.projectlist

import com.entourageapp.features.projects.domain.ProjectCard

data class ProjectListState(
    val isLoading: Boolean = false,
    val projectFilter: ProjectFilter = ProjectFilter.ALL,
    val projectCards: List<ProjectCard> = emptyList(),
    val error: String? = null
)

sealed class ProjectListIntent {
    object LoadProjects : ProjectListIntent()
    data class FilterProjects (val filter: ProjectFilter) : ProjectListIntent()
}