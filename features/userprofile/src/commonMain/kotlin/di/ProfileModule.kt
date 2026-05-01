package com.entourageapp.features.userprofile.di

import com.entourageapp.features.userprofile.data.UserProfileRepositoryImpl
import com.entourageapp.features.userprofile.domain.UserProfileRepository
import com.entourageapp.features.userprofile.presentation.UserProfileVM
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    singleOf(::UserProfileRepositoryImpl) { bind<UserProfileRepository>() }
    viewModelOf(::UserProfileVM)
}
