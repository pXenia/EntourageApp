package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectMemberDto(
    @SerialName("user_id")
    val id: Int,
    val name: String,
    val email: String,
    @SerialName("role_id")
    val roleId: Int
)