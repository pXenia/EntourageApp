package com.entourageapp.features.estimates.data

import com.entourageapp.core.network.api.EstimateApi
import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.EstimateListDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.features.estimates.domain.EstimateRepository

class EstimateRepositoryImpl(private val api: EstimateApi) : EstimateRepository {
    override suspend fun getUnits(): List<MeasureUnitDto> = api.getUnits()
    
    override suspend fun getItemTypes(): List<EstimateItemTypeDto> = api.getItemTypes()
    
    override suspend fun getRooms(projectId: Int): List<RoomShortDto> = api.getRooms(projectId)
    
    override suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto) = 
        api.addEstimateItem(projectId, item)
    
    override suspend fun getEstimateList(projectId: Int): EstimateListDto = 
        api.getEstimateList(projectId)

    override suspend fun getEstimateItem(projectId: Int, itemId: Int): EstimateItemDto =
        api.getEstimateItem(projectId, itemId)

    override suspend fun updateEstimateItem(projectId: Int, itemId: Int, item: EstimateItemCreateDto) =
        api.updateEstimateItem(projectId, itemId, item)

    override suspend fun deleteEstimateItem(projectId: Int, itemId: Int) =
        api.deleteEstimateItem(projectId, itemId)
    
    override suspend fun exportEstimateXlsx(projectId: Int): ByteArray = 
        api.exportEstimateXlsx(projectId)
}
