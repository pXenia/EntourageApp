package com.entourageapp.core.network.api

import com.entourageapp.core.network.TokenStore
import com.entourageapp.core.network.dto.AuthResponse
import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthKtorApi(
    private val client: HttpClient,
    private val tokenStore: TokenStore
) : AuthApi {

    override suspend fun login(email: String, password: String) {
        val response: AuthResponse = client.post("auth/login/") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email, "password" to password))
        }.body()

        tokenStore.saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun register(name: String, email: String, password: String): MessageDto {
        return client.post("auth/register/") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to name, "email" to email, "password" to password))
        }.body()
    }

    override suspend fun refreshToken() {
        val response: AuthResponse = client.post("auth/refresh/") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("refresh_token" to tokenStore.getRefreshToken()))
        }.body()

        tokenStore.saveTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun logout() {
        client.post("auth/logout/")
        tokenStore.clear()
    }

    override suspend fun getMe(): UserDto {
        return client.get("auth/me/").body()
    }
}