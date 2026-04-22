package com.entourageapp.features.auth.di

import com.entourageapp.features.auth.data.AuthRepositoryImpl
import com.entourageapp.features.auth.domain.AuthRepository
import com.entourageapp.features.auth.presentation.AuthVM
import com.entourageapp.features.auth.presentation.login.LoginVM
import com.entourageapp.features.auth.presentation.register.RegisterVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel { LoginVM(get()) }
    viewModel { RegisterVM(get()) }
    viewModel { AuthVM(get(), get()) }
}