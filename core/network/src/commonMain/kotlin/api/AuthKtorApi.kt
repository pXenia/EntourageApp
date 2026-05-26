package com.entourageapp.core.network.api

import com.entourageapp.core.network.TokenStore
import com.entourageapp.core.network.dto.AuthResponse
import com.entourageapp.core.network.dto.UserDeleteConfirmDto
import com.entourageapp.core.network.dto.UserDto
import com.entourageapp.core.network.dto.UserEmailCheckDto
import com.entourageapp.core.network.dto.UserUpdateNameDto
import com.entourageapp.core.network.dto.UserUpdatePasswordDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthKtorApi(
    private val client: HttpClient,
    private val tokenStore: TokenStore
) : AuthApi {

    override suspend fun login(email: String, password: String) {
        val response: AuthResponse = client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email, "password" to password))
        }.body()

        tokenStore.saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun register(name: String, email: String, password: String) {
        client.post("auth/register") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to name, "email" to email, "password" to password))
        }
    }

    override suspend fun refreshToken() {
        val response: AuthResponse = client.post("auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("refresh_token" to tokenStore.getRefreshToken()))
        }.body()

        tokenStore.saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun logout() {
        client.post("auth/logout") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("refresh_token" to tokenStore.getRefreshToken()))
        }
        tokenStore.clear()
    }

    override suspend fun getMe(): UserDto {
        return client.get("users/me").body()
    }

    override suspend fun updateName(name: String) {
        client.patch("users/me/name") {
            contentType(ContentType.Application.Json)
            setBody(UserUpdateNameDto(name))
        }
    }

    override suspend fun updatePassword(current: String, new: String) {
        client.patch("users/me/password") {
            contentType(ContentType.Application.Json)
            setBody(UserUpdatePasswordDto(current, new))
        }
    }

    override suspend fun deleteAccount(password: String) {
        client.delete("users/me") {
            contentType(ContentType.Application.Json)
            setBody(UserDeleteConfirmDto(password))
        }
    }

    override suspend fun checkUserEmail(email: String): UserEmailCheckDto {
        return client.get("users/check-email") {
            parameter("email", email)
        }.body()
    }
}
