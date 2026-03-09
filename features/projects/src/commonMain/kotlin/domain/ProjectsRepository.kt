package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.ProjectCreateDto
import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getProjectsList(): Flow<List<ProjectCard>>
    suspend fun createProject(project: ProjectCreateDto)
}