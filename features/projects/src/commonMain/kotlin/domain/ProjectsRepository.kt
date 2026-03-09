package com.entourageapp.features.projects.domain

import kotlinx.coroutines.flow.Flow

interface ProjectsRepository {
    fun getProjectsList(): Flow<List<ProjectCard>>
}