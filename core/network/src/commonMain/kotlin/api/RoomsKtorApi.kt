package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.OffsetAddDto
import com.entourageapp.core.network.dto.OffsetDto
import com.entourageapp.core.network.dto.RoomAddDto
import com.entourageapp.core.network.dto.RoomCreatedDto
import com.entourageapp.core.network.dto.RoomDetailDto
import com.entourageapp.core.network.dto.RoomDto
import com.entourageapp.core.network.dto.RoomFullUpdateDto
import com.entourageapp.core.network.dto.RoomParamsDto
import com.entourageapp.core.network.dto.RoomTypeDto
import com.entourageapp.core.network.dto.WallAddDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RoomsKtorApi(private val client: HttpClient) : RoomsApi {
    override suspend fun getRoomTypes(projectId: Int): List<RoomTypeDto> =
        client.get("projects/$projectId/rooms/types/").body()

    override suspend fun getRooms(projectId: Int): List<RoomDto> =
        client.get("projects/$projectId/rooms/").body()

    override suspend fun createRoom(projectId: Int, room: RoomAddDto): RoomCreatedDto =
        client.post("projects/$projectId/rooms/") {
            contentType(ContentType.Application.Json)
            setBody(room)
        }.body()

    override suspend fun updateRoomFull(projectId: Int, roomId: Int, room: RoomFullUpdateDto) {
        client.put("projects/$projectId/rooms/update/$roomId") {
            contentType(ContentType.Application.Json)
            setBody(room)
        }
    }

    override suspend fun deleteRoom(projectId: Int, roomId: Int) {
        client.delete("projects/$projectId/rooms/$roomId")
    }

    override suspend fun addWall(projectId: Int, roomId: Int, wall: WallAddDto) {
        client.post("projects/$projectId/rooms/$roomId/walls/") {
            contentType(ContentType.Application.Json)
            setBody(wall)
        }
    }

    override suspend fun addOffset(projectId: Int, roomId: Int, offset: OffsetAddDto) {
        client.post("projects/$projectId/rooms/$roomId/offsets/") {
            contentType(ContentType.Application.Json)
            setBody(offset)
        }
    }

    override suspend fun getParams(projectId: Int, roomId: Int): RoomParamsDto =
        client.get("projects/$projectId/rooms/$roomId/params/").body()

    override suspend fun getRoomById(projectId: Int, roomId: Int): RoomDetailDto =
        client.get("projects/$projectId/rooms/$roomId/").body()

    override suspend fun getOffsets(projectId: Int, roomId: Int): List<OffsetDto> =
        client.get("projects/$projectId/rooms/$roomId/offsets/").body()
}