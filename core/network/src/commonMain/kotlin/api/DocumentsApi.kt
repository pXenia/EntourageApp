package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.documents.DocumentAddDto
import com.entourageapp.core.network.dto.documents.DocumentDto

interface DocumentsApi {
    suspend fun getDocuments(projectId: Int): List<DocumentDto>
    suspend fun createDocument(projectId: Int, document: DocumentAddDto)
    suspend fun deleteDocument(documentId: Int)
}
