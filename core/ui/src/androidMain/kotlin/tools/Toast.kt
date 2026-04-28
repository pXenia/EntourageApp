package com.entourageapp.core.ui.tools

import android.content.Context
import android.widget.Toast
import org.koin.mp.KoinPlatformTools

actual fun showToast(message: String) {
    try {
        val koin = KoinPlatformTools.defaultContext().get()
        val context = koin.get<Context>()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        // Fallback or log if Koin is not initialized
    }
}
