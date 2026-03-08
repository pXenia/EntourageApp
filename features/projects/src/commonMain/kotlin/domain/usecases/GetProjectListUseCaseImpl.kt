package com.entourageapp.features.projects.domain.usecases

import com.entourageapp.features.projects.domain.Project
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProjectListUseCaseImpl(
    private val projectsRepository: ProjectsRepository
): GetProjectListUseCase {
    override operator fun invoke(): Flow<List<Project>> = flow {
        emit(listOf(
            Project(
                1,
                "",
                "",
                0,
                0,
                "",
                true,
            )
        ))
    }
}