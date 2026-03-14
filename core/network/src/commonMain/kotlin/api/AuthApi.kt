package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.LoginRequestDto
import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.RegisterRequestDto
import com.entourageapp.core.network.dto.UserDto

interface AuthApi {
    suspend fun login(request: LoginRequestDto)
    suspend fun register(request: RegisterRequestDto): MessageDto
    suspend fun refreshToken()
    suspend fun logout()
    suspend fun getMe(): UserDto
}