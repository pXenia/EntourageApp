package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadedDto(
    val message: String,
    @SerialName("image_id")
    val imageId: Int
)