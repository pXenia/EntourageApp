package com.entourageapp.core.network.dto.documents

import kotlinx.serialization.Serializable

@Serializable
data class DocumentAddDto(
    val title: String,
    val url: String
)