package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.ProjectCreateDto
import com.entourageapp.core.network.dto.ProjectDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProjectsKtorApi(private val client: HttpClient) : ProjectsApi {
    override suspend fun getProjects(): List<ProjectDto> {
        return client.get("projects/").body()
    }

    override suspend fun createProject(project: ProjectCreateDto) {
        client.post("projects/add/") {
            contentType(ContentType.Application.Json)
            setBody(project)
        }
    }
}