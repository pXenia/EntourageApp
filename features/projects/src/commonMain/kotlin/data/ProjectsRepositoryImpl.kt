package com.entourageapp.features.projects.data

import com.entourageapp.core.network.ProjectCreateDto
import com.entourageapp.core.network.ProjectsApi
import com.entourageapp.features.projects.domain.ProjectCard
import com.entourageapp.features.projects.domain.ProjectsRepository
import com.entourageapp.features.projects.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProjectsRepositoryImpl(
    private val api: ProjectsApi
) : ProjectsRepository {

    override fun getProjectsList(): Flow<List<ProjectCard>> = flow {
        val response = api.getProjects()
        val domainProjects = response.map { it.toDomain() }
        emit(domainProjects)
    }.catch { e ->
        throw e
    }

    override suspend fun createProject(project: ProjectCreateDto) {
        return api.createProject(project)
    }
}