package com.entourageapp.core.network

import kotlinx.coroutines.flow.Flow

interface TokenStore {
    val hasAccessToken: Flow<Boolean>
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(accessToken: String, refreshToken: String)
    fun clear()
}