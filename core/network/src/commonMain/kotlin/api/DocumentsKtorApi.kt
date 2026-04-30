package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.DocumentAddDto
import com.entourageapp.core.network.dto.DocumentDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DocumentsKtorApi(private val client: HttpClient) : DocumentsApi {

    override suspend fun getDocuments(projectId: Int): List<DocumentDto> =
        client.get("projects/$projectId/documents/").body()

    override suspend fun addDocument(projectId: Int, document: DocumentAddDto) {
        client.post("projects/$projectId/documents/") {
            contentType(ContentType.Application.Json)
            setBody(document)
        }
    }
}