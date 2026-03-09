package com.entourageapp.features.projects.domain.usecases

import com.entourageapp.features.projects.domain.ProjectCard
import com.entourageapp.features.projects.domain.ProjectsRepository
import kotlinx.coroutines.flow.Flow

class GetProjectListUseCaseImpl(
    private val projectsRepository: ProjectsRepository
): GetProjectListUseCase {
    override operator fun invoke(): Flow<List<ProjectCard>> = projectsRepository.getProjectsList()
}