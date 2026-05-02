package com.entourageapp.features.userprofile.domain

import com.entourageapp.core.network.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    suspend fun getMe(): UserDto
    suspend fun updateName(name: String)
    suspend fun updatePassword(current: String, new: String)
    suspend fun deleteAccount(password: String)
    suspend fun logout()
    fun getProjectsList(): Flow<List<ProjectCard>>
    suspend fun deleteProject(projectId: Int)
}
