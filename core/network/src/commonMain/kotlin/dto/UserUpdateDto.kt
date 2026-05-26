package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateNameDto(
    val name: String
)

@Serializable
data class UserUpdatePasswordDto(
    @SerialName("current_password")
    val currentPassword: String,
    @SerialName("new_password")
    val newPassword: String
)

@Serializable
data class UserDeleteConfirmDto(
    val password: String
)
