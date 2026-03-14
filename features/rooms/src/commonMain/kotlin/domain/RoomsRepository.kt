package com.entourageapp.features.rooms.domain

import kotlinx.coroutines.flow.Flow

interface RoomsRepository {
    suspend fun getRoomList(projectId: Int): Flow<List<RoomCard>>
}