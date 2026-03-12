package com.entourageapp.core.network

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.dto.LoginRequestDto
import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectDto
import com.entourageapp.core.network.dto.RegisterRequestDto
import com.entourageapp.core.network.dto.TokenDto
import com.entourageapp.core.network.dto.UserDto
import com.russhwolf.settings.ObservableSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val BASE_URL = "http://10.0.2.2:8000/" // для эмулятора
//private const val BASE_URL = "http://10.240.155.126:8000/" // для устройства

class KtorApi(private val client: HttpClient) : ProjectsApi, AuthApi {
    override suspend fun getProjects(): List<ProjectDto> {
        return client.get("projects/list").body()
    }

    override suspend fun createProject(project: ProjectCreateDto) {
        client.post("projects/create") {
            contentType(ContentType.Application.Json)
            setBody(project)
        }
    }

    override suspend fun login(request: LoginRequestDto): TokenDto {
        return client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun register(request: RegisterRequestDto): UserDto {
        return client.post("auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun refreshToken(refreshToken: String): TokenDto {
        return client.post("auth/refresh") {
            parameter("refresh_token", refreshToken)
        }.body()
    }
}

val networkModule = module {
    single {
        HttpClient {
            defaultRequest { url(BASE_URL) }

            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenStorage = get<TokenStorage>()
                        val access = tokenStorage.accessToken.first()
                        val refresh = tokenStorage.refreshToken.first()
                        if (access != null && refresh != null)
                            BearerTokens(access, refresh)
                        else null
                    }
                    refreshTokens {
                        val tokenStorage = get<TokenStorage>()
                        val api = get<AuthApi>()
                        val refresh = tokenStorage.refreshToken.first() ?: return@refreshTokens null

                        val newTokens = runCatching { api.refreshToken(refresh) }.getOrNull()
                            ?: run {
                                tokenStorage.clearTokens()
                                return@refreshTokens null
                            }

                        tokenStorage.saveTokens(newTokens.access_token, newTokens.refresh_token)
                        BearerTokens(newTokens.access_token, newTokens.refresh_token)
                    }
                }
            }
        }
    }
    single<ObservableSettings> { createSettings() }
    single { TokenStorage(get()) }
    single<ProjectsApi> { KtorApi(get()) }
    single<AuthApi> { KtorApi(get()) }
}