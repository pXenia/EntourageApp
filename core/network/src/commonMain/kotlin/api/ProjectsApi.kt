package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectDto

interface ProjectsApi {
    suspend fun getProjects(): List<ProjectDto>
    suspend fun getProjectById(projectId: Int): ProjectDto
    suspend fun createProject(project: ProjectCreateDto): Int
    suspend fun addProjectMember(projectId: Int, email: String, roleCode: String)
    suspend fun deleteProject(projectId: Int)
}