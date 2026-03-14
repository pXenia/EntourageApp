package com.entourageapp.core.network

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.api.AuthKtorApi
import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.api.ProjectsKtorApi
import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectDto
import com.entourageapp.core.network.dto.UserDto
import com.russhwolf.settings.ObservableSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
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

            install(HttpCookies) {
                storage = get<CookiesStorage>()
            }
        }
    }

    single<ObservableSettings> { createSettings() }
    single { PersistentCookiesStorage(get()) }
    single<CookiesStorage> { get<PersistentCookiesStorage>() }
    single<TokenStore> { get<PersistentCookiesStorage>() }
    single<ProjectsApi> { ProjectsKtorApi(get()) }
    single<AuthApi> { AuthKtorApi(get()) }
}