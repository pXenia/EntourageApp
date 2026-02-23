package com.entourageapp.features.projects.domain.usecases

import com.entourageapp.features.projects.domain.Project
import com.entourageapp.features.projects.domain.ProjectsRepository

class GetProjectListUseCaseImpl(
    private val projectsRepository: ProjectsRepository
): GetProjectListUseCase {
    override operator fun invoke(): List<Project> = projectsRepository.getProjectsList()
}