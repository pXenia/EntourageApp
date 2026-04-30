package com.entourageapp.features.auth.data

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.dto.UserDto
import com.entourageapp.features.auth.domain.AuthRepository

class AuthRepositoryImpl(private val authApi: AuthApi): AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        authApi.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> = runCatching {
        authApi.register(name, email, password).message
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        authApi.logout()
    }

    override suspend fun getMe(): Result<UserDto> = runCatching {
        authApi.getMe()
    }

    override suspend fun refreshTokens(): Result<Unit> = runCatching {
        authApi.refreshToken()
    }
}