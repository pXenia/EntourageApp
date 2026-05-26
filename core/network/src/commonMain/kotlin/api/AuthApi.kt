package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.UserDto
import com.entourageapp.core.network.dto.UserEmailCheckDto

interface AuthApi {
    suspend fun login(email: String, password: String)
    suspend fun register(name: String, email: String, password: String)
    suspend fun refreshToken()
    suspend fun logout()
    suspend fun getMe(): UserDto
    suspend fun updateName(name: String)
    suspend fun updatePassword(current: String, new: String)
    suspend fun deleteAccount(password: String)
    suspend fun checkUserEmail(email: String): UserEmailCheckDto
}
