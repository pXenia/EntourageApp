package com.entourageapp.features.rooms.di

import com.entourageapp.features.rooms.presentation.createplan.CreateRoomPlanVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val roomsModule = module {
//    single<ProjectsRepositoryImpl>() {
//        ProjectsRepositoryImpl(get())
//    } bind ProjectsRepository::class

//    factory<GetProjectListUseCaseImpl>() {
//        GetProjectListUseCaseImpl(get())
//    } bind GetProjectListUseCase::class

    viewModel { CreateRoomPlanVM() }
}