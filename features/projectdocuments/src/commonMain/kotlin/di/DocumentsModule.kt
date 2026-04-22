package com.entourageapp.features.projectdocuments.di

import com.entourageapp.features.projectdocuments.data.DocumentsRepositoryImpl
import com.entourageapp.features.projectdocuments.domain.DocumentsRepository
import com.entourageapp.features.projectdocuments.presentation.DocumentListVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val documentsModule = module {
    single<DocumentsRepositoryImpl>() {
        DocumentsRepositoryImpl(get())
    } bind DocumentsRepository::class
    viewModel { DocumentListVM(get()) }
}