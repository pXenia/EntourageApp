package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.EstimateItemCreateDto
import com.entourageapp.core.network.dto.EstimateItemTypeDto
import com.entourageapp.core.network.dto.MeasureUnitDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class EstimateKtorApi(private val client: HttpClient) : EstimateApi {
    override suspend fun getUnits(projectId: Int): List<MeasureUnitDto> {
        return client.get("/projects/$projectId/estimates/units/").body()
    }

    override suspend fun getItemTypes(projectId: Int): List<EstimateItemTypeDto> {
        return client.get("/projects/$projectId/estimates/item-types/").body()
    }

    override suspend fun addEstimateItem(projectId: Int, item: EstimateItemCreateDto): Int {
        val response = client.post("/projects/$projectId/estimates/add/") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body<JsonObject>()
        return response["item_id"]?.jsonPrimitive?.int ?: throw Exception("Ошибка ID")
    }
}