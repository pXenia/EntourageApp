package com.entourageapp.core.network.dto.gallery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUpdateDto(
    val note: String? = null,
    @SerialName("room_id")
    val roomId: Int? = null
)
