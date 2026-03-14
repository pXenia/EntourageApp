package com.entourageapp.core.network

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.api.AuthKtorApi
import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.api.ProjectsKtorApi
import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.core.network.api.RoomsKtorApi
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

actual val networkModule = module {
    single<Settings> { createSettings() }
    single { PersistentCookiesStorage(get<Settings>()) }
    single<TokenStore> { get<PersistentCookiesStorage>() }

    single {
        HttpClient {
            defaultRequest { url("http://localhost:8000/") }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
            }
            install(Logging) { level = LogLevel.ALL; logger = Logger.DEFAULT }
            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, _ ->
                    val response = (exception as? ResponseException)?.response
                        ?: return@handleResponseExceptionWithRequest
                    if (response.status == HttpStatusCode.Unauthorized) {
                        get<TokenStore>().clear()
                    }
                }
            }
        }
    }

    single<ProjectsApi> { ProjectsKtorApi(get()) }
    single<AuthApi> { AuthKtorApi(get()) }
    single<RoomsApi> { RoomsKtorApi(get()) }
}