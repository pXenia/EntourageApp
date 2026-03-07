package com.entourageapp

import com.entourageapp.core.network.networkModule
import com.entourageapp.features.projects.di.projectsModule
import com.entourageapp.features.rooms.di.roomsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(projectsModule, networkModule, roomsModule)
    }
}

