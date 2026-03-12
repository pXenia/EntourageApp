package di

import data.AuthRepositoryImpl
import domain.AuthRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import presentation.RegisterViewModel
import presentation.login.LoginVM

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { LoginVM(get()) }
    viewModel { RegisterViewModel(get()) }
}