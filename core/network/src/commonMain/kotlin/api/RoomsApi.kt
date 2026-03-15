package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.RoomDto
import com.entourageapp.core.network.dto.RoomTypeDto

interface RoomsApi  {
    suspend fun getRoomTypes(projectId: Int): List<RoomTypeDto>

    suspend fun getRooms(projectId: Int): List<RoomDto>
    //suspend fun createRoom(room: RoomCreateDto)
}
