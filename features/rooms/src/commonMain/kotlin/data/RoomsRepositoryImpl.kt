package com.entourageapp.features.rooms.data

import com.entourageapp.core.network.api.RoomsApi
import com.entourageapp.features.rooms.domain.RoomCard
import com.entourageapp.features.rooms.domain.RoomDetail
import com.entourageapp.features.rooms.domain.RoomsRepository
import com.entourageapp.features.rooms.domain.toRoomCard
import com.entourageapp.features.rooms.domain.toRoomDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RoomsRepositoryImpl(
    private val api: RoomsApi
) : RoomsRepository {
    override suspend fun getRoomList(projectId: Int): Flow<List<RoomCard>> = flow {
        val response = api.getRooms(projectId)
        val domainProjects = response.map { it.toRoomCard() }
        emit(domainProjects)
    }.catch { e ->
        throw e
    }

    override fun getRoomById(projectId: Int, roomId: Int): Flow<RoomDetail> = flow {
        val response = api.getRoomById(projectId, roomId)
        emit(response.toRoomDetail())
    }.catch { e -> throw e }
}