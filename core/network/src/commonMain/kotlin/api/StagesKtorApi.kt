package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.MessageDto
import com.entourageapp.core.network.dto.StageAddDto
import com.entourageapp.core.network.dto.StageDetailDto
import com.entourageapp.core.network.dto.StageDto
import com.entourageapp.core.network.dto.StagePatchDto
import com.entourageapp.core.network.dto.StagePutDto
import com.entourageapp.core.network.dto.StageStatusAddDto
import com.entourageapp.core.network.dto.StageStatusDto
import com.entourageapp.core.network.dto.TaskAddDto
import com.entourageapp.core.network.dto.TaskDto
import com.entourageapp.core.network.dto.TaskPatchDto
import com.entourageapp.core.network.dto.TaskPutDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class StagesKtorApi(private val client: HttpClient) : StagesApi {
    override suspend fun getStatuses(): List<StageStatusDto> =
        client.get("stages/statuses").body()

    override suspend fun createStatus(data: StageStatusAddDto): MessageDto =
        client.post("stages/statuses") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun deleteStatus(statusId: Int): MessageDto =
        client.delete("stages/statuses/$statusId").body()

    override suspend fun getStages(): List<StageDto> =
        client.get("stages/").body()

    override suspend fun getStageDetail(stageId: Int): StageDetailDto =
        client.get("stages/$stageId").body()

    override suspend fun createStage(data: StageAddDto): MessageDto =
        client.post("stages/") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun updateStage(stageId: Int, data: StagePutDto): MessageDto =
        client.put("stages/$stageId") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun patchStageStatus(stageId: Int, data: StagePatchDto): MessageDto =
        client.patch("stages/$stageId/status") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun deleteStage(stageId: Int): MessageDto =
        client.delete("stages/$stageId").body()

    override suspend fun getTasks(stageId: Int): List<TaskDto> =
        client.get("stages/$stageId/tasks").body()

    override suspend fun createTask(stageId: Int, data: TaskAddDto): MessageDto =
        client.post("stages/$stageId/tasks") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun updateTask(stageId: Int, taskId: Int, data: TaskPutDto): MessageDto =
        client.put("stages/$stageId/tasks/$taskId") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun patchTaskComplete(stageId: Int, taskId: Int, data: TaskPatchDto): MessageDto =
        client.patch("stages/$stageId/tasks/$taskId/complete") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()

    override suspend fun deleteTask(stageId: Int, taskId: Int): MessageDto =
        client.delete("stages/$stageId/tasks/$taskId").body()
}
