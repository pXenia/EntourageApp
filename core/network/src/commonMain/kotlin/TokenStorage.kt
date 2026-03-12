package com.entourageapp.core.network

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.coroutines.flow.Flow

class TokenStorage(private val settings: ObservableSettings) {

    val accessToken: Flow<String?> = settings.getStringOrNullFlow("access_token")
    val refreshToken: Flow<String?> = settings.getStringOrNullFlow("refresh_token")

    fun saveTokens(accessToken: String, refreshToken: String) {
        settings.putString("access_token", accessToken)
        settings.putString("refresh_token", refreshToken)
    }

    fun clearTokens() {
        settings.remove("access_token")
        settings.remove("refresh_token")
    }
}