package com.entourageapp.di

import com.entourageapp.core.network.networkModule
import com.entourageapp.features.auth.di.authModule
import com.entourageapp.features.calculators.di.calculatorsModule
import com.entourageapp.features.estimates.di.estimateModule
import com.entourageapp.features.projectdocuments.di.documentsModule
import com.entourageapp.features.projects.di.projectsModule
import com.entourageapp.features.rooms.di.roomsModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            projectsModule,
            networkModule,
            roomsModule,
            authModule,
            estimateModule,
            calculatorsModule,
            documentsModule
        )
    }
}