package com.entourageapp.features.projectdocuments.domain

import com.entourageapp.core.network.dto.documents.DocumentDto
import kotlinx.coroutines.flow.Flow

interface DocumentsRepository {
    fun getDocuments(projectId: Int): Flow<List<DocumentDto>>
    suspend fun createDocument(projectId: Int, title: String, url: String)
    suspend fun deleteDocument(documentId: Int)
}
