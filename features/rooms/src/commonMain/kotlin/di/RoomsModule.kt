package com.entourageapp.features.rooms.di

import com.entourageapp.features.rooms.data.RoomsRepositoryImpl
import com.entourageapp.features.rooms.domain.RoomsRepository
import com.entourageapp.features.rooms.presentation.createplan.CreateRoomPlanVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val roomsModule = module {
    single<RoomsRepositoryImpl>() {
        RoomsRepositoryImpl(get())
    } bind RoomsRepository::class

    viewModel { CreateRoomPlanVM() }
}