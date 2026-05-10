package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.DocumentAddDto
import com.entourageapp.core.network.dto.DocumentCreatedDto
import com.entourageapp.core.network.dto.DocumentDto

interface DocumentsApi {
    suspend fun getDocuments(projectId: Int): List<DocumentDto>
    suspend fun getDocument(projectId: Int, documentId: Int): DocumentDto
    suspend fun createDocument(projectId: Int, document: DocumentAddDto): DocumentCreatedDto
    suspend fun updateDocument(projectId: Int, documentId: Int, document: DocumentAddDto)
    suspend fun deleteDocument(projectId: Int, documentId: Int)
}
