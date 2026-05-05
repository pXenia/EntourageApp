package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.StageAddDto
import com.entourageapp.core.network.dto.StageDetailDto
import com.entourageapp.core.network.dto.StageDto
import com.entourageapp.core.network.dto.StagePatchDto
import com.entourageapp.core.network.dto.StagePutDto
import com.entourageapp.core.network.dto.StageStatusAddDto
import com.entourageapp.core.network.dto.StageStatusDto
import com.entourageapp.core.network.dto.TaskAddDto
import com.entourageapp.core.network.dto.TaskDto
import com.entourageapp.core.network.dto.TaskPatchDto
import com.entourageapp.core.network.dto.TaskPutDto

interface StagesApi {
    suspend fun getStatuses(): List<StageStatusDto>
    suspend fun createStatus(data: StageStatusAddDto): MessageDto
    suspend fun deleteStatus(statusId: Int): MessageDto

    suspend fun getStages(): List<StageDto>
    suspend fun getStageDetail(stageId: Int): StageDetailDto
    suspend fun createStage(data: StageAddDto): MessageDto
    suspend fun updateStage(stageId: Int, data: StagePutDto): MessageDto
    suspend fun patchStageStatus(stageId: Int, data: StagePatchDto): MessageDto
    suspend fun deleteStage(stageId: Int): MessageDto

    suspend fun getTasks(stageId: Int): List<TaskDto>
    suspend fun createTask(stageId: Int, data: TaskAddDto): MessageDto
    suspend fun updateTask(stageId: Int, taskId: Int, data: TaskPutDto): MessageDto
    suspend fun patchTaskComplete(stageId: Int, taskId: Int, data: TaskPatchDto): MessageDto
    suspend fun deleteTask(stageId: Int, taskId: Int): MessageDto
}
