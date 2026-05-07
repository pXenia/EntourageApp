package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.dto.ProjectDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class ProjectCard(
    val id: Int,
    val title: String,
    val square: String,
    val roomsCount: Int,
    val membersCount: Int,
    val years: String,
    val isCompleted: Boolean = false
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

    val isCompleted = endDate?.let {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        LocalDate.parse(it) < today
    } ?: false

    return ProjectCard(
        id = id,
        title = title,
        square = (square ?: 0).toInt().toString(),
        roomsCount = roomsCount,
        membersCount = membersCount,
        years = yearsString,
        isCompleted = isCompleted
    )
}