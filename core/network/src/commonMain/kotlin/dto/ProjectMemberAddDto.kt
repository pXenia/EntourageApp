package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectMemberAddDto(
    @SerialName("email") val email: String,
    @SerialName("role_id") val roleId: Int
)