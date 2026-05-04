package com.entourageapp.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProjectMembersSyncDto(
    val members: List<ProjectMemberAddDto>
)
