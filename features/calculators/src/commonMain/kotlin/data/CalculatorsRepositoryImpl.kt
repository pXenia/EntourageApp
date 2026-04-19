package com.entourageapp.features.calculators.data

import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.core.network.dto.WallDto
import com.entourageapp.features.calculators.domain.CalculatorsRepository

class CalculatorsRepositoryImpl(
    val api: RoomsApi
): CalculatorsRepository {
    override suspend fun getWalls(projectId: Int, roomId: Int): List<WallDto> =
        api.getWalls(projectId, roomId)
}