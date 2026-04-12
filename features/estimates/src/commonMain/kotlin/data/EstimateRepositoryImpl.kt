package com.entourageapp.features.estimates.data

import com.entourageapp.core.network.api.EstimateApi
import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.features.estimates.domain.EstimateRepository

class EstimateRepositoryImpl(private val api: EstimateApi) : EstimateRepository {
    override suspend fun getUnits(projectId: Int) = api.getUnits(projectId)
    override suspend fun getItemTypes(projectId: Int) = api.getItemTypes(projectId)
    override suspend fun getRooms(projectId: Int): List<RoomShortDto> = api.getRooms(projectId)
    override suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto) = api.addEstimateItem(projectId, item)
    override suspend fun getEstimateList(projectId: Int) = api.getEstimateList(projectId)
    override suspend fun exportEstimateXlsx(projectId: Int) = api.exportEstimateXlsx(projectId)
}