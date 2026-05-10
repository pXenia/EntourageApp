package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.DocumentAddDto
import com.entourageapp.core.network.dto.DocumentCreatedDto
import com.entourageapp.core.network.dto.DocumentDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DocumentsKtorApi(private val client: HttpClient) : DocumentsApi {

    override suspend fun getDocuments(projectId: Int): List<DocumentDto> =
        client.get("projects/$projectId/documents").body()

    override suspend fun getDocument(projectId: Int, documentId: Int): DocumentDto =
        client.get("projects/$projectId/documents/$documentId").body()

    override suspend fun createDocument(projectId: Int, document: DocumentAddDto): DocumentCreatedDto =
        client.post("projects/$projectId/documents") {
            contentType(ContentType.Application.Json)
            setBody(document)
        }.body()

    override suspend fun updateDocument(projectId: Int, documentId: Int, document: DocumentAddDto) {
        client.patch("projects/$projectId/documents/$documentId") {
            contentType(ContentType.Application.Json)
            setBody(document)
        }
    }

    override suspend fun deleteDocument(projectId: Int, documentId: Int) {
        client.delete("projects/$projectId/documents/$documentId")
    }
}
