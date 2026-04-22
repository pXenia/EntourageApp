package com.entourageapp.features.auth.domain

import com.entourageapp.core.network.dto.UserDto

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun refreshTokens(): Result<Unit>
    suspend fun getMe():Result<UserDto>
}