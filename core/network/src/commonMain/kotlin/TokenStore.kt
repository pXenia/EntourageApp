package com.entourageapp.core.network

import kotlinx.coroutines.flow.Flow

interface TokenStore {
    val hasAccessToken: Flow<Boolean>
    fun clear()
}