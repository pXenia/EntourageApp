package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateItemDto(
    val id: Int,
    @SerialName("project_id") val projectId: Int,
    @SerialName("room_name") val room: String,
    @SerialName("item_type_name") val itemType: String,
    val name: String,
    val quantity: Double,
    @SerialName("unit_name") val unit: String,
    val price: Double,
    val total: Double
)

@Serializable
data class EstimateListDto(
    @SerialName("items_count") val itemsCount: Int,
    @SerialName("total_sum") val totalSum: Double,
    val items: List<EstimateItemDto>
)