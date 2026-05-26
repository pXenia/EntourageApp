package com.entourageapp.core.network.dto.documents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentDto(
    val id: Int,
    @SerialName("project_id")
    val projectId: Int,
    val title: String,
    val url: String
)
