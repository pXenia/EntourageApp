package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val id: Int,
    @SerialName("project_id")
    val projectId: Int,
    @SerialName("room_id")
    val roomId: Int? = null,
    @SerialName("object_name")
    val objectName: String,
    val note: String? = null,
    val url: String? = null
)