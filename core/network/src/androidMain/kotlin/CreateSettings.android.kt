package com.entourageapp.core.network

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.java.KoinJavaComponent.getKoin

actual fun createSettings(): Settings {
    val context = getKoin().get<Context>()
    return SharedPreferencesSettings(
        context.getSharedPreferences("entourage_prefs", Context.MODE_PRIVATE)
    )
}

//actual fun getBaseUrl(): String = "http://10.0.2.2:8000/"
actual fun getBaseUrl(): String = "http://192.168.1.102:8000/"