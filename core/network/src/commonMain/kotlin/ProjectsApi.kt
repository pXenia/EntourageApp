package com.entourageapp.core.network

interface ProjectsApi {
    suspend fun getProjects(): List<ProjectDto>
    suspend fun createProject(project: ProjectCreateDto)
}