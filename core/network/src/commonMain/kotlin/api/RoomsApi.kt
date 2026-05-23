package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.rooms.OffsetAddDto
import com.entourageapp.core.network.dto.rooms.OffsetDto
import com.entourageapp.core.network.dto.rooms.RoomAddDto
import com.entourageapp.core.network.dto.rooms.RoomCreatedDto
import com.entourageapp.core.network.dto.rooms.RoomDetailDto
import com.entourageapp.core.network.dto.rooms.RoomDto
import com.entourageapp.core.network.dto.rooms.RoomFullUpdateDto
import com.entourageapp.core.network.dto.rooms.RoomParamsDto
import com.entourageapp.core.network.dto.rooms.RoomShortDto
import com.entourageapp.core.network.dto.rooms.RoomTypeDto
import com.entourageapp.core.network.dto.rooms.WallAddDto

interface RoomsApi {
    suspend fun getRooms(projectId: Int): List<RoomDto>
    suspend fun getRoomsShort(projectId: Int): List<RoomShortDto>
    suspend fun getRoomById(roomId: Int): RoomDetailDto
    suspend fun createRoom(projectId: Int, room: RoomAddDto): RoomCreatedDto
    suspend fun updateRoomFull(roomId: Int, room: RoomFullUpdateDto)
    suspend fun deleteRoom(roomId: Int)
    suspend fun getRoomTypes(): List<RoomTypeDto>
    suspend fun getParams(roomId: Int): RoomParamsDto
    suspend fun addWall(roomId: Int, wall: WallAddDto)
    suspend fun deleteWall(roomId: Int, wallId: Int)
    suspend fun getOffsets(roomId: Int): List<OffsetDto>
    suspend fun addOffset(roomId: Int, offset: OffsetAddDto)
    suspend fun updateOffset(roomId: Int, offsetId: Int, offset: OffsetAddDto)
    suspend fun deleteOffset(roomId: Int, offsetId: Int)
}
