package com.entourageapp.core.network

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.api.AuthKtorApi
import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.api.ProjectsKtorApi
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

actual val networkModule = module {
    single<Settings> { createSettings() }
    single { PersistentCookiesStorage(get<Settings>()) }
    single<TokenStore> { get<PersistentCookiesStorage>() }
    // нет CookiesStorage — браузер сам управляет cookies

    single {
        HttpClient {
            defaultRequest { url("http://localhost:8000/") }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
            }
            install(Logging) { level = LogLevel.ALL; logger = Logger.DEFAULT }
            // нет HttpCookies
        }
    }

    single<ProjectsApi> { ProjectsKtorApi(get()) }
    single<AuthApi> { AuthKtorApi(get()) }
}