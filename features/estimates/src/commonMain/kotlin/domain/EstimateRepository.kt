package com.entourageapp.features.estimates.domain

import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.EstimateListDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import com.entourageapp.core.network.dto.RoomShortDto

interface EstimateRepository {
    suspend fun getUnits(): List<MeasureUnitDto>
    suspend fun getItemTypes(): List<EstimateItemTypeDto>
    suspend fun getRooms(projectId: Int): List<RoomShortDto>
    suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto)
    suspend fun getEstimateList(projectId: Int, roomId: Int?): EstimateListDto
    suspend fun getEstimateItem(projectId: Int, itemId: Int): EstimateItemDto
    suspend fun updateEstimateItem(projectId: Int, itemId: Int, item: EstimateItemCreateDto)
    suspend fun deleteEstimateItem(projectId: Int, itemId: Int)
    suspend fun exportEstimateXlsx(projectId: Int): ByteArray
}
