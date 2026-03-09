package com.entourageapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val BASE_URL = "http://10.0.2.2:8000/" // для эмулятора
//private const val BASE_URL = "http://10.240.155.126:8000/" // для устройства

class KtorProjectsApi(private val client: HttpClient) : ProjectsApi {
    override suspend fun getProjects(): List<ProjectDto> {
        return client.get("projects/list").body()
    }
}

val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url(BASE_URL)
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
        }
    }

    single<ProjectsApi> { KtorProjectsApi(get()) }
}