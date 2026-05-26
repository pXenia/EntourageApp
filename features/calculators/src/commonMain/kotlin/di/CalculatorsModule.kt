package com.entourageapp.features.calculators.di

import com.entourageapp.features.calculators.data.CalculatorsRepositoryImpl
import com.entourageapp.features.calculators.domain.CalculatorsRepository
import com.entourageapp.features.calculators.presentation.laminate.LaminateVM
import com.entourageapp.features.calculators.presentation.paint.PaintVM
import com.entourageapp.features.calculators.presentation.wallpaper.WallpaperVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val calculatorsModule = module {
    single<CalculatorsRepository> { CalculatorsRepositoryImpl(get()) }
    viewModel { WallpaperVM(get()) }
    viewModel { PaintVM(get()) }
    viewModel { LaminateVM(get()) }
}
