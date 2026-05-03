package com.entourageapp.features.projects.presentation.projectinfo

import com.entourageapp.core.network.dto.ProjectMemberDto
import com.entourageapp.features.projects.domain.ProjectDetail

data class ProjectInfoState(
    val isLoading: Boolean = false,
    val project: ProjectDetail? = null,
    val members: List<ProjectMemberDto> = emptyList(),
    val error: String? = null
)

sealed interface ProjectInfoIntent {
    data class LoadProject(val projectId: Int) : ProjectInfoIntent
}
