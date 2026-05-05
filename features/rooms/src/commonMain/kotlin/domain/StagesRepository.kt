package com.entourageapp.features.rooms.domain

import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.StageDetailDto
import com.entourageapp.core.network.dto.StageDto

interface StagesRepository {
    suspend fun getStages(): List<StageDto>
    suspend fun getStageDetail(stageId: Int): StageDetailDto
    suspend fun createStage(statusId: Int, title: String, deadline: String?): MessageDto
    suspend fun updateStageStatus(stageId: Int, statusId: Int): MessageDto
    suspend fun toggleTask(stageId: Int, taskId: Int, isCompleted: Boolean): MessageDto
    suspend fun createTask(stageId: Int, title: String, deadline: String?): MessageDto
}
