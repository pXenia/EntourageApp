package com.entourageapp.features.projects.data

import com.entourageapp.features.projects.domain.Project
import com.entourageapp.features.projects.domain.ProjectsRepository

class ProjectsRepositoryImpl() : ProjectsRepository {
    override fun getProjectsList(): List<Project> = listOf(
        Project(
            id = 1,
            title = "квартира на Ленинском",
            square = "100",
            numberOfRooms = 2,
            numberOfParticipants = 2,
            years = "2023-2026",
            isCompleted = true
        ),
        Project(
            id = 2,
            title = "Дача",
            square = "250",
            numberOfRooms = 10,
            numberOfParticipants = 10,
            years = "2025-2026",
            isCompleted = true
        ),
        Project(
            id = 3,
            title = "Первый проект",
            square = "70",
            numberOfRooms = 2,
            numberOfParticipants = 10,
            years = "202-2026"
        ),
        Project(
            id = 4,
            title = "Первый проект нашей квартиры в химках",
            square = "50",
            numberOfRooms = 19,
            numberOfParticipants = 1,
            years = "2023"
        )
    )
}