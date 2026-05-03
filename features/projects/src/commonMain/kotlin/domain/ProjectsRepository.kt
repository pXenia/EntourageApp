package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectSummaryDto
import com.entourageapp.core.network.dto.UserEmailCheckDto
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getProjectsList(): Flow<List<ProjectCard>>
    suspend fun createProject(project: ProjectCreateDto): Int
    suspend fun addProjectMember(projectId: Int, email: String, roleCode: String)
    fun getProjectById(projectId: Int): Flow<ProjectDetail>
    suspend fun checkEmail(email: String): UserEmailCheckDto
    fun getProjectSummary(projectId: Int): Flow<ProjectSummaryDto>
}
