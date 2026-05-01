package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val id: Int,
    val title: String,
    @SerialName("user_role")
    val role: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String?,
    val square: Float?,
    val budget: Float?,
    val description: String?,
    @SerialName("auto_calculate_square")
    val isSquareCalculated: Boolean,
    @SerialName("rooms_count")
    val roomsCount: Int,
    @SerialName("members_count")
    val membersCount: Int
)

