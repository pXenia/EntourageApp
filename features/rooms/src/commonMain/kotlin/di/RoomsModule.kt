package com.entourageapp.features.rooms.di

import com.entourageapp.features.rooms.data.RoomsRepositoryImpl
import com.entourageapp.features.rooms.data.StagesRepositoryImpl
import com.entourageapp.features.rooms.domain.RoomsRepository
import com.entourageapp.features.rooms.domain.StagesRepository
import com.entourageapp.features.rooms.presentation.createroom.CreateRoomVM
import com.entourageapp.features.rooms.presentation.roomInfo.RoomInfoVM
import com.entourageapp.features.rooms.presentation.roomdetil.RoomDetailVM
import com.entourageapp.features.rooms.presentation.roomlist.RoomListVM
import com.entourageapp.features.rooms.presentation.stages.StageVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val roomsModule = module {
    single<RoomsRepositoryImpl>() {
        RoomsRepositoryImpl(get())
    } bind RoomsRepository::class

    single<StagesRepositoryImpl>() {
        StagesRepositoryImpl(get())
    } bind StagesRepository::class

    viewModel { CreateRoomVM(get()) }
    viewModel { RoomListVM(get()) }
    factory { CreateRoomVM(get()) }
    viewModel { RoomDetailVM(get()) }
    viewModel { RoomInfoVM(get()) }
    viewModel { StageVM(get()) }
}