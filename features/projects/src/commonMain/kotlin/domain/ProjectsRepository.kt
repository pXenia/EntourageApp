package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectMemberAddDto
import com.entourageapp.core.network.dto.ProjectMemberDto
import com.entourageapp.core.network.dto.ProjectSummaryDto
import com.entourageapp.core.network.dto.UserEmailCheckDto
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getProjectsList(): Flow<List<ProjectCard>>
    suspend fun createProject(project: ProjectCreateDto): Int
    suspend fun updateProject(projectId: Int, project: ProjectCreateDto)
    suspend fun addProjectMember(projectId: Int, email: String, roleCode: String)
    suspend fun syncProjectMembers(projectId: Int, members: List<ProjectMemberAddDto>)
    fun getProjectById(projectId: Int): Flow<ProjectDetail>
    suspend fun checkEmail(email: String): UserEmailCheckDto
    fun getProjectSummary(projectId: Int): Flow<ProjectSummaryDto>
    suspend fun getProjectMembers(projectId: Int): List<ProjectMemberDto>
}
