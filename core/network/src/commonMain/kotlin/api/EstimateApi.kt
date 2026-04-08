package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.EstimateListDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import com.entourageapp.core.network.dto.RoomShortDto

interface EstimateApi {
    suspend fun getUnits(projectId: Int): List<MeasureUnitDto>
    suspend fun getItemTypes(projectId: Int): List<EstimateItemTypeDto>
    suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto): Int
    suspend fun getRooms(projectId: Int): List<RoomShortDto>
    suspend fun getEstimateList(projectId: Int): EstimateListDto
    suspend fun exportEstimateXlsx(projectId: Int): ByteArray
}