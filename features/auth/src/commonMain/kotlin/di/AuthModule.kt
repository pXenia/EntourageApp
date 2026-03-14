package di

import data.AuthRepositoryImpl
import domain.AuthRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import presentation.login.LoginVM
import presentation.register.RegisterVM

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { LoginVM(get()) }
    viewModel { RegisterVM(get()) }
}