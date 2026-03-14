package com.entourageapp.core.network

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow


expect class PersistentCookiesStorage(settings: Settings) : TokenStore {
    override val hasAccessToken: Flow<Boolean>
    override fun clear()
}