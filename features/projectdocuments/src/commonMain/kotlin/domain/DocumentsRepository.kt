package com.entourageapp.features.projectdocuments.domain

import com.entourageapp.core.network.dto.DocumentDto
import kotlinx.coroutines.flow.Flow

interface DocumentsRepository {
    fun getDocuments(projectId: Int): Flow<List<DocumentDto>>
    suspend fun addDocument(projectId: Int, title: String, url: String)
}