package com.entourageapp.features.calculators.domain

import com.entourageapp.core.network.dto.WallDto

interface CalculatorsRepository {
    suspend fun getWalls(projectId: Int, roomId: Int): List<WallDto>
}