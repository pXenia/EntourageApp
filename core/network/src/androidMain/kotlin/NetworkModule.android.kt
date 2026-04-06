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
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

//private const val BASE_URL = "http://10.240.155.126:8000/" // для устройства

actual val networkModule = module {
    single<Settings> { createSettings() }
    single { PersistentCookiesStorage(get<Settings>()) }
    single<CookiesStorage> { get<PersistentCookiesStorage>() }
    single<TokenStore> { get<PersistentCookiesStorage>() }

    single(named("refreshClient")) {
        HttpClient {
            defaultRequest { url("http://10.0.2.2:8000/") }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
            }
            install(HttpCookies) { storage = get<CookiesStorage>() }
        }
    }

    single {
        val refreshClient = get<HttpClient>(named("refreshClient"))
        val tokenStore = get<TokenStore>()

        HttpClient {
            defaultRequest { url("http://10.0.2.2:8000/") }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; coerceInputValues = true })
            }
            install(Logging) { level = LogLevel.ALL; logger = Logger.DEFAULT }
            install(HttpCookies) { storage = get<CookiesStorage>() }
            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, request ->
                    val response = (exception as? ResponseException)?.response
                        ?: return@handleResponseExceptionWithRequest

                    if (response.status == HttpStatusCode.Unauthorized) {
                        if (request.url.encodedPath.contains("auth/refresh")) {
                            tokenStore.clear()
                            return@handleResponseExceptionWithRequest
                        }
                        try {
                            refreshClient.post("auth/refresh/")
                        } catch (e: Exception) {
                            tokenStore.clear()
                        }
                    }
                }
            }
            install(HttpRequestRetry) {
                maxRetries = 1
                retryIf { _, response -> response.status == HttpStatusCode.Unauthorized }
                modifyRequest { request ->
                    request.headers.remove(HttpHeaders.Cookie)
                }
            }
        }
    }

    single<ProjectsApi> { ProjectsKtorApi(get()) }
    single<AuthApi> { AuthKtorApi(get()) }
    single<RoomsApi> { RoomsKtorApi(get()) }
}