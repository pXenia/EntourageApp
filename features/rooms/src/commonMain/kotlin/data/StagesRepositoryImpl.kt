package com.entourageapp.features.rooms.data

import com.entourageapp.core.network.api.StagesApi
import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.StageAddDto
import com.entourageapp.core.network.dto.StageDetailDto
import com.entourageapp.core.network.dto.StageDto
import com.entourageapp.core.network.dto.StagePatchDto
import com.entourageapp.core.network.dto.TaskAddDto
import com.entourageapp.core.network.dto.TaskPatchDto
import com.entourageapp.features.rooms.domain.StagesRepository

class StagesRepositoryImpl(private val api: StagesApi) : StagesRepository {
    override suspend fun getStages(roomId: Int): List<StageDto> = api.getStages(roomId)

    override suspend fun getStageDetail(stageId: Int): StageDetailDto = api.getStageDetail(stageId)

    override suspend fun createStage(roomId: Int, statusId: Int, title: String, deadline: String?)=
        api.createStage(roomId, StageAddDto(statusId, title, deadline))

    override suspend fun updateStageStatus(stageId: Int, statusId: Int) =
        api.patchStage(stageId, StagePatchDto(statusId))

    override suspend fun toggleTask(taskId: Int, isCompleted: Boolean) =
        api.patchTask(taskId, TaskPatchDto(isCompleted))

    override suspend fun createTask(stageId: Int, title: String, deadline: String?) =
        api.createTask(stageId, TaskAddDto(title, deadline))

    override suspend fun deleteStage(stageId: Int) = api.deleteStage(stageId)

    override suspend fun deleteTask(taskId: Int) = api.deleteTask(taskId)
}
