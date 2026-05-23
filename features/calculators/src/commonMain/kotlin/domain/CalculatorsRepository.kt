package com.entourageapp.features.calculators.domain

import com.entourageapp.core.network.dto.rooms.RoomParamsDto

interface CalculatorsRepository {
    suspend fun getParams(roomId: Int): RoomParamsDto
}
