package com.entourageapp.core.network.api

import com.entourageapp.core.network.dto.DocumentAddDto
import com.entourageapp.core.network.dto.DocumentDto

interface DocumentsApi {
    suspend fun getDocuments(projectId: Int): List<DocumentDto>
    suspend fun addDocument(projectId: Int, document: DocumentAddDto)
}