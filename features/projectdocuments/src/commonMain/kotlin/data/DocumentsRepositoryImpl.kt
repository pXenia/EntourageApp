package com.entourageapp.features.projectdocuments.data

import com.entourageapp.core.network.api.DocumentsApi
import com.entourageapp.core.network.dto.DocumentAddDto
import com.entourageapp.core.network.dto.DocumentDto
import com.entourageapp.features.projectdocuments.domain.DocumentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DocumentsRepositoryImpl(
    private val api: DocumentsApi
) : DocumentsRepository {

    override fun getDocuments(projectId: Int): Flow<List<DocumentDto>> = flow {
        emit(api.getDocuments(projectId))
    }.catch { e -> throw e }

    override suspend fun createDocument(projectId: Int, title: String, url: String) {
        api.createDocument(projectId, DocumentAddDto(title = title, url = url))
    }

    override suspend fun getDocument(projectId: Int, documentId: Int): Flow<DocumentDto> = flow {
        emit(api.getDocument(projectId, documentId))
    }.catch { e -> throw e }

    override suspend fun updateDocument(projectId: Int, documentId: Int, title: String, url: String) {
        api.updateDocument(projectId, documentId, DocumentAddDto(title = title, url = url))
    }

    override suspend fun deleteDocument(projectId: Int, documentId: Int) {
        api.deleteDocument(projectId, documentId)
    }
}
