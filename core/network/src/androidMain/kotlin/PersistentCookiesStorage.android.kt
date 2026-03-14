package com.entourageapp.core.network

import com.russhwolf.settings.Settings
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

actual class PersistentCookiesStorage actual constructor(
    private val settings: Settings
) : CookiesStorage, TokenStore {
    private val _cookies = mutableListOf<Cookie>()

    private val _hasAccessToken = MutableStateFlow(
        settings.getStringOrNull("cookie_access_token") != null
    )
    actual override val hasAccessToken: Flow<Boolean> = _hasAccessToken

    init {
        val accessToken = settings.getStringOrNull("cookie_access_token")
        val refreshToken = settings.getStringOrNull("cookie_refresh_token")
        if (accessToken != null) _cookies.add(buildCookie("access_token", accessToken))
        if (refreshToken != null) _cookies.add(buildCookie("refresh_token", refreshToken))
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override suspend fun get(requestUrl: Url): List<Cookie> = _cookies

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        _cookies.removeAll { it.name == cookie.name }
        if (!cookie.value.isNullOrEmpty()) {
            _cookies.add(cookie)
            when (cookie.name) {
                "access_token" -> {
                    settings.putString("cookie_access_token", cookie.value)
                    _hasAccessToken.value = true
                }

                "refresh_token" -> settings.putString("cookie_refresh_token", cookie.value)
            }
        } else {
            when (cookie.name) {
                "access_token" -> {
                    settings.remove("cookie_access_token")
                    _hasAccessToken.value = false
                }

                "refresh_token" -> settings.remove("cookie_refresh_token")
            }
        }
    }

    override fun close() {}

    actual override fun clear() {
        _cookies.clear()
        settings.remove("cookie_access_token")
        settings.remove("cookie_refresh_token")
        _hasAccessToken.value = false
    }

    private fun buildCookie(name: String, value: String) = Cookie(
        name = name, value = value, path = "/", httpOnly = true
    )
}