package com.entourageapp.features.projects.presentation.projectdetail

import com.entourageapp.features.projects.domain.ProjectDetail

data class ProjectDetailState(
    val isLoading: Boolean = false,
    val project: ProjectDetail? = null,
    val error: String? = null
)

sealed class ProjectDetailIntent {
    data class LoadProject(val projectId: Int) : ProjectDetailIntent()
}