package com.entourageapp.features.projects.domain

import com.entourageapp.core.network.ProjectDto

data class Project(
    val id: Int,
    val title: String,
    val square: String,
    val numberOfRooms: Int,
    val numberOfParticipants: Int,
    val years: String,
    val isCompleted: Boolean = false
)

fun ProjectDto.toDomain() = Project(
    id = id,
    title = title,
    square = square,
    numberOfRooms = numberOfRooms,
    numberOfParticipants = numberOfParticipants,
    years = years,
    isCompleted = isCompleted
)