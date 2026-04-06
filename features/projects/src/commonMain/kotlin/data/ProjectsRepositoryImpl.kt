package com.entourageapp.features.projects.data

import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.features.projects.domain.ProjectCard
import com.entourageapp.features.projects.domain.ProjectsRepository
import com.entourageapp.features.projects.domain.toProjectCard
import com.entourageapp.features.projects.domain.ProjectDetail
import com.entourageapp.features.projects.domain.toProjectDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProjectsRepositoryImpl(
    private val api: ProjectsApi
) : ProjectsRepository {

    override fun getProjectsList(): Flow<List<ProjectCard>> = flow {
        val response = api.getProjects()
        val domainProjects = response.map { it.toProjectCard() }
        emit(domainProjects)
    }.catch { e ->
        throw e
    }

    override suspend fun createProject(project: ProjectCreateDto) {
        return api.createProject(project)
    }

    override fun getProjectById(projectId: Int): Flow<ProjectDetail> = flow {
        val response = api.getProjectById(projectId)
        emit(response.toProjectDetail())
    }.catch { e -> throw e }
}