package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val id: Int,
    val title: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String?,
    val square: Float,
    val budget: Float,
    @SerialName("is_square_calculated")
    val isSquareCalculated: Boolean,
    val description: String,
    @SerialName("rooms_count")
    val roomsCount: Int,
    @SerialName("members_count")
    val membersCount: Int,
    @SerialName("is_completed")
    val isCompleted: Boolean
)

