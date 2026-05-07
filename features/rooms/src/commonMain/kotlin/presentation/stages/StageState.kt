package com.entourageapp.features.rooms.presentation.stages

import com.entourageapp.core.network.dto.StageStatusDto

data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val deadline: String
)

enum class StageStatus(val id: Int, val label: String) {
    COMPLETED(1, "завершена"),
    IN_PROGRESS(2, "в процессе"),
    NOT_STARTED(3, "не начата");

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id } ?: NOT_STARTED
    }
}

data class Stage(
    val id: Int,
    val title: String,
    val status: StageStatus,
    val tasks: List<Task>,
    val deadline: String
)

data class StageState(
    val stages: List<Stage> = emptyList(),
    val statuses: List<StageStatusDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface StageIntent {
    data class LoadStages(val roomId: Int) : StageIntent
    data class ToggleTask(val roomId: Int, val stageId: Int, val taskId: Int) : StageIntent
    data class UpdateStageStatus(val roomId: Int, val stageId: Int, val status: StageStatus) : StageIntent
    data class AddStage(val roomId: Int, val title: String, val deadline: String) : StageIntent
    data class AddTask(val roomId: Int, val stageId: Int, val title: String, val deadline: String) : StageIntent
}
