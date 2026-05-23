package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.EstimateListDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import com.entourageapp.core.network.dto.rooms.RoomShortDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType

class EstimateKtorApi(private val client: HttpClient) : EstimateApi {
    override suspend fun getUnits(): List<MeasureUnitDto> =
        client.get("estimate/units").body()

    override suspend fun getItemTypes(): List<EstimateItemTypeDto> =
        client.get("estimate/item-types").body()

    override suspend fun getRooms(projectId: Int): List<RoomShortDto> =
        client.get("projects/$projectId/rooms/short").body()

    override suspend fun getEstimateList(projectId: Int, roomId: Int?): EstimateListDto =
        client.get("projects/$projectId/estimates"){
            roomId?.let { parameter("room_id", it) }
        }.body()

    override suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto) {
        client.post("projects/$projectId/estimates") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
    }

    override suspend fun getEstimateItem(itemId: Int): EstimateItemDto =
        client.get("estimates/$itemId").body()

    override suspend fun updateEstimateItem(itemId: Int, item: EstimateItemCreateDto) {
        client.put("estimates/$itemId") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
    }

    override suspend fun deleteEstimateItem(itemId: Int) {
        client.delete("estimates/$itemId")
    }

    override suspend fun exportEstimateXlsx(projectId: Int): ByteArray =
        client.get("projects/$projectId/estimates/export").readBytes()
}
