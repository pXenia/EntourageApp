package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectDto
import com.entourageapp.core.network.dto.ProjectMemberAddDto
import com.entourageapp.core.network.dto.ProjectMemberDto
import com.entourageapp.core.network.dto.ProjectMembersSyncDto
import com.entourageapp.core.network.dto.ProjectSummaryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class ProjectsKtorApi(private val client: HttpClient) : ProjectsApi {
    override suspend fun getProjects(): List<ProjectDto> {
        return client.get("projects/").body()
    }

    override suspend fun getProjectById(projectId: Int): ProjectDto {
        return client.get("projects/$projectId").body()
    }

    override suspend fun createProject(project: ProjectCreateDto): Int {
        val response = client.post("projects/add/") {
            contentType(ContentType.Application.Json)
            setBody(project)
        }.body<JsonObject>()
        return response["project_id"]?.jsonPrimitive?.int
            ?: throw Exception("Не удалось получить id проекта")
    }

    override suspend fun updateProject(projectId: Int, project: ProjectCreateDto) {
        client.put("projects/update/$projectId") {
            contentType(ContentType.Application.Json)
            setBody(project)
        }
    }

    override suspend fun addProjectMember(projectId: Int, email: String, roleCode: String) {
        client.post("projects/$projectId/members/") {
            contentType(ContentType.Application.Json)
            setBody(ProjectMemberAddDto(email = email, roleCode = roleCode))
        }
    }

    override suspend fun syncProjectMembers(projectId: Int, members: ProjectMembersSyncDto) {
        client.put("projects/$projectId/members/") {
            contentType(ContentType.Application.Json)
            setBody(members)
        }
    }

    override suspend fun deleteProject(projectId: Int) {
        client.delete("projects/$projectId")
    }

    override suspend fun getProjectSummary(projectId: Int): ProjectSummaryDto {
        return client.get("projects/$projectId/summary").body()
    }

    override suspend fun getProjectMembers(projectId: Int): List<ProjectMemberDto> {
        return client.get("projects/$projectId/members").body()
    }
}
