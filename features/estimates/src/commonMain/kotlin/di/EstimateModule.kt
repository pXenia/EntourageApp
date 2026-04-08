package di

import data.EstimateRepositoryImpl
import domain.EstimateRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import presentation.createestimateposition.CreatePositionVM

val estimateModule = module {
    single<EstimateRepositoryImpl>() {
        EstimateRepositoryImpl(get())
    } bind EstimateRepository::class

    viewModel { CreatePositionVM(get()) }
}