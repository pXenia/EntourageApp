package com.entourageapp.core.network

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersistentCookiesStorage(
    private val settings: ObservableSettings
) : CookiesStorage, TokenStore  {

    private val cookies = mutableListOf<Cookie>()

    override val hasAccessToken: Flow<Boolean> =
        settings.getStringOrNullFlow("cookie_access_token").map { it != null }

    init {
        val accessToken = settings.getStringOrNull("cookie_access_token")
        val refreshToken = settings.getStringOrNull("cookie_refresh_token")

        if (accessToken != null) cookies.add(buildCookie("access_token", accessToken))
        if (refreshToken != null) cookies.add(buildCookie("refresh_token", refreshToken))
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = cookies

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        cookies.removeAll { it.name == cookie.name }
        if (!cookie.value.isNullOrEmpty()) {
            cookies.add(cookie)
            when (cookie.name) {
                "access_token" -> settings.putString("cookie_access_token", cookie.value)
                "refresh_token" -> settings.putString("cookie_refresh_token", cookie.value)
            }
        } else {
            when (cookie.name) {
                "access_token" -> settings.remove("cookie_access_token")
                "refresh_token" -> settings.remove("cookie_refresh_token")
            }
        }
    }

    override fun close() {}

    override fun clear() {
        cookies.clear()
        settings.remove("cookie_access_token")
        settings.remove("cookie_refresh_token")
    }

    private fun buildCookie(name: String, value: String) = Cookie(
        name = name,
        value = value,
        path = "/",
        httpOnly = true
    )
}