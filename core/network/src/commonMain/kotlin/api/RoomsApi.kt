package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.RoomDto

interface RoomsApi  {
    suspend fun getRooms(projectId: Int): List<RoomDto>
    //suspend fun createRoom(room: RoomCreateDto)
}
