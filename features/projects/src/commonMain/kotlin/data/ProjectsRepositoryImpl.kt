package com.entourageapp.features.projects.data

import com.entourageapp.core.network.ProjectsApi
import com.entourageapp.features.projects.domain.Project
import com.entourageapp.features.projects.domain.ProjectsRepository
import com.entourageapp.features.projects.domain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProjectsRepositoryImpl(
    private val api: ProjectsApi
) : ProjectsRepository {

    override fun getProjectsList(): Flow<List<Project>> = flow {
        val response = api.getProjects()
        val domainProjects = response.map { it.toDomain() }
        emit(domainProjects)
    }.catch { e ->
        throw e
    }
}