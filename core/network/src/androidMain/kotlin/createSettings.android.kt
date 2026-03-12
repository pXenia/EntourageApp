package com.entourageapp.core.network

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.context.GlobalContext

actual fun createSettings(): ObservableSettings {
    val context = GlobalContext.get().get<Context>()
    return SharedPreferencesSettings(
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    )
}