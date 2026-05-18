package com.entourageapp.features.gallery.di

import com.entourageapp.features.gallery.data.GalleryRepositoryImpl
import com.entourageapp.features.gallery.domain.GalleryRepository
import com.entourageapp.features.gallery.presentation.GalleryVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val galleryModule = module {
    single<GalleryRepository>() {
        GalleryRepositoryImpl(get(),get())
    } bind GalleryRepository::class

    viewModel { GalleryVM(get()) }
}