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
    override suspend fun getRooms(projectId: Int): List<RoomDto> =
        client.get("projects/$projectId/rooms").body()

    override suspend fun getRoomsShort(projectId: Int): List<RoomShortDto> =
        client.get("projects/$projectId/rooms/short").body()

    override suspend fun getRoomById(roomId: Int): RoomDetailDto =
        client.get("rooms/$roomId").body()

    override suspend fun createRoom(projectId: Int, room: RoomAddDto): RoomCreatedDto =
        client.post("projects/$projectId/rooms") {
            contentType(ContentType.Application.Json)
            setBody(room)
        }.body()

    override suspend fun updateRoomFull(roomId: Int, room: RoomFullUpdateDto) {
        client.put("rooms/$roomId") {
            contentType(ContentType.Application.Json)
            setBody(room)
        }
    }

    override suspend fun deleteRoom(roomId: Int) {
        client.delete("rooms/$roomId")
    }

    override suspend fun getRoomTypes(): List<RoomTypeDto> =
        client.get("rooms/types").body()

    override suspend fun getParams(roomId: Int): RoomParamsDto =
        client.get("rooms/$roomId/params").body()

    override suspend fun addWall(roomId: Int, wall: WallAddDto) {
        client.post("rooms/$roomId/walls") {
            contentType(ContentType.Application.Json)
            setBody(wall)
        }
    }

    override suspend fun deleteWall(roomId: Int, wallId: Int) {
        client.delete("rooms/$roomId/walls/$wallId")
    }

    override suspend fun getOffsets(roomId: Int): List<OffsetDto> =
        client.get("rooms/$roomId/offsets").body()

    override suspend fun addOffset(roomId: Int, offset: OffsetAddDto) {
        client.post("rooms/$roomId/offsets") {
            contentType(ContentType.Application.Json)
            setBody(offset)
        }
    }

    override suspend fun updateOffset(roomId: Int, offsetId: Int, offset: OffsetAddDto) {
        client.put("rooms/$roomId/offsets/$offsetId") {
            contentType(ContentType.Application.Json)
            setBody(offset)
        }
    }

    override suspend fun deleteOffset(roomId: Int, offsetId: Int) {
        client.delete("rooms/$roomId/offsets/$offsetId")
    }
}
