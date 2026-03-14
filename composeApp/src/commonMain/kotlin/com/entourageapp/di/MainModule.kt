package com.entourageapp.di

import com.entourageapp.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module{
    viewModel { AuthViewModel(get()) }
}