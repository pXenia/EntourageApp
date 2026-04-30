package com.entourageapp.core.network

import com.entourageapp.core.network.dto.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthInterceptor(
    private val tokenStore: TokenStore,
    private val refreshClient: HttpClient
) {

    fun onRequest(request: HttpRequestBuilder) {
        val token = tokenStore.getAccessToken()
        if (token != null) {
            request.headers["Authorization"] = "Bearer $token"
        }
    }

    suspend fun onUnauthorized(): Boolean {
        val refreshToken = tokenStore.getRefreshToken() ?: return false

        return try {
            val response: AuthResponse = refreshClient.post("auth/refresh/") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refresh_token" to refreshToken))
            }.body()

            tokenStore.saveTokens(response.accessToken, response.refreshToken)
            true
        } catch (e: Exception) {
            tokenStore.clear()
            false
        }
    }
}