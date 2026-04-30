package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.dto.ProjectCreateDto
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getProjectsList(): Flow<List<ProjectCard>>
    suspend fun createProject(project: ProjectCreateDto): Int
    suspend fun addProjectMember(projectId: Int, email: String, roleCode: String)
    fun getProjectById(projectId: Int): Flow<ProjectDetail>
}