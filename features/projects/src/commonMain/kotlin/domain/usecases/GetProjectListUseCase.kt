package com.entourageapp.features.projects.domain.usecases

import com.entourageapp.features.projects.domain.Project
import kotlinx.coroutines.flow.Flow

interface GetProjectListUseCase {
    operator fun invoke(): Flow<List<Project>>
}
