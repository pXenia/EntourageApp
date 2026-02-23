package com.entourageapp

import com.entourageapp.core.network.networkModule
import com.entourageapp.features.projects.di.projectsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(projectsModule, networkModule)
    }
}

