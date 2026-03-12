package di

import data.AuthRepositoryImpl
import domain.AuthRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import presentation.LoginViewModel
import presentation.RegisterViewModel

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}