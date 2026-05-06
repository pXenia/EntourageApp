package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectDto
import com.entourageapp.core.network.dto.ProjectMemberDto
import com.entourageapp.core.network.dto.ProjectMembersSyncDto
import com.entourageapp.core.network.dto.ProjectSummaryDto

interface ProjectsApi {
    suspend fun getProjects(): List<ProjectDto>
    suspend fun getProjectById(projectId: Int): ProjectDto
    suspend fun createProject(project: ProjectCreateDto): Int
    suspend fun updateProject(projectId: Int, project: ProjectCreateDto)
    suspend fun addProjectMember(projectId: Int, email: String, roleCode: Int)
    suspend fun syncProjectMembers(projectId: Int, members: ProjectMembersSyncDto)
    suspend fun deleteProject(projectId: Int)
    suspend fun getProjectSummary(projectId: Int): ProjectSummaryDto
    suspend fun getProjectMembers(projectId: Int): List<ProjectMemberDto>

}
