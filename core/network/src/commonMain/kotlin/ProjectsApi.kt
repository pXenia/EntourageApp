package com.entourageapp.core.network

interface ProjectsApi {
    suspend fun getProjects(): List<ProjectDto>
}