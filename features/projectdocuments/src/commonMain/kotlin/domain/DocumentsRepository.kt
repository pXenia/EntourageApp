package com.entourageapp.features.projectdocuments.domain

import com.entourageapp.core.network.dto.DocumentDto
import kotlinx.coroutines.flow.Flow

interface DocumentsRepository {
    fun getDocuments(projectId: Int): Flow<List<DocumentDto>>
    suspend fun createDocument(projectId: Int, title: String, url: String)
    suspend fun getDocument(projectId: Int, documentId: Int): Flow<DocumentDto>
    suspend fun updateDocument(projectId: Int, documentId: Int, title: String, url: String)
    suspend fun deleteDocument(projectId: Int, documentId: Int)
}
