package com.entourageapp.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StageStatusDto(
    val id: Int,
    val title: String
)

@Serializable
data class StageAddDto(
    @SerialName("status_id")
    val statusId: Int,
    val title: String,
    val deadline: String? = null
)

@Serializable
data class StagePatchDto(
    @SerialName("status_id")
    val statusId: Int
)

@Serializable
data class StagePutDto(
    @SerialName("status_id")
    val statusId: Int,
    val title: String,
    val deadline: String? = null
)

@Serializable
data class StageDto(
    val id: Int,
    @SerialName("room_id")
    val roomId: Int,
    @SerialName("status_id")
    val statusId: Int,
    val title: String,
    val deadline: String? = null
)

@Serializable
data class StageDetailDto(
    val id: Int,
    @SerialName("room_id")
    val roomId: Int,
    @SerialName("status_id")
    val statusId: Int,
    val title: String,
    val deadline: String? = null,
    val tasks: List<TaskDto> = emptyList()
)

@Serializable
data class TaskAddDto(
    val title: String,
    val deadline: String? = null,
    @SerialName("is_completed")
    val isCompleted: Boolean = false
)

@Serializable
data class TaskPatchDto(
    @SerialName("is_completed")
    val isCompleted: Boolean
)

@Serializable
data class TaskDto(
    val id: Int,
    @SerialName("stage_id")
    val stageId: Int,
    val title: String,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    val deadline: String? = null
)
