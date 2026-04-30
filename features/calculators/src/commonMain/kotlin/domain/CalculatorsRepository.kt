package com.entourageapp.features.calculators.domain

import com.entourageapp.core.network.dto.RoomParamsDto

interface CalculatorsRepository {
    suspend fun getParams(projectId: Int, roomId: Int): RoomParamsDto
}