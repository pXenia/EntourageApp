package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.dto.ProjectDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class ProjectDetail(
    val id: Int,
    val title: String,
    val role: String,
    val square: Float?,
    val budget: Float?,
    val description: String?,
    val roomsCount: Int,
    val pastDays: Int,
    val allDays: Int,
    val progress: Float,
    val startDateFormatted: String,
    val endDateFormatted: String?
)


fun ProjectDto.toProjectDetail(): ProjectDetail {
    val start = LocalDate.parse(startDate)
    val end = endDate?.let { LocalDate.parse(it) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val allDays = if (end != null) start.daysUntil(end) else 0
    val pastDays = start.daysUntil(today).coerceIn(0, allDays)
    val progress = if (allDays > 0) pastDays.toFloat() / allDays.toFloat() else 0f

    fun LocalDate.format(): String =
        "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}.$year"

    return ProjectDetail(
        id = id,
        title = title,
        role = role,
        square = square,
        budget = budget,
        description = description,
        roomsCount = roomsCount,
        pastDays = pastDays,
        allDays = allDays,
        progress = progress,
        startDateFormatted = start.format(),
        endDateFormatted = end?.format()
    )
}