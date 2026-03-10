package com.entourageapp.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectCreateDto(
    val title: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String?,
    val square: Double? = 0.0,
    @SerialName("is_square_calculated")
    val isSquareCalculated: Boolean = false,
    val budget: Double = 0.0,
    val description: String = ""
)