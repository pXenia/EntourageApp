package com.entourageapp.features.projects.di

import com.entourageapp.features.projects.data.ProjectsRepositoryImpl
import com.entourageapp.features.projects.domain.ProjectsRepository
import com.entourageapp.features.projects.domain.usecases.GetProjectListUseCase
import com.entourageapp.features.projects.domain.usecases.GetProjectListUseCaseImpl
import com.entourageapp.features.projects.presentation.projectlist.ProjectListVM
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val projectsModule = module {
    viewModelOf(::ProjectListVM)
    singleOf(::ProjectsRepositoryImpl) bind ProjectsRepository::class
    singleOf(::GetProjectListUseCaseImpl) bind GetProjectListUseCase::class
}