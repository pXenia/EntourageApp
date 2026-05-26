package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectSummaryDto(
    val budget: Double?,
    @SerialName("total_spent")
    val totalSpent: Double,
    val rooms: List<RoomSummaryDto>,
    @SerialName("by_category")
    val byCategory: List<CategorySummaryDto>
)

@Serializable
data class RoomSummaryDto(
    @SerialName("room_id")
    val roomId: Int,
    @SerialName("room_title")
    val roomTitle: String,
    @SerialName("by_category")
    val byCategory: List<CategorySummaryDto>,
    @SerialName("room_total")
    val roomTotal: Double
)

@Serializable
data class CategorySummaryDto(
    val category: String,
    val total: Double
)
