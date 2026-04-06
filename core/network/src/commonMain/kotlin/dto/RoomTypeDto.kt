package com.entourageapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoomTypeDto(
    val code: String,
    val title: String
)