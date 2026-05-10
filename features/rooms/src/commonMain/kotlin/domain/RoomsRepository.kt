package com.entourageapp.features.rooms.domain

import kotlinx.coroutines.flow.Flow

interface RoomsRepository {
    suspend fun getRoomList(projectId: Int): Flow<List<RoomCard>>
    fun getRoomById(projectId: Int, roomId: Int): Flow<RoomDetail>
    suspend fun deleteRoom(projectId: Int, roomId: Int)
}