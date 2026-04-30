package com.entourageapp.features.calculators.data

import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.core.network.dto.RoomParamsDto
import com.entourageapp.features.calculators.domain.CalculatorsRepository

class CalculatorsRepositoryImpl(
    val api: RoomsApi
): CalculatorsRepository {
    override suspend fun getParams(projectId: Int, roomId: Int): RoomParamsDto =
        api.getParams(projectId, roomId)
}