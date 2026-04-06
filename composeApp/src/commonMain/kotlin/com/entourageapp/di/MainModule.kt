package com.entourageapp.di

import com.entourageapp.presentation.AuthVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module{
    viewModel { AuthVM(get(),get()) }
}