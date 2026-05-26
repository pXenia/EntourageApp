package com.entourageapp.features.rooms.domain

import kotlinx.coroutines.flow.Flow

interface RoomsRepository {
    suspend fun getRoomList(projectId: Int): Flow<List<RoomCard>>
    fun getRoomById(roomId: Int): Flow<RoomDetail>
    suspend fun deleteRoom(roomId: Int)
}
