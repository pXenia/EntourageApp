package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.OffsetAddDto
import com.entourageapp.core.network.dto.RoomAddDto
import com.entourageapp.core.network.dto.RoomCreatedDto
import com.entourageapp.core.network.dto.RoomDto
import com.entourageapp.core.network.dto.RoomTypeDto
import com.entourageapp.core.network.dto.WallAddDto
import com.entourageapp.core.network.dto.WallDto

interface RoomsApi {
    suspend fun getRoomTypes(projectId: Int): List<RoomTypeDto>
    suspend fun getRooms(projectId: Int): List<RoomDto>
    suspend fun createRoom(projectId: Int, room: RoomAddDto): RoomCreatedDto
    suspend fun addWall(projectId: Int, roomId: Int, wall: WallAddDto): MessageDto
    suspend fun addOffset(projectId: Int, roomId: Int, offset: OffsetAddDto): MessageDto

    suspend fun getWalls(projectId: Int, roomId: Int): List<WallDto>
}