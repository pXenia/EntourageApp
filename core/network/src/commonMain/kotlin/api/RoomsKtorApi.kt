package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.RoomDto
import com.entourageapp.core.network.dto.RoomTypeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RoomsKtorApi(private val client: HttpClient) : RoomsApi {
    override suspend fun getRoomTypes(projectId: Int): List<RoomTypeDto> {
        return client.get("rooms/types/") {
            parameter("project_id", projectId)
        }.body()
    }
    override suspend fun getRooms(projectId: Int): List<RoomDto> {
        return client.get("projects/$projectId/rooms/").body()
    }
}
