package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.LoginRequestDto
import com.entourageapp.core.network.dto.RegisterRequestDto
import com.entourageapp.core.network.dto.TokenDto
import com.entourageapp.core.network.dto.UserDto

interface AuthApi {
    suspend fun login(request: LoginRequestDto): TokenDto
    suspend fun register(request: RegisterRequestDto): UserDto
    suspend fun refreshToken(refreshToken: String): TokenDto
}