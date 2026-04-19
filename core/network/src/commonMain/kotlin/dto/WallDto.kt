package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WallDto (
    val id: Int,
    @SerialName("length")
    val len: Float
)