package com.entourageapp.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val id: Int,
    val title: String,
    val square: String,
    @SerialName("number_of_rooms")
    val numberOfRooms: Int,
    @SerialName("number_of_participants")
    val numberOfParticipants: Int,
    val years: String,
    @SerialName("is_completed")
    val isCompleted: Boolean
)

