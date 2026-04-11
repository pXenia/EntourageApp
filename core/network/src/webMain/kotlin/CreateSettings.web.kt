package com.entourageapp.core.network

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings

actual fun createSettings(): Settings = StorageSettings()

actual fun getBaseUrl(): String = "http://localhost:8000/"