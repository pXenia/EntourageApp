package com.entourageapp

import com.entourageapp.core.network.networkModule
import com.entourageapp.features.projects.di.projectsModule
import com.entourageapp.features.rooms.di.roomsModule
import di.authModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(projectsModule, networkModule, roomsModule, authModule, mainModule)
    }
}

val mainModule = module{
    viewModel { AuthViewModel(get()) }
}