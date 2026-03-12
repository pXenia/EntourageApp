package com.entourageapp.core.network

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.StorageSettings

actual fun createSettings(): ObservableSettings = StorageSettings()
