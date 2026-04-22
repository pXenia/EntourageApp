package com.entourageapp.core.network

import com.entourageapp.core.network.api.AuthApi
import com.entourageapp.core.network.api.AuthKtorApi
import com.entourageapp.core.network.api.EstimateApi
import com.entourageapp.core.network.api.EstimateKtorApi
import com.entourageapp.core.network.api.ProjectsApi
import com.entourageapp.core.network.api.ProjectsKtorApi
import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.core.network.api.RoomsKtorApi
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single<Settings> { createSettings() }
    single<TokenStore> { SettingsTokenStore(get()) }

    single(named("refreshClient")) {
        HttpClient {
            defaultRequest { url(getBaseUrl()) }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }

    single { AuthInterceptor(get(), get(named("refreshClient"))) }

    single {
        val interceptor = get<AuthInterceptor>()

        HttpClient {
            defaultRequest {
                url(getBaseUrl())
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }

            install(Logging) {
                level = LogLevel.BODY
                logger = Logger.DEFAULT
            }

            install(createClientPlugin("AuthPlugin") {
                onRequest { request, _ ->
                    interceptor.onRequest(request)
                }
            })

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, _ ->
                    val response = (exception as? ResponseException)?.response
                        ?: return@handleResponseExceptionWithRequest

                    if (response.status == HttpStatusCode.Unauthorized) {
                        interceptor.onUnauthorized()
                    }
                }
            }

            install(HttpRequestRetry) {
                maxRetries = 1
                retryIf { _, response ->
                    response.status == HttpStatusCode.Unauthorized
                }
            }
        }
    }

    single<AuthApi> { AuthKtorApi(get(), get()) }
    single<ProjectsApi> { ProjectsKtorApi(get()) }
    single<RoomsApi> { RoomsKtorApi(get()) }
    single<EstimateApi> { EstimateKtorApi(get()) }
}