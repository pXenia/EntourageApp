package com.entourageapp.features.projects.domain.usecases

import com.entourageapp.features.projects.domain.Project

interface GetProjectListUseCase {
    operator fun invoke(): List<Project>
}
