package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.dto.ProjectDto

data class ProjectCard(
    val id: Int,
    val title: String,
    val square: String,
    val roomsCount: Int,
    val membersCount: Int,
    val years: String,
    val isCompleted: Boolean = false
)

fun ProjectDto.toDomain(): ProjectCard {
    val startYear = startDate.take(4)
    val endYear = endDate?.take(4)

    val yearsString = when {
        endYear != null && startYear != endYear -> "$startYear–$endYear"
        endYear != null && startYear == endYear -> startYear
        endYear == null -> startYear
        else -> ""
    }

    return ProjectCard(
        id = id,
        title = title,
        square = square.toInt().toString(),
        roomsCount = roomsCount,
        membersCount = membersCount,
        years = yearsString,
        isCompleted = true
    )
}