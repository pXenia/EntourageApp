package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.LoginRequestDto
import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.RegisterRequestDto
import com.entourageapp.core.network.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthKtorApi(private val client: HttpClient) : AuthApi {
    override suspend fun login(request: LoginRequestDto) {
        client.post("auth/login/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun register(request: RegisterRequestDto): MessageDto {
        return client.post("auth/register/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun refreshToken() {
        client.post("auth/refresh/")
    }

    override suspend fun logout() {
        client.post("auth/logout/")
    }

    override suspend fun getMe(): UserDto {
        return client.get("auth/me/").body()
    }
}