package com.entourageapp.core.network

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsTokenStore(private val settings: Settings) : TokenStore {
    private val _hasAccessToken = MutableStateFlow(
        settings.getStringOrNull("access_token") != null
    )
    override val hasAccessToken: Flow<Boolean> = _hasAccessToken

    override fun getAccessToken(): String? =
        settings.getStringOrNull("access_token")

    override fun getRefreshToken(): String? =
        settings.getStringOrNull("refresh_token")

    override fun saveTokens(accessToken: String, refreshToken: String) {
        settings.putString("access_token", accessToken)
        settings.putString("refresh_token", refreshToken)
        _hasAccessToken.value = true
    }

    override fun clear() {
        settings.remove("access_token")
        settings.remove("refresh_token")
        _hasAccessToken.value = false
    }
}