package com.entourageapp.features.rooms.domain

import com.entourageapp.core.network.dto.StageDetailDto
import com.entourageapp.core.network.dto.StageDto

interface StagesRepository {
    suspend fun getStages(roomId: Int): List<StageDto>
    suspend fun getStageDetail(stageId: Int): StageDetailDto
    suspend fun createStage(roomId: Int, statusId: Int, title: String, deadline: String?)
    suspend fun updateStage(stageId: Int, title: String, deadline: String?)
    suspend fun updateStageStatus(stageId: Int, statusId: Int)
    suspend fun toggleTask(taskId: Int, isCompleted: Boolean)
    suspend fun createTask(stageId: Int, title: String, deadline: String?)
    suspend fun deleteStage(stageId: Int)
    suspend fun deleteTask(taskId: Int)
}
