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
    override suspend fun getStages(): List<StageDto> = api.getStages()

    override suspend fun getStageDetail(stageId: Int): StageDetailDto = api.getStageDetail(stageId)

    override suspend fun createStage(statusId: Int, title: String, deadline: String?): MessageDto =
        api.createStage(StageAddDto(statusId, title, deadline))

    override suspend fun updateStageStatus(stageId: Int, statusId: Int): MessageDto =
        api.patchStageStatus(stageId, StagePatchDto(statusId))

    override suspend fun toggleTask(stageId: Int, taskId: Int, isCompleted: Boolean): MessageDto =
        api.patchTaskComplete(stageId, taskId, TaskPatchDto(isCompleted))

    override suspend fun createTask(stageId: Int, title: String, deadline: String?): MessageDto =
        api.createTask(stageId, TaskAddDto(title, deadline))
}
