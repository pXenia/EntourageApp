package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.OffsetAddDto
import com.entourageapp.core.network.dto.OffsetDto
import com.entourageapp.core.network.dto.RoomAddDto
import com.entourageapp.core.network.dto.RoomCreatedDto
import com.entourageapp.core.network.dto.RoomDetailDto
import com.entourageapp.core.network.dto.RoomDto
import com.entourageapp.core.network.dto.RoomFullUpdateDto
import com.entourageapp.core.network.dto.RoomParamsDto
import com.entourageapp.core.network.dto.RoomShortDto
import com.entourageapp.core.network.dto.RoomTypeDto
import com.entourageapp.core.network.dto.WallAddDto

interface RoomsApi {
    suspend fun getRoomTypes(projectId: Int): List<RoomTypeDto>
    suspend fun getRooms(projectId: Int): List<RoomDto>
    suspend fun getRoomsShort(projectId: Int): List<RoomShortDto>
    suspend fun createRoom(projectId: Int, room: RoomAddDto): RoomCreatedDto
    suspend fun updateRoomFull(projectId: Int, roomId: Int, room: RoomFullUpdateDto)
    suspend fun deleteRoom(projectId: Int, roomId: Int)
    suspend fun addWall(projectId: Int, roomId: Int, wall: WallAddDto)
    suspend fun addOffset(projectId: Int, roomId: Int, offset: OffsetAddDto)
    suspend fun getParams(projectId: Int, roomId: Int): RoomParamsDto
    suspend fun getRoomById(projectId: Int, roomId: Int): RoomDetailDto
    suspend fun getOffsets(projectId: Int, roomId: Int): List<OffsetDto>
}