package com.entourageapp

import android.app.Application
import com.entourageapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class EntourageApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@EntourageApp)
        }
    }
}
