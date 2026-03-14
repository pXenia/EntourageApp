package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.RoomDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RoomsKtorApi(private val client: HttpClient) : RoomsApi {
    override suspend fun getRooms(projectId: Int): List<RoomDto> {
        return client.get("projects/$projectId/rooms/").body()
    }
}
