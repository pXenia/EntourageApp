package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.UserDto
import com.entourageapp.core.network.dto.UserEmailCheckDto

interface AuthApi {
    suspend fun login(email: String, password: String)
    suspend fun register(name: String, email: String, password: String): MessageDto
    suspend fun refreshToken()
    suspend fun logout()
    suspend fun getMe(): UserDto
    suspend fun updateName(name: String): MessageDto
    suspend fun updatePassword(current: String, new: String): MessageDto
    suspend fun deleteAccount(password: String): MessageDto
    suspend fun checkUserEmail(email: String): UserEmailCheckDto
}