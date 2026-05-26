package com.entourageapp.features.userprofile.data

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.dto.UserDto
import com.entourageapp.features.userprofile.domain.ProjectCard
import com.entourageapp.features.userprofile.domain.UserProfileRepository
import com.entourageapp.features.userprofile.domain.toProjectCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserProfileRepositoryImpl(
    private val authApi: AuthApi,
    private val projectsApi: ProjectsApi
) : UserProfileRepository {
    override suspend fun getMe(): UserDto = authApi.getMe()

    override suspend fun updateName(name: String) {
        authApi.updateName(name)
    }

    override suspend fun updatePassword(current: String, new: String) {
        authApi.updatePassword(current, new)
    }

    override suspend fun deleteAccount(password: String) {
        authApi.deleteAccount(password)
    }

    override suspend fun logout() {
        authApi.logout()
    }

    override fun getProjectsList(): Flow<List<ProjectCard>> = flow {
        val response = projectsApi.getProjects()
        emit(response.map { it.toProjectCard() })
    }.catch { e -> throw e }

    override suspend fun deleteProject(projectId: Int) {
        projectsApi.deleteProject(projectId)
    }
}
