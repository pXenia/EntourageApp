package com.entourageapp.features.userprofile.presentation.projectmanagement

import com.entourageapp.features.userprofile.domain.ProjectCard

data class ProjectManagementState(
    val projects: List<ProjectCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedProject: ProjectCard? = null,
    val isConfirmSheetVisible: Boolean = false
) {
    val ownedProjects: List<ProjectCard> get() = projects.filter { it.role == "owner" }
    val memberProjects: List<ProjectCard> get() = projects.filter { it.role != "owner" }
}

sealed interface ProjectManagementIntent {
    data object LoadProjects : ProjectManagementIntent
    data class ShowConfirmSheet(val project: ProjectCard) : ProjectManagementIntent
    data object HideConfirmSheet : ProjectManagementIntent
    data object ConfirmAction : ProjectManagementIntent
}

sealed interface ProjectManagementSideEffect {
    data class ShowError(val message: String) : ProjectManagementSideEffect
    data class ShowMessage(val message: String) : ProjectManagementSideEffect
}
