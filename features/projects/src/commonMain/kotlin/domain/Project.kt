package com.entourageapp.features.projects.domain

data class Project(
    val id: Int,
    val title: String,
    val square: String,
    val numberOfRooms: Int,
    val numberOfParticipants: Int,
    val years: String
)