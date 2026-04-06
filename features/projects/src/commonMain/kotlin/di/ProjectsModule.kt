package com.entourageapp.features.projects.di

import com.entourageapp.features.projects.data.ProjectsRepositoryImpl
import com.entourageapp.features.projects.domain.ProjectDetail
import com.entourageapp.features.projects.domain.ProjectsRepository
import com.entourageapp.features.projects.domain.usecases.GetProjectListUseCase
import com.entourageapp.features.projects.domain.usecases.GetProjectListUseCaseImpl
import com.entourageapp.features.projects.presentation.createproject.CreateProjectVM
import com.entourageapp.features.projects.presentation.projectdetail.ProjectDetailVM
import com.entourageapp.features.projects.presentation.projectlist.ProjectListVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val projectsModule = module {
    single<ProjectsRepositoryImpl>() {
        ProjectsRepositoryImpl(get())
    } bind ProjectsRepository::class
    factory<GetProjectListUseCaseImpl>() {
        GetProjectListUseCaseImpl(get())
    } bind GetProjectListUseCase::class
    viewModel { ProjectListVM(get()) }
    viewModel { CreateProjectVM(get()) }
    viewModel { ProjectDetailVM(get()) }

}