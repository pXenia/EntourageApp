package com.entourageapp.features.userprofile.domain

import com.entourageapp.core.network.dto.ProjectDto

data class ProjectCard(
    val id: Int,
    val title: String,
    val role: Int,
    val membersCount: Int,
    val years: String,
)

fun ProjectDto.toProjectCard(): ProjectCard {
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
        role = role,
        membersCount = membersCount,
        years = yearsString,
    )
}
