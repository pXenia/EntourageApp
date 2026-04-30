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

    override suspend fun addDocument(projectId: Int, title: String, url: String) {
        api.addDocument(projectId, DocumentAddDto(title = title, url = url))
    }
}