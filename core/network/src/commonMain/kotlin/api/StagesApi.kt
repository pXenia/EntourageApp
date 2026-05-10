package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.StageAddDto
import com.entourageapp.core.network.dto.StageDetailDto
import com.entourageapp.core.network.dto.StageDto
import com.entourageapp.core.network.dto.StagePatchDto
import com.entourageapp.core.network.dto.StageStatusDto
import com.entourageapp.core.network.dto.TaskAddDto
import com.entourageapp.core.network.dto.TaskDto
import com.entourageapp.core.network.dto.TaskPatchDto

interface StagesApi {
    suspend fun getStatuses(): List<StageStatusDto>
    suspend fun getStages(roomId: Int): List<StageDto>
    suspend fun getStageDetail(stageId: Int): StageDetailDto
    suspend fun createStage(roomId: Int, data: StageAddDto)
    suspend fun patchStage(stageId: Int, data: StagePatchDto)
    suspend fun deleteStage(stageId: Int)
    suspend fun getTasks(stageId: Int): List<TaskDto>
    suspend fun createTask(stageId: Int, data: TaskAddDto)
    suspend fun patchTask(taskId: Int, data: TaskPatchDto)
    suspend fun deleteTask(taskId: Int)
}
