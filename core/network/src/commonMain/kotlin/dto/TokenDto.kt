package com.entourageapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val access_token: String,
    val refresh_token: String,
    val token_type: String
)