package com.entourageapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserEmailCheckDto(
    val email: String,
    val name: String
)
