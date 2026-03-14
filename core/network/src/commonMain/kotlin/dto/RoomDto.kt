package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomDto(
    val id: Int,
    val title: String,
    @SerialName("type_code")
    val type: String,
    val description: String,
    val square: Float,
    @SerialName("ceiling_height")
    val ceilingHeight: Float,
)