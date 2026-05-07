package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomDetailDto(
    val id: Int,
    val title: String,
    @SerialName("type_id")
    val typeCode: Int?,
    val description: String?,
    val square: Float?,
    @SerialName("ceiling_height")
    val ceilingHeight: Float?,
    @SerialName("furniture_total")
    val furnitureTotal: Float,
    @SerialName("components_total")
    val componentsTotal: Float,
    @SerialName("work_total")
    val workTotal: Float,
    @SerialName("room_total")
    val roomTotal: Float,
    @SerialName("project_share_percent")
    val projectSharePercent: Float,
    @SerialName("estimate_items_count")
    val estimateItemsCount: Int,
    @SerialName("photo_count")
    val photoCount: Int,
    @SerialName("stages_count")
    val stagesCount: Int,
    @SerialName("tasks_count")
    val tasksCount: Int
)