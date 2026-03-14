package com.entourageapp.core.network

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

actual class PersistentCookiesStorage actual constructor(
    private val settings: Settings
) : TokenStore {

    private val _hasAccessToken = MutableStateFlow(
        settings.getStringOrNull("cookie_access_token") != null
    )
    actual override val hasAccessToken: Flow<Boolean> = _hasAccessToken

    actual override fun clear() {
        settings.remove("cookie_access_token")
        settings.remove("cookie_refresh_token")
        _hasAccessToken.value = false
    }
}