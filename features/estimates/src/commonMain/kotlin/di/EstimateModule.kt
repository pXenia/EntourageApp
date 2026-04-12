package com.entourageapp.features.estimates.di

import com.entourageapp.features.estimates.data.EstimateRepositoryImpl
import com.entourageapp.features.estimates.domain.EstimateRepository
import com.entourageapp.features.estimates.presentation.createestimateposition.CreatePositionVM
import com.entourageapp.features.estimates.presentation.estimatelist.EstimateListVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val estimateModule = module {
    single<EstimateRepositoryImpl>() {
        EstimateRepositoryImpl(get())
    } bind EstimateRepository::class

    viewModel { CreatePositionVM(get()) }
    viewModel { EstimateListVM(get()) }
}