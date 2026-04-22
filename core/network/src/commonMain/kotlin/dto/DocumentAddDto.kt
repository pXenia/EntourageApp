package com.entourageapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class DocumentAddDto(
    val title: String,
    val url: String
)