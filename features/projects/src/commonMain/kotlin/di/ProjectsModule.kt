package com.entourageapp.features.projects.di

import com.entourageapp.features.projects.presentation.projectlist.ProjectListVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val projectsModule = module {
    viewModel<ProjectListVM> { ProjectListVM() }
}