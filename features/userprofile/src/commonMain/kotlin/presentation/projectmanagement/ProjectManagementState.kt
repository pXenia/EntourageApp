package com.entourageapp.features.userprofile.presentation.projectmanagement

import com.entourageapp.features.userprofile.domain.ProjectCard

data class ProjectManagementState(
    val projects: List<ProjectCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val ownedProjects: List<ProjectCard> get() = projects.filter { it.role == "owner" }
    val memberProjects: List<ProjectCard> get() = projects.filter { it.role != "owner" }
}

sealed interface ProjectManagementIntent {
    data object LoadProjects : ProjectManagementIntent
    data class DeleteProject(val projectId: Int) : ProjectManagementIntent
    data class LeaveProject(val projectId: Int) : ProjectManagementIntent
}

sealed interface ProjectManagementSideEffect {
    data class ShowError(val message: String) : ProjectManagementSideEffect
    data class ShowMessage(val message: String) : ProjectManagementSideEffect
}
