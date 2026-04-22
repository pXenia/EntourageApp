package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateItemCreateDto(
    @SerialName("room_id")
    val roomId: Int? = null,
    @SerialName("item_type_id")
    val itemTypeId: Int,
    val name: String,
    val quantity: Double,
    @SerialName("unit_id")
    val unitId: Int,
    val price: Double
)